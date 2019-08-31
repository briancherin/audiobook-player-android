package com.corson.audiobookplayer.api;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.appsync.sigv4.CognitoUserPoolsAuthProvider;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import com.amazonaws.amplify.generated.graphql.ListBooksQuery;
import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

import javax.annotation.Nonnull;

public class Database {

    private AWSAppSyncClient awsAppSyncClient;

    public Database(Context context) {
        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(context)
                .awsConfiguration(new AWSConfiguration(context))
                .cognitoUserPoolsAuthProvider(new CognitoUserPoolsAuthProvider() {
                    @Override
                    public String getLatestAuthToken() {
                        try {
                            return AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                        } catch (Exception e) {
                            Log.e("APPSYNC_ERROR", e.getLocalizedMessage());
                            return e.getLocalizedMessage();
                        }
                    }
                })
                .build();
    }

    public void fetchBooks(final ICallback<ArrayList<Audiobook>> clientCallback) {

        GraphQLCall.Callback<ListBooksQuery.Data> booksCallback = new GraphQLCall.Callback<ListBooksQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListBooksQuery.Data> response) {
                ArrayList<Audiobook> books = new ArrayList<>();
                for(ListBooksQuery.Item item : response.data().listBooks().items()) {
                    books.add(new Audiobook(item.id(), item.title(), item.fileExtension()));
                }

                clientCallback.onResult(books);

            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
            }
        };

        awsAppSyncClient.query(ListBooksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(booksCallback);
    }



}

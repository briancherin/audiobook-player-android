package com.corson.audiobookplayer.api;

import android.content.Context;
import android.util.Log;

import com.amazonaws.amplify.generated.graphql.CreateBookMutation;
import com.amazonaws.amplify.generated.graphql.GetBookQuery;
import com.amazonaws.amplify.generated.graphql.UpdateBookMutation;
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

import type.CreateBookInput;
import type.UpdateBookInput;

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


    public void updateCurrentPosition(String audiobookId, int newTimestamp) {

        UpdateBookInput updateBookInput = UpdateBookInput.builder()
                .id(audiobookId)
                .currentPositionMillis(newTimestamp)
                .build();

        GraphQLCall.Callback<UpdateBookMutation.Data> mutationCallback = new GraphQLCall.Callback<UpdateBookMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateBookMutation.Data> response) {
                Log.i("Results", "Updated current position.");
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
            }
        };

        awsAppSyncClient.mutate(UpdateBookMutation.builder().input(updateBookInput).build())
                .enqueue(mutationCallback);

    }

    public void getCurrentPosition(String audiobookId, final ICallback<Integer> callback) {

        GraphQLCall.Callback<GetBookQuery.Data> bookCallback = new GraphQLCall.Callback<GetBookQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetBookQuery.Data> response) {
                callback.onResult(response.data().getBook().currentPositionMillis());
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        };

        awsAppSyncClient.query(GetBookQuery.builder().id(audiobookId).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(bookCallback);

    }


    public void getLastDeviceUsed(String audiobookId, final ICallback<String> callback) {
        GraphQLCall.Callback<GetBookQuery.Data> bookCallback = new GraphQLCall.Callback<GetBookQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GetBookQuery.Data> response) {
                callback.onResult(response.data().getBook().lastDeviceUsed());
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        };

        awsAppSyncClient.query(GetBookQuery.builder().id(audiobookId).build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(bookCallback);
    }



    public void updateLastDeviceUsed(String audiobookId, String thisDeviceId) {
        UpdateBookInput updateBookInput = UpdateBookInput.builder()
                .id(audiobookId)
                .lastDeviceUsed(thisDeviceId)
                .build();

        GraphQLCall.Callback<UpdateBookMutation> mutationCallback = new GraphQLCall.Callback<UpdateBookMutation>() {
            @Override
            public void onResponse(@Nonnull Response<UpdateBookMutation> response) {
                Log.i("Results", "Updated last deviced used as current device.");
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("Error", e.toString());
            }
        };
    }
}

package com.corson.audiobookplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.corson.audiobookplayer.api.AudiobookManager;
import com.corson.audiobookplayer.api.Factory;
import com.corson.audiobookplayer.api.ICallback;
import com.corson.audiobookplayer.model.Audiobook;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListRecyclerViewAdapter.ItemClickListener{


    private static final int RC_SIGN_IN = 23;
    private Factory factory;
    private AudiobookManager audiobookManager;

    private FirebaseAuth firebaseAuth;

    BookListRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<Audiobook> savedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.bookListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        factory = new Factory(this);



        firebaseAuth = FirebaseAuth.getInstance();
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
                RC_SIGN_IN);
/*
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.i("INIT", "onResult: " + result.getUserState());
                switch(result.getUserState()) {
                    case SIGNED_IN:
                        //Get list of books

                       initializeBookList();

                        break;
                    case SIGNED_OUT:
                        showSignIn();
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("INIT", "Initialization error.", e);
            }
        });
*/
    }


/*
    public void showSignIn() {
        AWSMobileClient.getInstance().showSignIn(this, new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.d("SHOW_SIGN_IN", "onResult: " + result.getUserState());
            }

            @Override
            public void onError(Exception e) {
                Log.e("SHOW_SIGN_IN", "onError: ", e);
            }
        });
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                System.out.println("Signed in ok");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                initializeBookList();
            } else {
                //Sign in failed
                System.out.println("sign in failed");
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, Player.class);
        intent.putExtra(MyConstants.BUNDLE_BOOK_ID_EXTRA, savedBooks.get(position).getId());
        intent.putExtra(MyConstants.BUNDLE_BOOK_TITLE_EXTRA, savedBooks.get(position).getTitle());
        intent.putExtra(MyConstants.BUNDLE_BOOK_FILE_EXTENSION_EXTRA, savedBooks.get(position).getFileExtension());
        startActivity(intent);
    }

    public void initializeBookList() {

        audiobookManager = factory.createAudiobookManager();

        audiobookManager.listBooks(new ICallback<ArrayList<Audiobook>>() {
            @Override
            public void onResult(ArrayList<Audiobook> booksResult) {
                savedBooks = booksResult;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new BookListRecyclerViewAdapter(getApplicationContext(), savedBooks);
                        adapter.setClickListener(MainActivity.this);
                        recyclerView.setAdapter(adapter);

                        System.out.println("FETCHED BOOK KEYS:");
                        System.out.println(savedBooks);
                    }
                });

            }
        });
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

}

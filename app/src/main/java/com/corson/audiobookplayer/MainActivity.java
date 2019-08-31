package com.corson.audiobookplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.corson.audiobookplayer.api.AudioManager;
import com.corson.audiobookplayer.api.Factory;
import com.corson.audiobookplayer.api.ICallback;
import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListRecyclerViewAdapter.ItemClickListener{


    private Factory factory;
    private AudioManager audioManager;

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

        try {
            audioManager = factory.createAudioManager();
        } catch (Factory.NoContextProvidedException e) {
            e.printStackTrace();
        }

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails result) {
                Log.i("INIT", "onResult: " + result.getUserState());
                switch(result.getUserState()) {
                    case SIGNED_IN:
                        //Get list of books

                        audioManager.listBooks(new ICallback<ArrayList<Audiobook>>() {
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

    }

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


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, Player.class);
        intent.putExtra(MyConstants.BUNDLE_BOOK_ID_EXTRA, savedBooks.get(position).getId());
        intent.putExtra(MyConstants.BUNDLE_BOOK_TITLE_EXTRA, savedBooks.get(position).getTitle());
        intent.putExtra(MyConstants.BUNDLE_BOOK_FILE_EXTENSION_EXTRA, savedBooks.get(position).getFileExtension());
        startActivity(intent);
    }

}

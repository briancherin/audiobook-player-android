package com.corson.audiobookplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.corson.audiobookplayer.api.AudiobookManager;
import com.corson.audiobookplayer.api.AuthHelper;
import com.corson.audiobookplayer.api.Factory;
import com.corson.audiobookplayer.api.ICallback;
import com.corson.audiobookplayer.model.Audiobook;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListRecyclerViewAdapter.ItemClickListener{


    private static final int RC_SIGN_IN = 23;
    private Factory factory;
    private AudiobookManager audiobookManager;

    List<AuthUI.IdpConfig> providers;

    BookListRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarToggle;

    ArrayList<Audiobook> savedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = new Factory(this);

        //Set up list of audiobooks
        recyclerView = findViewById(R.id.bookListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Set up side navigation bar
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navdraweropen, R.string.navdrawerclose);
        drawerLayout.addDrawerListener(actionBarToggle);
        actionBarToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id) {
                    case R.id.nav_menu_logout:
                        signOut();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Set up auth
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        //If user isn't signed in, prompt for sign in
        if (AuthHelper.getCurrentUserId() == null) {
            showSignIn();
        } else {
            initializeBookList();
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                System.out.println("Signed in ok");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                initializeBookList();
            } else {
                //Sign in failed
                System.out.println("sign in failed");
                showSignIn();
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

    //Nav drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void showSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        //Prompt for sign in
        showSignIn();


        //Erase old user's stuff
        adapter.clearAll();
    }

}

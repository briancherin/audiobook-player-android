package com.corson.audiobookplayer.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.corson.audiobookplayer.model.Audiobook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class FirebaseDatabaseHelper implements IDatabase {

    private DatabaseReference booksReference;   //Reference to the logged-in user's list of books

    public FirebaseDatabaseHelper(Context context) {
        booksReference = FirebaseDatabase.getInstance().getReference()
                .child("booksData")
                .child(AuthHelper.getCurrentUserId());
    }

    public void fetchBooks(final ICallback<ArrayList<Audiobook>> clientCallback) {



        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Audiobook> bookList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Audiobook book = postSnapshot.getValue(Audiobook.class);
                    book.setId(postSnapshot.getKey());
                    bookList.add(book);
                    System.out.println("Adding book: " + book.getTitle() + ", id: " + book.getId() + ", fileExtension: " + book.getFileExtension());
                }

                clientCallback.onResult(bookList);
            };

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateCurrentPosition(String audiobookId, int newTimestamp) {

        booksReference.child(audiobookId).child("currentPositionMillis").setValue(newTimestamp);

    }

    public void getCurrentPosition(String audiobookId, final ICallback<Integer> callback) {
        System.out.println("Enterred FbDbHelper.getCurrentPosition()");
        booksReference.child(audiobookId).child("currentPositionMillis").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer timestamp = dataSnapshot.getValue(Integer.class);
                System.out.println("FbDbHelper.getCurrentPosition() : timestamp = " + timestamp);
                callback.onResult(timestamp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void getLastDeviceUsed(String audiobookId, final ICallback<String> callback) {
        booksReference.child(audiobookId).child("deviceLastUsed").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastDeviceUsedId = dataSnapshot.getValue(String.class);
                callback.onResult(lastDeviceUsedId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void updateLastDeviceUsed(String audiobookId, String thisDeviceId) {
        booksReference.child(audiobookId).child("lastDeviceUsed").setValue(thisDeviceId);
    }
}

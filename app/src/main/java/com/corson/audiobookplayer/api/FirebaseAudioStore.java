package com.corson.audiobookplayer.api;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseAudioStore implements AudioStore {

    private static final String BUCKET_NAME = "audiobook-player-files-audiobkenv";

    StorageReference booksStorageRef;

    public FirebaseAudioStore() {

        String currentUserId = AuthHelper.getCurrentUserId();
        System.out.println("Current user id: (in FBAudioStore)" + currentUserId);

        booksStorageRef = FirebaseStorage.getInstance()
                .getReference("books")
                .child(currentUserId);
    }

    @Override
    public void getAudioStreamUrl(String bookId, String fileExtension, final ICallback<String> callback) {
        booksStorageRef.child(bookId + "." + fileExtension).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                callback.onResult(url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("AUDIO_STREAM_URL", e.getMessage());
            }
        });
    }

}

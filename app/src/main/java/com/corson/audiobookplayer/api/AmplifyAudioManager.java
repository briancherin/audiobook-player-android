package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class AmplifyAudioManager implements AudioManager {

    Database database;

    public AmplifyAudioManager(Context context) {
        database = new Database(context);
    }

    @Override
    public void listBooks(ICallback<ArrayList<Audiobook>> callback) {
        database.fetchBooks(callback);
    }

    @Override
    public void updateCurrentPosition() {

    }
}

package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class FirebaseAudiobookManager implements IAudiobookManager, OnlineAudiobookManager {

    FirebaseDatabaseHelper database;
    IDeviceInformationManager deviceInformationManager;

    public FirebaseAudiobookManager(Context context, IDeviceInformationManager deviceInformationManager) {
        database = new FirebaseDatabaseHelper(context);
        this.deviceInformationManager = deviceInformationManager;
    }

    @Override
    public void listBooks(ICallback<ArrayList<Audiobook>> callback) {
        database.fetchBooks(callback);
    }

    @Override
    public void updateCurrentPosition(String audiobookId, int newTimestamp) {
        //TODO: Update current position to database
        //Specify this device as deviceLastUsed (some sort of id uniquely identifying this device?)
        database.updateCurrentPosition(audiobookId, newTimestamp);
    }

    @Override
    public void getCurrentPosition(String audiobookId, ICallback<Integer> callback) {
        database.getCurrentPosition(audiobookId, callback);
    }

    @Override
    public void getLastDeviceUsed(String audiobookId, ICallback<String> callback) {
        database.getLastDeviceUsed(audiobookId, callback);
    }

    @Override
    public void updateLastDeviceUsed(String audiobookId) {
        //TOOD: Update lastDeviceUsed with current device
        String thisDeviceId = deviceInformationManager.getDeviceId();
        //Upload to database
        database.updateLastDeviceUsed(audiobookId, thisDeviceId);
    }

}

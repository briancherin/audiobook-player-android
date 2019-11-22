package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class AmplifyAudiobookManager implements IAudiobookManager, OnlineAudiobookManager {

    Database database;
    IDeviceInformationManager deviceInformationManager;

    public AmplifyAudiobookManager(Context context, IDeviceInformationManager deviceInformationManager) {
        database = new Database(context);
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
    public int getCurrentPosition(String audiobookId) {
        return database.getCurrentPosition(audiobookId);
    }

    @Override
    public String getLastDeviceUsed(String audiobookId) {
        return database.getLastDeviceUsed(audiobookId);
    }

    @Override
    public void updateLastDeviceUsed(String audiobookId) {
        //TOOD: Update lastDeviceUsed with current device
        String thisDeviceId = deviceInformationManager.getDeviceId();
        //Upload to database
        database.updateLastDeviceUsed(audiobookId, thisDeviceId);
    }


}

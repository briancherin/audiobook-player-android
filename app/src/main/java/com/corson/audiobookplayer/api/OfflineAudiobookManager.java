package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class OfflineAudiobookManager /*implements IAudiobookManager*/ {

    private Context context;
    private OfflineDataStore offlineDataStore;
    private Factory factory;

    public OfflineAudiobookManager(Context context) {
        this.context = context;
        factory = new Factory(context);

        offlineDataStore = new OfflineDataStore(context);
    }


//    @Override
    public void listBooks(ICallback<ArrayList<Audiobook>> callback) {
        //TODO: Provide way to access list of books while offline
    }

//    @Override
    public void updateCurrentPosition(String audioBookId, int timestampInSeconds) {
        String key = getCurrentPositionKey(audioBookId);
        offlineDataStore.saveKeyValue(key, timestampInSeconds);
    }

//    @Override
    public int getCurrentPosition(String audiobookId) {
        String key = getCurrentPositionKey(audiobookId);
        return offlineDataStore.getKeyValueInt(key);
    }

    private String getCurrentPositionKey(String audiobookId) {
        return "curr_timestamp_" + audiobookId;
    }

}

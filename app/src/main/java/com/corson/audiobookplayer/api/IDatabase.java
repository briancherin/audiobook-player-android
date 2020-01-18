package com.corson.audiobookplayer.api;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public interface IDatabase {
    public void fetchBooks(final ICallback<ArrayList<Audiobook>> clientCallback);

    public void updateCurrentPosition(String audiobookId, int newTimestamp);

    public void getCurrentPosition(String audiobookId, final ICallback<Integer> callback);

    public void getLastDeviceUsed(String audiobookId, final ICallback<String> callback);

    public void updateLastDeviceUsed(String audiobookId, String thisDeviceId);


}

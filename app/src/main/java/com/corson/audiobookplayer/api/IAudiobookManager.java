package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public interface IAudiobookManager {

    /**
     * Get the list of audiobooks stored for this user
     * @param callback  A callback method which receives an ArrayList of Audiobook objects
     */
    void listBooks(ICallback<ArrayList<Audiobook>> callback);

    /**
     * Save to the database the most recently listened-to timestamp for this audiobook
     * @param audiobookId
     * @param newTimestamp
     */
    void updateCurrentPosition(String audiobookId, int newTimestamp);

    /**
     * Retrieve the latest timestamp listened to for the specified audiobook
     * @param audiobookId   The id of the audiobook being queried
     * @param callback The callback which will receive the integer of the current timestamp
     * @return  The current position in seconds
     */
    void getCurrentPosition(String audiobookId, ICallback<Integer> callback);
}





package com.corson.audiobookplayer.api;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class AudiobookManager {
    OfflineAudiobookManager offlineAudiobookManager;
    FirebaseAudiobookManager firebaseAudiobookManager;
    IDeviceInformationManager deviceInformationManager;

    boolean shouldUseOnline;

    public AudiobookManager(OfflineAudiobookManager offlineAudiobookManager, FirebaseAudiobookManager firebaseAudiobookManager, IDeviceInformationManager deviceInformationManager, boolean shouldUseOnline) {
        this.offlineAudiobookManager = offlineAudiobookManager;
        this.firebaseAudiobookManager = firebaseAudiobookManager;
        this.deviceInformationManager = deviceInformationManager;
        this.shouldUseOnline = shouldUseOnline;
    }

    public void listBooks(ICallback<ArrayList<Audiobook>> callback) {
        if (shouldUseOnline) {
            firebaseAudiobookManager.listBooks(callback);
        } else {
            offlineAudiobookManager.listBooks(callback);
        }
    }

    public void updateCurrentPositionOnline(String audiobookId, int timestampSeconds) {
        firebaseAudiobookManager.updateCurrentPosition(audiobookId, timestampSeconds);
    }

    public void getCurrentPositionOnline(String audiobookId, ICallback<Integer> callback) {
        firebaseAudiobookManager.getCurrentPosition(audiobookId, callback);
    }

    public void updateCurrentPositionOffline(String audiobookId, int timestampSeconds) {
        offlineAudiobookManager.updateCurrentPosition(audiobookId, timestampSeconds);
    }

    public int getCurrentPositionOffline(String audiobookId) {
        return offlineAudiobookManager.getCurrentPosition(audiobookId);
    }

    /**
     * Check if the current device was the last device that was used to listen to this audiobook
     * @return True if this was the device last used, false otherwise
     */
    public void isDeviceLastUsed(String audiobookId, final ICallback<Boolean> callback) {

        firebaseAudiobookManager.getLastDeviceUsed(audiobookId, new ICallback<String>(){
            @Override
            public void onResult(String lastDeviceUsedId) {
                //Compare the device IDs
                callback.onResult(deviceInformationManager.getDeviceId().equals(lastDeviceUsedId));
            }
        });


    }

    /**
     * Set the current device as the last device that hte specified audiobook was listened on
     * @param audiobookId
     */
    public void updateLastDeviceUsed(String audiobookId) {
        firebaseAudiobookManager.updateLastDeviceUsed(audiobookId);
    }

}

package com.corson.audiobookplayer.api;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public class AudiobookManager {
    OfflineAudiobookManager offlineAudiobookManager;
    AmplifyAudiobookManager amplifyAudiobookManager;
    IDeviceInformationManager deviceInformationManager;

    boolean shouldUseOnline;

    public AudiobookManager(OfflineAudiobookManager offlineAudiobookManager, AmplifyAudiobookManager amplifyAudiobookManager, IDeviceInformationManager deviceInformationManager, boolean shouldUseOnline) {
        this.offlineAudiobookManager = offlineAudiobookManager;
        this.amplifyAudiobookManager = amplifyAudiobookManager;
        this.deviceInformationManager = deviceInformationManager;
        this.shouldUseOnline = shouldUseOnline;
    }

    public void listBooks(ICallback<ArrayList<Audiobook>> callback) {
        if (shouldUseOnline) {
            amplifyAudiobookManager.listBooks(callback);
        } else {
            offlineAudiobookManager.listBooks(callback);
        }
    }

    public void updateCurrentPositionOnline(String audiobookId, int timestampSeconds) {
        amplifyAudiobookManager.updateCurrentPosition(audiobookId, timestampSeconds);
    }

    public void getCurrentPositionOnline(String audiobookId, ICallback<Integer> callback) {
        amplifyAudiobookManager.getCurrentPosition(audiobookId, callback);
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

        amplifyAudiobookManager.getLastDeviceUsed(audiobookId, new ICallback<String>(){
            @Override
            public void onResult(String lastDeviceUsedId) {
                //Compare the device IDs
                callback.onResult(deviceInformationManager.getDeviceId().equals(lastDeviceUsedId));
            }
        });


    }

}

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

    public int getCurrentPositionOnline(String audiobookId) {
        return amplifyAudiobookManager.getCurrentPosition(audiobookId);
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
    public boolean isDeviceLastUsed(String audiobookId) {
        return deviceInformationManager.getDeviceId().equals(amplifyAudiobookManager.getLastDeviceUsed(audiobookId));
    }

}

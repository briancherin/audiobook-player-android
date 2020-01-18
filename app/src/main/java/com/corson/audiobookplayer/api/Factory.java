package com.corson.audiobookplayer.api;

import android.content.Context;

public class Factory {

    private Context context;

    public Factory(Context context) {
        this.context = context;
    }

    //TODO: There shouldn't be duplicates of createDeviceInformationManager. Create (all?) in constructor, and then return the objects in their methods?
    public AudiobookManager createAudiobookManager() {
        return new AudiobookManager(createOfflineAudiobookManager(), createFirebaseAudiobookManager(), createDeviceInformationManager(),true);
    }

    public AudioStore createAudioStore() {
        return new FirebaseAudioStore();
    }

    private OfflineAudiobookManager createOfflineAudiobookManager() {
        return new OfflineAudiobookManager(context);
    }

    /*private AmplifyAudiobookManager createAmplifyAudiobookManager() {
        return new AmplifyAudiobookManager(context, createDeviceInformationManager());
    }
    */

    private FirebaseAudiobookManager createFirebaseAudiobookManager() {
        return new FirebaseAudiobookManager(context, createDeviceInformationManager());
    }

    public IDeviceInformationManager createDeviceInformationManager() {
        return new DeviceInformationManager(context);
    }
}

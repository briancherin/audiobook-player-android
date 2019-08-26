package com.corson.audiobookplayer.api;

import android.content.Context;

public class Factory {

    private Context context;

    public Factory() {

    }

    public Factory(Context context) {
        this.context = context;
    }

    public AudioManager createAudioManager() throws NoContextProvidedException{
        if (context != null) {
            return new AmplifyAudioManager(context);
        } else {
            throw new NoContextProvidedException();
        }
    }

    public AudioStore createAudioStore() {
        return new AmplifyAudioStore();
    }

    public class NoContextProvidedException extends Exception {
        public NoContextProvidedException () {
            super("Must provide application context in constructor.");
        }
    }

}

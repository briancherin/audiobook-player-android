package com.corson.audiobookplayer.api;

import android.content.Context;

public class Factory {

    private Context context;

    public Factory(Context context) {
        this.context = context;
    }

    public AudioManager createAudioManager() {
        return new AmplifyAudioManager(context);
    }

}

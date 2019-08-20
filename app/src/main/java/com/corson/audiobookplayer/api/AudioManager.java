package com.corson.audiobookplayer.api;

import android.content.Context;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public interface AudioManager {

    void listBooks(ICallback<ArrayList<Audiobook>> callback);

    void updateCurrentPosition();

}

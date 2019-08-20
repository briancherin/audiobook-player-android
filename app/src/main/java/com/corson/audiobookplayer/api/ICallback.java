package com.corson.audiobookplayer.api;

import com.corson.audiobookplayer.model.Audiobook;

import java.util.ArrayList;

public interface ICallback<T> {
    void onResult(T t);
}

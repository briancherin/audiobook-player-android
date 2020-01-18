package com.corson.audiobookplayer.api;

public interface AudioStore {

    void getAudioStreamUrl(String bookId, String fileExtension, ICallback<String> callback);
}

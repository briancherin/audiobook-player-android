package com.corson.audiobookplayer.api;

interface OnlineAudiobookManager {
    void getLastDeviceUsed(String audiobookId, ICallback<String> callback);

    void updateLastDeviceUsed(String audiobookId);
}

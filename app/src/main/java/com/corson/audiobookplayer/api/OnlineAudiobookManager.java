package com.corson.audiobookplayer.api;

interface OnlineAudiobookManager {
    String getLastDeviceUsed(String audiobookId);

    void updateLastDeviceUsed(String audiobookId);
}

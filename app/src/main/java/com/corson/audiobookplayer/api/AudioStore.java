package com.corson.audiobookplayer.api;

public interface AudioStore {
    String getAudioStreamUrl(String bookId, String bookFileExtension);
}

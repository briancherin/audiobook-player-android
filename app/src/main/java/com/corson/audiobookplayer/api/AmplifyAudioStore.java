package com.corson.audiobookplayer.api;
/*

public class AmplifyAudioStore implements AudioStore {

    private static final String BUCKET_NAME = "audiobook-player-files-audiobkenv";

    private final int HOURS_UNTIL_STREAM_EXPIRES = 24;

    S3Helper s3Helper;

    public AmplifyAudioStore() {
        s3Helper = new S3Helper();
    }

    @Override
    public String getAudioStreamUrl(String bookId, String bookFileExtension) {
        String fullFileKey = s3Helper.getFullFileKey(bookId, bookFileExtension);
        return s3Helper.getPresignedUrl(BUCKET_NAME, fullFileKey, HOURS_UNTIL_STREAM_EXPIRES);
    }

}
*/

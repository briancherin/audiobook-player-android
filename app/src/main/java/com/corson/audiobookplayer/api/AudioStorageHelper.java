package com.corson.audiobookplayer.api;

import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.ArrayList;
import java.util.List;

public class AudioStorageHelper {


    private static final String BUCKET_NAME = "audiobook-player-files-audiobkenv";


    S3Helper s3Helper;

    public AudioStorageHelper() {
        s3Helper = new S3Helper();
    }

    public ArrayList<String> fetchBookKeys() {
        return s3Helper.listPrivateObjectKeys(BUCKET_NAME);
    }

    public String getPresignedUrl(String bookKey, int hoursUntilExpires) {
        return s3Helper.getPresignedUrl(BUCKET_NAME, bookKey, hoursUntilExpires);
    }
}

package com.corson.audiobookplayer.api;
/*

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
*/

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class  S3Helper {
    /*private AmazonS3Client s3;

    public S3Helper() {
        s3 = new AmazonS3Client(AuthHelper.getAWSCredentials(), Region.getRegion(Regions.US_EAST_1));
    }

    private List<S3ObjectSummary> listObjects(String bucketName, String prefix) {
        ObjectListing objectListing = s3.listObjects(
                new ListObjectsRequest(bucketName, prefix, null, null, null)
                        .withEncodingType(Constants.URL_ENCODING));

        return objectListing.getObjectSummaries();
    }


    private List<S3ObjectSummary> listPrivateObjects(String bucketName) {
        String prefix = getPrivateUserFolder() + "/";
        return listObjects(bucketName, prefix);
    }

    public ArrayList<String> listPrivateObjectKeys(String bucketName) {
        List<S3ObjectSummary> objects = listPrivateObjects(bucketName);
        ArrayList<String> keys = new ArrayList<>();

        for (S3ObjectSummary object : objects) {
            String keyWithoutPrefix = removePrefixFromKey(object.getKey());
            keys.add(keyWithoutPrefix);
        }

        return keys;
    }

    public String getPresignedUrl(String bucketName, String key, int hoursUntilExpires) {
        String url = "";

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
        request.setMethod(HttpMethod.GET);
        request.setExpiration(DateTimeUtils.addHoursToDate(new Date(), hoursUntilExpires));

        URL presignedUrl = s3.generatePresignedUrl(request);

        return presignedUrl.toString();
    }


    private String getPrivateUserFolder() {
        return "private/" + AuthHelper.getCurrentUserId();
    }

    //Will return the full key for the file. Eg: file located at bucketName/fullFileKey
    public String getFullFileKey(String fileKey, String fileExtension) {
        return getPrivateUserFolder() + "/" + fileKey + "." + fileExtension;
    }


    private String removePrefixFromKey(String keyWithPrefix) {
        String prefix = getPrivateUserFolder() + "/";
        return keyWithPrefix.split(prefix)[1];
    }*/

}

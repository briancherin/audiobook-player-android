package com.corson.audiobookplayer.api;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;

public class AuthHelper {

    //private static final String IDENTITY_POOL_ID = "us-east-1:bd82812f-546d-47bc-bd69-f7b81eef91c8";


    public static String getCurrentUserId() {
        return AWSMobileClient.getInstance().getIdentityId();
    }

    public static AWSCredentials getAWSCredentials() {
        try {
            return AWSMobileClient.getInstance().getAWSCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

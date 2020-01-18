package com.corson.audiobookplayer.api;


import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {

    public static String getCurrentUserId() {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        return currUser != null ? currUser.getUid() : null;
    }


}

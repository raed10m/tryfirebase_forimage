package com.assadh.tryfirebase_forimage;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class utils<Public> {
    public static FirebaseAuth mAuth;
    public static FirebaseUser user;
    public static  String uriImginStorage="https://imgv3.fotor.com/images/side/Free-photo-editor-for-PC-mobile.jpg";
    public static DocumentReference DocRef;
    public  static FirebaseFirestore FireStor;
    public static boolean isConnected(Activity a){
        ConnectivityManager connectivityManager=(ConnectivityManager)a.getSystemService(Context.CONNECTIVITY_SERVICE);
        return false;
    }

}

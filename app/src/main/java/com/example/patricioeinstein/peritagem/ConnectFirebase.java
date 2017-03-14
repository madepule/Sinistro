package com.example.patricioeinstein.peritagem;


import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Patricio  Einstein on 3/11/2017.
 */

//esta classe serve para conectar o A.Studio com o FireBase
public class ConnectFirebase {

    private static Firebase firebase;
    private static DatabaseReference firebaseRef;

    public static String PREF = "com.example.patricioeinstein.peritagem.PREF";
    public static Firebase getFirebase() {
        if (firebase == null) {
            firebase = new Firebase("https://peritagem-d8f8d.firebaseio.com/");
        }
        return (firebase);
    }
    public static DatabaseReference getDatabaseReference(){
        if( firebaseRef == null ){
            firebaseRef = FirebaseDatabase.getInstance().getReference();
        }
        return( firebaseRef );
    }
    static public void saveSP(Context context, String key, String value ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    static public String getSP(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return( token );
    }

}

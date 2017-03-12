package com.example.patricioeinstein.peritagem;

import android.app.Application;

//import com.firebase.client.Firebase;

/**
 * Created by Patricio  Einstein on 3/11/2017.
 */

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
       // Firebase.setAndroidContext(this);
    }
}

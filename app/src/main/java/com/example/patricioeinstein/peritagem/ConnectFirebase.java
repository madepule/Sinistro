package com.example.patricioeinstein.peritagem;


import com.firebase.client.Firebase;

/**
 * Created by Patricio  Einstein on 3/11/2017.
 */


public class ConnectFirebase {

    private static Firebase firebase;
    public static Firebase getFirebase(){
       if( firebase == null ){
          firebase = new Firebase("https://peritagem-d8f8d.firebaseio.com/");
        }
       return( firebase );
   }

}

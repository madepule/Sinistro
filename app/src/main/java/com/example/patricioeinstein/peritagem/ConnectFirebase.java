package com.example.patricioeinstein.peritagem;


import com.firebase.client.Firebase;

/**
 * Created by Patricio  Einstein on 3/11/2017.
 */

//esta classe serve para conectar o A.Studio com o FireBase
public class ConnectFirebase {

    private static Firebase firebase;

    public static Firebase getFirebase() {
        if (firebase == null) {
            firebase = new Firebase("https://peritagem-d8f8d.firebaseio.com/");
        }
        return (firebase);
    }

}

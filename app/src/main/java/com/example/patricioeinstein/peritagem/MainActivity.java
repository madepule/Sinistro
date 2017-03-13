package com.example.patricioeinstein.peritagem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {
    //instanciar a classe connectFirebas
    private Firebase firebase;
    private EditText txtnome;
    private Teste t1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //teste
        firebase = ConnectFirebase.getFirebase();

        txtnome = (EditText) findViewById(R.id.nome);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public View submeter(View view) {

        t1 = new Teste();
        t1.setNome(txtnome.getText().toString());

       // firebase.child("Testes").child("t1").setValue(t1);
        return view;
    }

}

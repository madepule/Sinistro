package com.example.patricioeinstein.peritagem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;


public class MainActivity extends AppCompatActivity {
    private Firebase firebase;
    private EditText txtnome;
    private Teste t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //teste
       firebase = ConnectFirebase.getFirebase();

        //txtnome = (EditText) findViewById(R.id.nome);

    }

    public View submeter(View view)
    {

        t1 = new Teste();
        t1.setNome(txtnome.getText().toString());

        //firebase.child("Testes").child("t1").setValue(t1);
        return view;
    }

}

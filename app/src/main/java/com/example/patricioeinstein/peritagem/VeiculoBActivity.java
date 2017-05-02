package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class VeiculoBActivity extends AppCompatActivity{
    //instanciar a classe connectFirebas
    private Firebase firebase;
    private FirebaseAuth mAuth;
    private Sinistro t1;
    private int count = 0;
    private ProgressBar progressBar;
    private Button btnsubmeter;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    private  Utilizador ut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculob);
        Firebase.setAndroidContext(this);
        //mAuth.signOut();
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");
        //Sinistro
        btnsubmeter = (Button) findViewById(R.id.btngravarveiculoB);
        Date dataActual = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("dd/MMMM/yyyy hh:mm", new Locale("pt", "PT"));
        String dataformatada = spf.format(dataActual);
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);


    }

}

package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class DetalhesActivity extends AppCompatActivity  {
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
    public static Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");


        btnsubmeter = (Button) findViewById(R.id.btngravardetalhes);
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);

        btnsubmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetalhesActivity.this, "Gravado com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public View callCanvas(View view) {
        Intent intent = new Intent(DetalhesActivity.this, CanvasActivity.class);
        startActivity(intent);
        return  view;
    }


}

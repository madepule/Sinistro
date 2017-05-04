package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class NovoActivity extends AppCompatActivity  {
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
    private String coordenadas;
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
        setContentView(R.layout.activiy_nova_participacao);
        Firebase.setAndroidContext(this);
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");
        //Sinistro
        firebase = ConnectFirebase.getFirebase();
        mAuth = FirebaseAuth.getInstance(); //estamos a buscar a instancia da Autenticacao do Firebase

        btnsubmeter = (Button) findViewById(R.id.btnEnviarPart);
       // btnsubmeter.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);

        btnsubmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NovoActivity.this, "Enviado com Sucesso", Toast.LENGTH_LONG).show();
            }
        });

        }

    public View callDetalhes(View view) {
        Intent intent = new Intent(NovoActivity.this, DetalhesActivity.class);
        startActivity(intent);
        return  view;
    }
    public View callVeiculoB(View view) {
        Intent intent = new Intent(NovoActivity.this, VeiculoBActivity.class);
        startActivity(intent);
        return  view;
    }

    public View callCanvas(View view) {
        Intent intent = new Intent(NovoActivity.this, CanvasActivity.class);
        startActivity(intent);
        return  view;
    }

    public View callFotos(View view) {
        //MADEPULE IMPLEMENTE O METODO QUE NEM FEITO AI EM CIMA NO CALLCANVAS.......
        //
        Intent intent = new Intent(NovoActivity.this, FotosActivity.class);
        startActivity(intent);

        return  view;
    }

    public View submeter(View view) {

        t1 = new Sinistro();

        progressBar.setVisibility(View.VISIBLE);
        // firebase.child("Testes").child("t1").setValue(t1);
        //salvar os dados no firebase
        ConnectFirebase.getFirebase().child("Sinistros").child(generateRegCode()).setValue(t1, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    // Toast.makeText( this, "Falhou: "+firebaseError.getMessage(), Toast.LENGTH_LONG ).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar = (ProgressBar) findViewById(R.id.snackbarp);
                    Snackbar.make(progressBar, "Falhou: " + firebaseError.getMessage(), Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();
                } else {
                    //Toast.makeText( this, "Atualização realizada com sucesso.", Toast.LENGTH_SHORT ).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar = (ProgressBar) findViewById(R.id.snackbarp);
                    Snackbar.make(progressBar,
                            "Dados Submetidos Com Sucesso.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED)
                            .show();

                    //Qunado ele submeter com sucesso vai desautenticar
                    //mAuth.signOut(); //estamos a desautenticar
                    NovoActivity.this.finish();
                    //agora vamos testart se funciona
                }
            }
        });

        return view;
    }

    public String generateRegCode()
    {
        String code="";
        Date dataActual = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("ddMMyyyyhhmm");
        String codeData = spf.format(dataActual);
        code=ut.getId()+codeData;
        return code;
    }

}

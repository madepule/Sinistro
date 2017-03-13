package com.example.patricioeinstein.peritagem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class MainActivity extends AppCompatActivity implements Firebase.CompletionListener  {
    //instanciar a classe connectFirebas
    private Firebase firebase;
    private Teste t1;
    private EditText txtnome;
    private EditText txtdata;
    private EditText txthora;
    private EditText txtlocal;
    private int count =0;
    private ProgressBar progressBar;
    private Button  btnsubmeter;

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
        txtdata = (EditText) findViewById(R.id.dataa);
        txthora = (EditText) findViewById(R.id.hora);
        txtlocal = (EditText) findViewById(R.id.local);
        txtlocal.setEnabled(false);
        btnsubmeter = (Button) findViewById(R.id.btnsubmeter);
        Date dataActual = new Date();
        SimpleDateFormat spf=new SimpleDateFormat("dd/MMMM/yyyy hh:mm" , new Locale("pt", "PT"));
        String dataformatada = spf.format(dataActual);
        txtdata.setText(dataformatada);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get the last know location from your location manager.
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // permission has been granted, continue as usual
            // now get the lat/lon from the location and do something with it.
            boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println("ENABLED: "+statusOfGPS);
            if (statusOfGPS == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Para obter a sua localização actual é necessário habilitar o GPS, deseja habilitar?")
                        .setCancelable(false)
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                txtlocal.setText("preencha o campo");
                                btnsubmeter.setEnabled(false);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                txtlocal.setText("Latitude: "+location.getLatitude() +", Longitude: "+location.getLongitude());

            }
            }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public View submeter(View view) {

        t1 = new Teste();
        t1.setNome(txtnome.getText().toString());
        t1.setDataa(txtdata.getText().toString());
        t1.setLocal(txtlocal.getText().toString());
        count+=1;
       // firebase.child("Testes").child("t1").setValue(t1);
        //salvar os dados no firebase
        ConnectFirebase.getFirebase().child("Testes").child("t"+count).setValue(t1);
        return view;
    }

    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {


        if( firebaseError != null ){
           // Toast.makeText( this, "Falhou: "+firebaseError.getMessage(), Toast.LENGTH_LONG ).show();
            progressBar = (ProgressBar) findViewById(R.id.snackbarp);
            Snackbar.make(progressBar, "Falhou: "+firebaseError.getMessage(), Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }
        else{
            //Toast.makeText( this, "Atualização realizada com sucesso.", Toast.LENGTH_SHORT ).show();
            progressBar = (ProgressBar) findViewById(R.id.snackbarp);
            Snackbar.make(progressBar, "Atualização realizada com sucesso.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        }
    }
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}

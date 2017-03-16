package com.example.patricioeinstein.peritagem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class RegistroActivity extends AppCompatActivity implements Firebase.CompletionListener, LocationListener {
    //instanciar a classe connectFirebas
    private Firebase firebase;
    private FirebaseAuth mAuth;
    private Sinistro t1;
    private EditText txtnome;
    private EditText txtdata;
    private EditText txthora;
    private EditText txtlocal;
    private EditText txtApolice;
    private EditText txtMatricula;
    private EditText txtLocalSinistro;
    private EditText txtDanos;
    private EditText txtNometerceiro;
    private EditText txtMatriculaterceiro;
    private EditText txtDanosterceiro;
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
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
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
        setContentView(R.layout.activity_registro);
        Firebase.setAndroidContext(this);
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");
        //Sinistro
        firebase = ConnectFirebase.getFirebase();
        mAuth = FirebaseAuth.getInstance(); //estamos a buscar a instancia da Autenticacao do Firebase
        txtnome = (EditText) findViewById(R.id.nome);
        txtdata = (EditText) findViewById(R.id.dataa);
        txthora = (EditText) findViewById(R.id.hora);
        txtlocal = (EditText) findViewById(R.id.local);
        txtApolice = (EditText) findViewById(R.id.Apolice);
        txtMatricula = (EditText) findViewById(R.id.matricula);
        txtLocalSinistro = (EditText) findViewById(R.id.localsinistro);
        txtDanos = (EditText) findViewById(R.id.danos);
        txtNometerceiro  = (EditText) findViewById(R.id.nometerceiro);
        txtMatriculaterceiro = (EditText) findViewById(R.id.matriculaterceiro);
        txtDanosterceiro = (EditText) findViewById(R.id.danosterceiro);
        btnsubmeter = (Button) findViewById(R.id.btnsubmeter);

        Date dataActual = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("dd/MMMM/yyyy hh:mm", new Locale("pt", "PT"));
        String dataformatada = spf.format(dataActual);
        txtdata.setText(dataformatada);
        txtnome.setText(ut.getName());
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get the last know location from your location manager.
        if (ActivityCompat.checkSelfPermission(RegistroActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegistroActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(RegistroActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // permission has been granted, continue as usual
            // now get the lat/lon from the location and do something with it.
            boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println("ENABLED: " + statusOfGPS);
            if (statusOfGPS == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
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
                loc= getLocation();
                if(loc != null)
                {
                    txtlocal.setText(latitude + ":" + longitude);
                }
            }
        }

    }

    public View submeter(View view) {

        t1 = new Sinistro();
        t1.setNome(txtnome.getText().toString());
        t1.setDataa(txtdata.getText().toString());
        t1.setLocal(txtlocal.getText().toString());
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
                    RegistroActivity.this.finish();
                    //agora vamos testart se funciona
                }
            }
        });

        return view;
    }

    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {


        if (firebaseError != null) {
            // Toast.makeText( this, "Falhou: "+firebaseError.getMessage(), Toast.LENGTH_LONG ).show();
            progressBar = (ProgressBar) findViewById(R.id.snackbarp);
            Snackbar.make(progressBar, "Falhou: " + firebaseError.getMessage(), Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();
        } else {
            //Toast.makeText( this, "Atualização realizada com sucesso.", Toast.LENGTH_SHORT ).show();
            progressBar = (ProgressBar) findViewById(R.id.snackbarp);
            Snackbar.make(progressBar, "Atualização realizada com sucesso.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show();

        }
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) RegistroActivity.this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Snackbar.make(progressBar, "Nenhum Seviço GPS está disponível no momento. Por Favor, tente mais tarde!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .show();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (checkNetwork) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    } catch (SecurityException e) {
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                if (loc == null) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
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
    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

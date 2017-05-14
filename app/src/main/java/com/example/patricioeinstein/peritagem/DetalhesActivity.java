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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class DetalhesActivity extends AppCompatActivity implements  LocationListener {
    //instanciar a classe connectFirebas

    private FirebaseAuth mAuth;
    private Sinistro t1;
    private int count = 0;
    private ProgressBar progressBar;
    private Button btnsubmeter;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    private EditText txtlocal;
    private EditText txtdata;
    //private EditText txtnome;
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

        txtlocal = (EditText) findViewById(R.id.local);
        txtdata = (EditText) findViewById(R.id.dataa);
        //txtnome = (EditText) findViewById(R.id.nome);

        btnsubmeter = (Button) findViewById(R.id.btngravardetalhes);
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);

        btnsubmeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetalhesActivity.this, "Gravado com sucesso", Toast.LENGTH_LONG).show();

                DetalhesActivity.this.finish();
            }
        });

        Date dataActual = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("dd/MMMM/yyyy   hh:mm", new Locale("pt", "PT"));
        String dataformatada = spf.format(dataActual);
        txtdata.setText(dataformatada);
       // txtnome.setText(ut.getName());
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        // get the last know location from your location manager.
        if (ActivityCompat.checkSelfPermission(DetalhesActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetalhesActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Check Permissions Now
            ActivityCompat.requestPermissions(DetalhesActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // permission has been granted, continue as usual
            // now get the lat/lon from the location and do something with it.
            boolean statusOfGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            System.out.println("ENABLED: " + statusOfGPS);
            if (statusOfGPS == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesActivity.this);
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
                    txtlocal.setText("Latitude: "+  latitude  + "    "+" Longitude: " + longitude);
                }
            }
        }

    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) DetalhesActivity.this
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

    public View callCanvas(View view) {
        Intent intent = new Intent(DetalhesActivity.this, CanvasActivity.class);
        startActivity(intent);
        return  view;
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
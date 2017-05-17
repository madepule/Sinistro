package com.example.patricioeinstein.peritagem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private CanvasView customCanvas;
    private final int PICK_IMAGE_REQUEST = 4;
    private ImageView imageView;
    private ScrollView scrollview;
//fotos
    private  int i =0;
    private TextView txtFotos;
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();
    private GridView fotos;
    ArrayList<ImageView> fotosselecionadas=new ArrayList<>();

//fotoss

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
        setContentView(R.layout.participarsinistro);
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");

        txtlocal = (EditText) findViewById(R.id.local);
        txtdata = (EditText) findViewById(R.id.dataa);
        //txtnome = (EditText) findViewById(R.id.nome);

        btnsubmeter = (Button) findViewById(R.id.btngravardetalhes);
        progressBar = (ProgressBar) findViewById(R.id.snackbarp);
        txtFotos = (TextView) findViewById(R.id.txtFotos);
        fotos = (GridView) findViewById(R.id.fotosgrid);
        Fresco.initialize(getApplicationContext());

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

        Button btnImag = (Button) findViewById(R.id.uploadImagem);

        btnImag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT <19){

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Selecciona a Imagem" ), PICK_IMAGE_REQUEST);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Selecciona a Imagem" ), PICK_IMAGE_REQUEST);

                }

            }
        });

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

    }

    public View callSelectFotos(View view) {
        // start multiple photos selector
        Locale locale = new Locale("en", "US");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

        Intent intent = new Intent(DetalhesActivity.this, ImagesSelectorActivity.class);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction((Intent.ACTION_GET_CONTENT));
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        //intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);
        return  view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Foram Selecionadas %d fotos:", mResults.size())).append("\n");
                for(String result : mResults) {
                    i = i+1;
                    //ImageView imageView = new ImageView(this);

                    ImageView image = new ImageView(this);
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(500,400));
                    image.setAdjustViewBounds(true);
                    //image.setMaxHeight(500);
                   // image.setMaxWidth(500);

                    //sb.append(result).append("\n");

                    image.setImageURI(Uri.fromFile(new File(result)));
                    fotosselecionadas.add(image);
                    // linearLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.TOP);
                    // linearLayout.addView(image);
                    //customCanvas.addBitmap(image);
                }
                txtFotos.setText(sb.toString());
                MyAdapter myAdapter=new MyAdapter(this,R.layout.grid_view_items,fotosselecionadas);
                fotos.setAdapter(myAdapter);
            }
        }

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void limparTela(View v) {
        customCanvas.clearCanvas();
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

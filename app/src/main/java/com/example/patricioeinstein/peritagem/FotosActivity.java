package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.util.ArrayList;



//Implementar a interface Firebase.CompletionListener para o firebase devolver ou informar sobre o sucesso ou falha num evento na base de dados
public class FotosActivity extends AppCompatActivity {
    //instanciar a classe connectFirebas
    private int count = 0;
    private ProgressBar progressBar;
    private Button btnsubmeter;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double latitude;
    double longitude;
    private CanvasView customCanvas;
    private  int i =0;
    private TextView txtFotos;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    private  Utilizador ut;
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fotos2);
        Intent intent = getIntent();
        ut= (Utilizador) intent.getSerializableExtra("utilizador");
        //Sinistro
        txtFotos = (TextView) findViewById(R.id.txtFotos);
        Fresco.initialize(getApplicationContext());

        Button gravarft = (Button) findViewById(R.id.btnGravarFotos);
        gravarft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FotosActivity.this, "Gravado com sucesso!", Toast.LENGTH_LONG).show();

                FotosActivity.this.finish();
            }
        });
    }

    public View callSelectFotos(View view) {
        // start multiple photos selector
        //Locale locale = new Locale("en", "US");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        //conf.locale = locale;
        res.updateConfiguration(conf, dm);

        Intent intent = new Intent(FotosActivity.this, ImagesSelectorActivity.class);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction((Intent.ACTION_GET_CONTENT));
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 100);
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
        // get selected images from selector
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Imagelinear);

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
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(550,400));
                    image.setMaxHeight(10);
                    image.setMaxWidth(10);

                    //sb.append(result).append("\n");

                    image.setImageURI(Uri.fromFile(new File(result)));
                    linearLayout.addView(image);
                    //customCanvas.addBitmap(image);
                }
                txtFotos.setText(sb.toString());
            }
        }
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

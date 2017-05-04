package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Hawkingg on 25/04/2017.
 */

public class CanvasActivity extends AppCompatActivity implements Firebase.CompletionListener {

    private CanvasView customCanvas;
    private final int PICK_IMAGE_REQUEST = 4;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croquis_main);

        Button btnImag = (Button) findViewById(R.id.uploadImagem);

        Button btngravar = (Button) findViewById(R.id.gravar);
        btngravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CanvasActivity.this, "Gravado com sucesso", Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    public void limparTela(View v) {
        customCanvas.clearCanvas();
    }


    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

    }
}

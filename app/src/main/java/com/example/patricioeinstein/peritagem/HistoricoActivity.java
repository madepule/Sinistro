package com.example.patricioeinstein.peritagem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Hawkingg on 15/03/2017.
 */

public class HistoricoActivity extends AppCompatActivity {
    private GoogleApiClient client;
    private ArrayList<Sinistro> sinistros;
    private ProgressDialog progressDialog;
    private Utilizador ut;
    private ListView lista;
    protected ProgressBar progressBar;
    private boolean notificado = false;
    private boolean firstTime = true;
    private Snackbar snackbar;
    private Sinistro sinitro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        lista = (ListView) findViewById(R.id.lstHistorico);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando Registro de Sinistros, Por favor aguarde...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        ut = (Utilizador) intent.getSerializableExtra("utilizador");
        sinistros = new ArrayList<>();
        ConnectFirebase.getDatabaseReference().child("Sinistros").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are" + snapshot.getChildrenCount() + " Historicos");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(postSnapshot.getValue(Sinistro.class) != null){
                        sinitro = postSnapshot.getValue(Sinistro.class);
                        if(postSnapshot.getKey().contains(ut.getId())) {
                            sinistros.add(sinitro);
                        }
                    }
                }
                if(sinistros.isEmpty()) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoricoActivity.this);
                    builder.setMessage("Sem Registro de Sinistros")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    HistoricoActivity.this.finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    lista = (ListView) findViewById(R.id.lstHistorico);
                    if (lista != null) {

                        lista.setAdapter(new CustomArrayAdapterH(HistoricoActivity.this, android.R.layout.simple_list_item_1, sinistros));
                        progressDialog.dismiss();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("A busca por dados na nuvem falhou: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if( keyCode== KeyEvent.KEYCODE_BACK)
        {
            progressDialog.cancel();
            this.finish();
        }

        return super.onKeyDown(keyCode, event);

    }
}

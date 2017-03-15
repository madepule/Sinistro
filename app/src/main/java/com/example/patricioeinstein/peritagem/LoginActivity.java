package com.example.patricioeinstein.peritagem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import java.util.Arrays;

public class LoginActivity extends CommonActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Utilizador utilizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = getFirebaseAuthResultHandler();
        initViews();
        initUser();
    }
//teste
    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( mAuthListener != null ){
            mAuth.removeAuthStateListener( mAuthListener );
        }
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler(){
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if( userFirebase == null ){
                    return;
                }

                if( utilizador.getId() == null
                        && isNameOk(utilizador, userFirebase ) ){

                    utilizador.setId( userFirebase.getUid() );
                    utilizador.setNameIfNull( userFirebase.getDisplayName() );
                    utilizador.setEmailIfNull( userFirebase.getEmail() );
                    utilizador.saveDB();
                }

                callMainActivity();
            }
        };
        return( callback );
    }

    private boolean isNameOk(Utilizador utilizador, FirebaseUser firebaseUser ){
        return(
                utilizador.getName() != null
                        || firebaseUser.getDisplayName() != null
        );
    }

    protected void initViews(){
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    protected void initUser(){
        utilizador = new Utilizador();
        utilizador.setEmail( email.getText().toString() );
        utilizador.setPassword( password.getText().toString() );
    }

    public void callSignUp(View view){
        Intent intent = new Intent( this, SignUpActivity.class );
        startActivity(intent);
    }

    public void callReset(View view){
        Intent intent = new Intent( this, ResetActivity.class );
        startActivity(intent);
    }

    public void sendLoginData( View view ){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginData()");
        openProgressBar();
        initUser();
        verifyLogin();
    }

    public void sendLoginFacebookData( View view ){
        FirebaseCrash.log("LoginActivity:clickListener:button:sendLoginFacebookData()");
        LoginManager
                .getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList("public_profile", "user_friends", "email")
                );
    }

    private void callMainActivity(){
        Intent intent = new Intent( this, MainActivity.class );
        startActivity(intent);
        finish();
    }

    private void verifyLogged(){
        if( mAuth.getCurrentUser() != null ){
            callMainActivity();
        }
        else{
            mAuth.addAuthStateListener( mAuthListener );
        }
    }

    private void verifyLogin(){

        FirebaseCrash.log("LoginActivity:verifyLogin()");
        utilizador.saveProviderSP( LoginActivity.this, "" );
        if(utilizador.getEmail().equals(null)|| utilizador.getEmail().equals("") ||utilizador.getPassword().equals(null) || utilizador.getPassword().equals(""))
        {
            showSnackbar("Por Favor, Introduze as Credenciais");
            progressBar.setVisibility(View.INVISIBLE);
        }
        else
        {

            mAuth.signInWithEmailAndPassword(
                    utilizador.getEmail(),
                    utilizador.getPassword()
            )
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if( !task.isSuccessful() ){
                                showSnackbar("Login falhou");
                                progressBar.setVisibility(View.INVISIBLE);
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report( e );
                }
            });
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        FirebaseCrash.report( new Exception( connectionResult.getErrorCode()+": "+connectionResult.getErrorMessage() ) );
        showSnackbar( connectionResult.getErrorMessage() );
        progressBar.setVisibility(View.INVISIBLE);
    }
}
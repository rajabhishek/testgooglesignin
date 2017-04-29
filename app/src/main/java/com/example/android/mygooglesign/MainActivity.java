package com.example.android.mygooglesign;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    //Signin button
    private  SignInButton signInButton;
    //Signout button
    private Button signOutButton;
    //Signin Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    //TextViews
    private TextView textViewName;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing Views
        textViewName = (TextView)findViewById(R.id.textViewName);
        textViewEmail = (TextView)findViewById(R.id.textViewEmail);
        //Initializing google sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //Initializing signInButton
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signOutButton=(Button)(findViewById(R.id.sign_out_button));
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        //Initializing Google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /*fragmentActivity*/,this /*onConnectionFailed*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        //setting onClickListener to signInbutton
        signInButton.setOnClickListener(this);
        //setting onClickListener to signOutbutton
        signOutButton.setOnClickListener(this);


    }
    //this function will option signin intent
    private  void signIn() {
        //creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //starting Intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    public void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if signIn
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    //calling a new function to handle signIn
            handleSignInResult(result);

        }
    }
    //after signing we are calling this function
    public void handleSignInResult(GoogleSignInResult result){
        //if the login succeed
        if(result.isSuccess()){
            //getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            //displaying name and email
            textViewName.setText(acct.getDisplayName());
            textViewEmail.setText(acct.getEmail());
        }
        else{
            //if result fails
            Toast.makeText(this,"Login Failed",Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //calling signIn
            signIn();
        } else if (v == signOutButton) {
            //calling signout
            signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}



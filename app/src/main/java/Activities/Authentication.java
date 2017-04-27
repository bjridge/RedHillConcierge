package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Date;

import Application.MyApplication;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.User;

public class Authentication extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //view objects
    Button googleButton;
    TextView dateOutput;

    //Google Authentication resources
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient googleTools;

    //Firebase Authentication Resources
    private FirebaseAuth firebaseAuthenticationStatus;
    private FirebaseAuth.AuthStateListener firebaseAuthenticationStatusListener;

    //save profilePicture from google account, pass to Firebase account
    private String googleProfilePictureAddress;

    private User user;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__authentication);
        initializeView();
        initializeAuthenticationTools();
    }
    private void initializeView(){
        initializeViewObjects();
        setInitialValues();
    }
    private void initializeViewObjects(){
        dateOutput = (TextView) findViewById(R.id.dateOutput);
        googleButton = (Button) findViewById(R.id.googleButton);
        googleButton.setOnClickListener(this);
    }
    private void setInitialValues(){
        String dateString = getDateAsString();
        dateOutput.setText(dateString);
    }
    private String getDateAsString(){
        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy", d.getTime());
        return s.toString();
    }

    private void initializeAuthenticationTools(){
        initializeGoogleResources();
        initializeFirebaseResources();
    }
    private void initializeGoogleResources(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleTools = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initializeFirebaseResources(){
        firebaseAuthenticationStatus = FirebaseAuth.getInstance();
        firebaseAuthenticationStatusListener = buildFirebaseAuthenticationStatusListener();
    }


    /*-------------------- called after authentication changes and result is successful ------------- */



//---------------------------------------begin actions-----------------------------------//
//  1) "Sign In With Google" button is clicked
    @Override
    public void onClick(View v) {
        tryGoogleAndFirebaseAuthentication();
    }
//  2) start the google authentication flow
    private void tryGoogleAndFirebaseAuthentication() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleTools);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//  3) the google authentication flow will end and give a result (below)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
//  4) if google authentication succeeds, then authenticate with Firebase
                firebaseAuthWithGoogle(account);
            }else{
                //failed to authenticate with google
                Toast.makeText(Authentication.this, "Google Authentication failed.", Toast.LENGTH_LONG).show();            }
        }
    }

//  4) get Firebase access/permission using the Google credentials
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        googleProfilePictureAddress = acct.getPhotoUrl().toString();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuthenticationStatus.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//  5) if it worked, the authentication status (firebaseAuthenticationStatus) will change, triggering the listener
                        }else{
                            Toast.makeText(Authentication.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//  6) when authentication status changes - either logging in or out - this is triggered
    private FirebaseAuth.AuthStateListener buildFirebaseAuthenticationStatusListener(){
        FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //if user is logged in
                if (user != null) {
//  7) go to the app
                    goToApp(user);
                } else {

                    Toast.makeText(Authentication.this, "Signed Out",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        return listener;
    }







    //STEP FIVE IN AUTHENTICATION - once user logs in, decide where to go



    private void goToApp(FirebaseUser user){
        Context context = getApplicationContext();
        User appUser = new User();
        appUser.setKey(user.getUid());
        appUser.setName(user.getDisplayName());
        appUser.setImage(user.getPhotoUrl().toString());
        Intent i = new Intent(context, BasicUserView.class);
        i.putExtra("user", appUser);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    //adds and removes the authStateListener
    @Override
    public void onStart() {
        super.onStart();
        firebaseAuthenticationStatus.addAuthStateListener(firebaseAuthenticationStatusListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (firebaseAuthenticationStatusListener != null) {
            firebaseAuthenticationStatus.removeAuthStateListener(firebaseAuthenticationStatusListener);
        }
    }

}
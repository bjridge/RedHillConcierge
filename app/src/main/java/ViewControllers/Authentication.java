package ViewControllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.User;

public class Authentication extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //view objects
    TextView firebaseTitle;
    TextView firebaseOutput;
    TextView loginStatus;
    com.google.android.gms.common.SignInButton googleButton;
    Button signOutButton;

    //Google Authentication resources
    private static final String TAG = "Google";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        setupViewObjects();

        testFirebase();

        configureGoogleAuthentication();

        configureFirebaseAuthentication();
        Log.v(TAG, "app initialized properly");

    }

    private void configureGoogleAuthentication() {
        //sets up what information is needed
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //allows us to use the API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //allows us to see if user is already logged in
    }


    //adds and removes the authStateListener
        //when this activity starts, it will need to know when someone changes login status.  when its not, it no longer needs this listener
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }







    //STEP ONE IN AUTHENTICATION
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //STEP TWO IN AUTHENTICATION
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                Log.v(TAG, "failed to authenticate with google - double check SHA fingerprint");
            }
        }
    }

    //STEP THREE IN AUTHENTICATION
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Authentication.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //STEP FOUR IN AUTHENTICATION - initialized in onCreate()
    private void configureFirebaseAuthentication(){
        mAuth = FirebaseAuth.getInstance();
        //takes action depending on whether or not a user is logged in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loginToApp(user);
                    // TODO: 3/21/2017 user flow
                } else {
                    // User is signed out
                }
            }
        };
    }


    //STEP FIVE IN AUTHENTICATION - once user logs in, decide where to go
    private void loginToApp(FirebaseUser user){
        loginStatus.setText("logged in");
        userExists(user);
    }
    private boolean userExists(final FirebaseUser user){
        DatabaseController dc = new DatabaseController();
        Task<DatabaseObject> getUserTask = dc.getObject("user", user.getUid());

        getUserTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            // TODO: 3/21/2017 add a loading mechanism while it figures out if the user is new or not!
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                DatabaseObject result = task.getResult();
                if (result.key() == "empty object"){
                    //new user
                    firebaseOutput.setText("new user");
                    loginNewUser(user);
                }else{
                    //existing user
                    firebaseOutput.setText("old user");
                }
            }
        });
        //testFirebase();
        return false;
    }


    private void loginNewUser(FirebaseUser user){
        //transition to new screen with new names
        Context context = getApplicationContext();
        Intent i = new Intent(context, NewUser.class);
        i.putExtra("id", user.getUid());
        i.putExtra("name", user.getDisplayName());
        i.putExtra("pictureURL", user.getPhotoUrl());
        i.putExtra("email", user.getEmail());
        startActivity(i);
    }















    private void signOut(){
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                logOut();
            }
        });
    }

    private void logOut(){
        loginStatus.setText("Logged Out");
        testFirebase();
    }


    private void testFirebase(){
        firebaseOutput.setText("failed");
        DatabaseController dc = new DatabaseController();
        Task<ArrayList<DatabaseObject>> getAllUsersTask = dc.getAll("user");
        getAllUsersTask.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> objects = task.getResult();
                for (DatabaseObject object: objects){
                    User user = (User) object;
                    firebaseOutput.setText("success");
                }
            }
        });

    }

    private void setupViewObjects(){
        firebaseTitle = (TextView) findViewById(R.id.firebaseTitle);
        firebaseOutput = (TextView) findViewById(R.id.firebaseOutput);
        loginStatus = (TextView) findViewById(R.id.loginStatus);
        googleButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.googleButton);
        signOutButton = (Button) findViewById(R.id.signOutButton);

        googleButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);

        firebaseTitle.setText("Firebase Status:");
        loginStatus.setText("Not Logged In");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.googleButton){
            Log.v(TAG, "signing in");
            signIn();
        }else if(v.getId() == R.id.signOutButton){
            signOut();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
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

import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;

public class Authentication extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //view objects
    Button googleButton;
    TextView dateOutput;

    //Google Authentication resources
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    //Firebase Authentication Resources
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //save profilePicture from google account, pass to Firebase account
    private String googleProfilePictureAddress;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__authentication);

        setupViewObjects();
        setupGoogleResources();
        setupFirebaseResources();
    }
    private void setupViewObjects(){
        initializeViewObjects();
        setInitialValues();
    }
    private void initializeViewObjects(){
        googleButton = (Button) findViewById(R.id.googleButton);
        dateOutput = (TextView) findViewById(R.id.dateOutput);
    }
    private void setInitialValues(){
        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy", d.getTime());
        dateOutput.setText(s);
        googleButton.setOnClickListener(this);
    }

    private void setupGoogleResources(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setupFirebaseResources(){
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
    //adds and removes the authStateListener
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

    /*-------------------- called after authentication changes and result is successful ------------- */
    private void loginToApp(FirebaseUser user){
        userExists(user);
    }
    private boolean userExists(final FirebaseUser user){
        DatabaseController taskBuilder = new DatabaseController();
        Task<DatabaseObject> getUserTask = taskBuilder.getObject("user", user.getUid());

        getUserTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            // TODO: 3/21/2017 add a loading mechanism while it figures out if the user is new or not!
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                DatabaseObject result = task.getResult();
                if (result.key() == "empty object"){
                    //new user
                    loginNewUser(user);
                }else{
                    //existing user
                    loginExistingUser(user);
                }
            }
        });
        return false;
    }








//---------------------------------------begin actions-----------------------------------//

    @Override
    public void onClick(View v) {
        tryToAuthenticateWithGoogleAndThenFirebase();
    }
    private void tryToAuthenticateWithGoogleAndThenFirebase() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                //failed to authenticate with google
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        googleProfilePictureAddress = acct.getPhotoUrl().toString();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Firebase authentication succeeded
                        }else{
                            Toast.makeText(Authentication.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    //STEP FIVE IN AUTHENTICATION - once user logs in, decide where to go



    private void loginNewUser(FirebaseUser user){
        //transition to new screen with new names
        Context context = getApplicationContext();
        Intent i = new Intent(context, NewUser.class);
        i.putExtra("id", user.getUid());
        i.putExtra("name", user.getDisplayName());
        i.putExtra("pictureURL", user.getPhotoUrl().toString());
        Log.v("IMPORTNANT",user.getPhotoUrl().toString());
        startActivity(i);
    }

    private void loginExistingUser(FirebaseUser user){
        Context context = getApplicationContext();
        Intent i = new Intent(context, BasicUserView.class);
        i.putExtra("id", user.getUid());
        startActivity(i);
    }















    private void signOut(){
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //logOut();
            }
        });
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}

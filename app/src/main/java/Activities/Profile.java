package Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import Application.MyApplication;
import DataControllers.Change;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.User;

public class Profile extends AppCompatActivity  {

    private ImageButton exitButton;
    private FloatingActionButton nextButton;
    private ImageView profilePicture;
    private ImageView profilePictureBackground;
    private EditText pictureInput;
    private Spinner userTypeSpinner;
    private Button logOffButton;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneOneInput;
    private EditText phoneTwoInput;
    private EditText streetAddressInput;
    private EditText cityInput;
    private Spinner stateSpinner;
    private EditText zipCodeInput;
    private TextView pictureLabel;

    private MyApplication application;
    private User user;
    private Contact contact;
    private boolean userIsNew;

    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    private int returnCode = 0;
    //0: viewed/edited your own profile
    //1: viewed someone else's profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);

    }
}

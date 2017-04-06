package Activities.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import DataControllers.Contact;
import DataControllers.User;

public class EventsTab extends MyFragment {


    private WebView web_view;
    private User user;
    private Contact contact;

    CoordinatorLayout layout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int[] drawables;
    MyFragment[] fragments;

    //toolbar resources
    private Toolbar toolbar;
    private TextView toolbarTitle;

    ImageButton administratorButton;
    ImageButton profileButton;
    ImageButton cameraButton;

    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__jobs);
        initializeViewResources();
        setSpreedSheet("<iframe width=\"100%\" height=\"80%\" src=\"https://calendar.google.com/calendar/embed?src=latjq0kuep4232j3r0qd53a6bs%40group.calendar.google.com&ctz=America/New_York\"></iframe>");
    }

    private void initializeViewResources() {
        web_view = (WebView) findViewById(R.id.workers_cal);
    }


    public EventsTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab__events, container, false);
    }
















}

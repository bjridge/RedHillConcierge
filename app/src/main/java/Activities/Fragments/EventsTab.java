package Activities.Fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import DataControllers.Contact;
import DataControllers.User;

public class EventsTab extends Fragment{

    private WebView web_view;

    public EventsTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab__events, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initializeViewResources();
        setupCalendarWebView(savedInstanceState);
    }
    private void initializeViewResources() {
        web_view = (WebView) getView().findViewById(R.id.workers_cal);
    }
    private void setupCalendarWebView(Bundle savedInstanceState) {
        setupSpreadSheet("<iframe width=\"100%\" height=\"80%\" src=\"https://calendar.google.com/calendar/embed?src=latjq0kuep4232j3r0qd53a6bs%40group.calendar.google.com&ctz=America/New_York\"></iframe>");
    }


    private void setupSpreadSheet(String calendar){
        web_view.setWebViewClient(new WebViewClient());
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.loadData(calendar, "text/html", "utf-8");
    }













}

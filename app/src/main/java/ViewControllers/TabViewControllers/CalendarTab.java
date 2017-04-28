package ViewControllers.TabViewControllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

public class CalendarTab extends Fragment{

    private WebView web_view;

    public CalendarTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab__calendar, container, false);
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

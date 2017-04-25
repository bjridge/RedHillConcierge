package Activities.Fragments;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.Calendar;
import java.util.Date;

import Application.MyApplication;
import DataControllers.Contact;
import DataControllers.User;

public class HomeTab extends Fragment {

    MyApplication application;

    TextView nameOutput;
    TextView typeOutput;

    public HomeTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab__home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication)  getActivity().getApplication();
        nameOutput = (TextView) getView().findViewById(R.id.home_name);
        typeOutput = (TextView) getView().findViewById(R.id.home_user_type);
        Log.v("important", "about to get application");
        nameOutput.setText(application.getUser().getFirstName());
        typeOutput.setText(application.getUser().getType());

    }



















}

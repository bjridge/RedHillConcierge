package Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.User;

public class HomeTab extends MyFragment {


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

        Bundle bundle = getArguments();
        if (bundle != null){

        }

    }



















}

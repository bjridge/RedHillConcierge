package Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.List;

import Activities.Profile2;
import Application.MyApplication;
import DataControllers.User;

public class UsersTab extends Fragment implements AdapterView.OnItemClickListener{

    MyApplication application;
    List<String> names;
    ListView displayedUsers;

    public UsersTab(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab__users, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
        displayedUsers = (ListView) getView().findViewById(R.id.users_list);
        names = application.getAllUserNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
        displayedUsers.setAdapter(adapter);
        displayedUsers.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = names.get(position);
        String userID = application.getUserByName(name);
        User user = application.getUser(userID);
        Intent i = new Intent(getContext(), Profile2.class);
        i.putExtra("user", user);
        startActivity(i);
    }
}

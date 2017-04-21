package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Activities.Fragments.HorseListAdapter;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;

public class HorseList extends AppCompatActivity {
    List<Horse> horses;
    List<Permission> permissions;
    User user;

    ListView displayedHorses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__horse_list);

        initializeViewObjects();
        //what we'll need from the intent:
            //list of horses to display
            //user who is logged in
            //list of permissions
        getResourcesFromIntent();
        Log.v("IMPORTANT", "HORSE COUNT: " + horses.size());
        showHorses();
        addOnClickListener();
    }
    private void initializeViewObjects(){
        displayedHorses = (ListView) findViewById(R.id.horse_list);
    }
    private void getResourcesFromIntent(){
        Intent i = getIntent();
        horses = (List<Horse>) i.getSerializableExtra("horses");
        Collections.sort(horses, new Comparator<Horse>() {
            @Override
            public int compare(Horse o1, Horse o2) {
                return o1.compareTo(o2);
            }
        });
        user = (User) i.getSerializableExtra("user");
        permissions = (List<Permission>) i.getSerializableExtra("permissions");
    }
    private void showHorses(){
        //display them in the horse view!
        HorseListAdapter adapter = new HorseListAdapter(getApplicationContext(), horses);
        displayedHorses.setAdapter(adapter);
    }
    private void addOnClickListener(){
        displayedHorses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the selected horse
                Horse horse = horses.get(position);

                //go to next view
                Intent i = new Intent(getApplicationContext(), EditableHorse.class);
                i.putExtra("horse", horse);
                i.putExtra("permissions", (Serializable) permissions);
                i.putExtra("user", user);
            }
        });
    }
}



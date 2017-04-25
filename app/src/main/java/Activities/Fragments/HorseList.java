package Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.List;

import Activities.HorseTabs;
import DataControllers.Horse;
import ListAdapters.HorseListAdapter;

//vertical list of all horse objects
    //on item selected, opens up HorseTabs activity
public class HorseList extends Fragment{


    List<Horse> horses;

    public HorseList(){}
    public HorseList withHorses(List<Horse> horses){
        this.horses = horses;
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab__horse_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ListView horseList =(ListView) getView().findViewById(R.id.horse_list);
        horseList.setAdapter(new HorseListAdapter(getContext(), horses));
        horseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Horse selection = horses.get(position);
                ArrayList<Horse> sentHorses = (ArrayList<Horse>) horses;
                Intent intent = new Intent(getContext(), HorseTabs.class);
                intent.putExtra("selectedHorse", selection);
                intent.putExtra("horses", sentHorses);
                startActivity(intent);
            }
        });
    }
}

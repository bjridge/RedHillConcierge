package Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.List;

import Activities.HorseList;
import DataControllers.Horse;

public class HorseListTab extends MyFragment implements View.OnClickListener{

    //takes in a list of horses, and which horse to select
        //if no horse is selected/passed, select the first one
    //displays currently selected horse
        //once current horse is loaded, if it is your horse, make it uneditable

    List<Horse> horses;

    public HorseListTab(){}
    public HorseListTab withHorses(List<Horse> horses){
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
                Intent intent = new Intent(getContext(), HorseList.class);
                intent.putExtra("selectedHores", selection);
                intent.putExtra("horses", sentHorses);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        //get the horse that is selected

    }
}

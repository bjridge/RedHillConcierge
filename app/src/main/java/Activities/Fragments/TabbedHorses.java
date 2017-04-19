package Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.List;

import DataControllers.Horse;

public class TabbedHorses extends MyFragment {

    List<Horse> horses;
    Horse selectedHorse;

    public TabbedHorses(){

    }
    public TabbedHorses withHorses(List<Horse> inputHorses){
        this.horses = inputHorses;
        return this;
    }
    public TabbedHorses select(Horse inputHorse){
        this.selectedHorse = inputHorse;
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab__search, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (horses == null){
            horses = application.getAllHorses();
        }
        int selectedHorseIndex = 0;
        if (selectedHorse != null){
            selectedHorseIndex = horses.indexOf(selectedHorse);
            //// TODO: 4/18/2017 select this horse
        }


    }
}

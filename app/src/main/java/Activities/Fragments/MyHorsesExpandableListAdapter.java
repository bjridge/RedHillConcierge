package Activities.Fragments;

import android.content.Intent;
import android.util.Log;
import android.widget.BaseExpandableListAdapter;

/**
 * Created by BradleyRidge on 3/30/2017.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Horse;

public class MyHorsesExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<List<Horse>> horseLists;
    List<String> horseListTitles;

    public MyHorsesExpandableListAdapter(Context context, List<Horse> myHorses, List<Horse> sharedHorses, List<Horse> horses) {
        this.context = context;
        log("adapter: initializing");
        this.horseLists = new ArrayList<List<Horse>>();
        horseLists.add(myHorses);
        horseLists.add(sharedHorses);
        horseLists.add(horses);
        log("adapter: horse lists added");
        this.horseListTitles = new ArrayList<String>();
        horseListTitles.add("My Horses");
        horseListTitles.add("Horses Shared With Me");
        horseListTitles.add("All Horses");
        log("adapter: horse list titles added");
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.horseLists.get(listPosition).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Horse horse = (Horse) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_horse_list_item, null);
        }
        TextView horseName = (TextView) convertView.findViewById(R.id.horse_name);
        TextView horseStall = (TextView) convertView.findViewById(R.id.horse_stall);
        TextView horseDescription = (TextView) convertView.findViewById(R.id.horse_description);
        TextView horseSex = (TextView) convertView.findViewById(R.id.horse_sex);
        TextView dateChangesMade = (TextView) convertView.findViewById(R.id.changes_made);

        horseName.setText(horse.getName());
        horseStall.setText("Stall: " + horse.getStallNumber());
        horseDescription.setText(horse.getColor() + " " + horse.getBreed());
        horseSex.setText(horse.getSex());

        Calendar date = Calendar.getInstance();
        SimpleDateFormat dfN = new SimpleDateFormat("yyMdd");
        String todayNum = dfN.format(date.getTime());

        SimpleDateFormat df = new SimpleDateFormat("MMM-dd");
        String today = df.format(date.getTime());

        if((Integer.parseInt(horse.getChangesMade())+2)> Integer.parseInt(todayNum)){
            dateChangesMade.setText("Changed: " + today);
            dateChangesMade.setVisibility(View.VISIBLE);
        }else{
            dateChangesMade.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.horseLists.get(listPosition).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.horseListTitles.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.horseLists.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_horse_list_header, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.horse_list_name);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
    private void log(String message){
        Log.v("IMPORTANT", message);
    }
}

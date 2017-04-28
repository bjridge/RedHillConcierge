package ListAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.Objects.Horse;

public class ExpandableHorseListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<List<Horse>> horseLists;
    List<String> horseListTitles;

    public ExpandableHorseListAdapter(Context context, List<Horse> sharedHorses, List<Horse> horses) {
        this.context = context;
        this.horseLists = new ArrayList<List<Horse>>();
        horseLists.add(sharedHorses);
        horseLists.add(horses);
        this.horseListTitles = new ArrayList<String>();
        horseListTitles.add("My Horses");
        horseListTitles.add("All Horses");
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
            convertView = layoutInflater.inflate(R.layout.custom__list_item__horse_list, null);
        }
        TextView horseName = (TextView) convertView.findViewById(R.id.horse_name);
        TextView horseStall = (TextView) convertView.findViewById(R.id.horse_stall);
        TextView horseDescription = (TextView) convertView.findViewById(R.id.horse_description);
        TextView dateChangesMade = (TextView) convertView.findViewById(R.id.changes_made);

        horseName.setText(horse.getName());
        horseStall.setText("Stall: " + horse.getStallNumber());
        horseDescription.setText(horse.getColor() + " " + horse.getBreed());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat readableFormat = new SimpleDateFormat("MMM-dd");
        String lastDateString = horse.getLastRevisionDate();
        Date lastRevisionDate;
        try{
            lastRevisionDate = format.parse(lastDateString);
            Calendar c = Calendar.getInstance();
            Long difference = c.getTimeInMillis() - lastRevisionDate.getTime();
            Long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
            if (days <= 2){
                    dateChangesMade.setText("Changed: " + readableFormat.format(lastRevisionDate));
                    dateChangesMade.setVisibility(View.VISIBLE);
                    Log.v("HORSECHECK", "HORSE " + horse.getName() + " was revised recently");
            }else{
                dateChangesMade.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.v("HORSECHECK", "GOT STUCK ON " + horse.getName());
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
            convertView = layoutInflater.inflate(R.layout.custom__expandable_list_header, null);
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

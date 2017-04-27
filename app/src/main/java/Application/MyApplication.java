package Application;

import android.app.Application;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;


public class MyApplication extends Application {

    List<User> allUsers;
    List<Horse> allHorses;
    User user;
    List<String> breedOptions;
    List<String> colorOptions;
    List<String> grainOptions;
    List<String> sexOptions;
    List<String> hayOptions;

    public String getCurrentDateString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentDate = Calendar.getInstance();
        String currentDateString = format.format(currentDate.getTime());
        return currentDateString;
    }

    public List<String> getLimitedBreedOptions(){
        List<String> breedOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if (!breedOptions.contains(horse.getBreed())){
                breedOptions.add(horse.getBreed());
            }
        }
        return breedOptions;
    }
    public List<String> getLimitedColorOptions(){
        List<String> colorOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!colorOptions.contains(horse.getColor())){
                colorOptions.add(horse.getColor());
            }
        }
        return colorOptions;
    }
    public List<String> getLimitedSexOptions(){
        List<String> sexOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!sexOptions.contains(horse.getSex())){
                sexOptions.add(horse.getSex());
            }
        }
        return sexOptions;
    }
    public List<String> getLimitedHorseNameOptions(){
        List<String> nameOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!nameOptions.contains(horse.getName())){
                nameOptions.add(horse.getName());
            }
        }
        return nameOptions;
    }
    public List<String> getLimitedStalls(){
        List<String> options = new ArrayList<String>();
        for(Horse horse: allHorses){
            if (!options.contains(horse.getStallNumber())){
                options.add(horse.getStallNumber());
            }
        }
        return options;
    }
    public List<String> getLimitedOwners(){
        List<String> options = new ArrayList<String>();
        for (Horse horse: allHorses){
            User owner = getUser(horse.getOwner());

            if (!options.contains(owner.getName())){
                options.add(owner.getName());
            }
        }
        return options;
    }

    public String getUserByName(String input){
        for (User user: allUsers){
            String name = user.getName();
            if (name.matches(input)){
                return user.key();
            }
        }
        return allUsers.get(0).key();
    }

    public List<String> getAllUserNames(){
        List<String> names = new ArrayList<String>();
        for(User user: allUsers){
            if (user.getName() != null){
                names.add(user.getName());
            }
        }
        return names;
    }
    public void updateHorse(Horse inputHorse) {
        int index = 0;
        for (Horse eachHorse : allHorses) {
            if (eachHorse.key().matches(inputHorse.key())) {
                allHorses.set(index, inputHorse);
                return;
            }
            index++;
        }
        allHorses.add(inputHorse);
    }


    public User getUser(String key){
        for (User eachUser: allUsers){
            if (eachUser.key().matches(key)){
                return eachUser;
            }
        }
        return user;
    }

    public void updateUser(User updateUser){
        int index = 0;
        for (User eachUser: allUsers){
            if (user.key().matches(updateUser.key())){
                allUsers.set(index, updateUser);
                return;
            }
            index++;
        }
    }
    public void addHorse(Horse horse){
        allHorses.add(horse);
    }



    public boolean loadingIsComplete(){
        if (breedOptions == null){
            return false;
        }
        if (allUsers == null){
            return false;
        }
        if (allHorses == null){
            return false;
        }
        if (user == null){
            return false;
        }
        return true;
    }
    public void setResources(List<List<String>> resources){
        setBreedOptions(resources.get(0));
        setColorOptions(resources.get(1));
        setGrainOptions(resources.get(2));
        setSexOptions(resources.get(4));
        setHayOptions(resources.get(3));
    }
    public List<String> getHayOptions() {
        return hayOptions;
    }
    public void setHayOptions(List<String> hayOptions) {
        this.hayOptions = hayOptions;
    }
    public List<String> getBreedOptions() {
        return breedOptions;
    }
    public void setBreedOptions(List<String> breedOptions) {
        Collections.sort(breedOptions);
        this.breedOptions = breedOptions;

    }
    public List<String> getColorOptions() {
        return colorOptions;
    }
    public void setColorOptions(List<String> colorOptions) {
        Collections.sort(colorOptions);
        this.colorOptions = colorOptions;
    }
    public List<String> getGrainOptions() {
        return grainOptions;
    }
    public void setGrainOptions(List<String> grainOptions) {
        Collections.sort(grainOptions);
        this.grainOptions = grainOptions;
    }
    public List<String> getSexOptions() {
        return sexOptions;
    }
    public void setSexOptions(List<String> sexOptions) {
        Collections.sort(sexOptions);
        this.sexOptions = sexOptions;
    }
    public List<User> getAllUsers() {
        return allUsers;
    }
    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }
    public List<Horse> getAllHorses() {
        return allHorses;
    }
    public void setAllHorses(List<Horse> allHorses) {
        this.allHorses = allHorses;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

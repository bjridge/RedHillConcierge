package Application;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;


public class MyApplication extends Application {

    List<User> allUsers;
    List<Horse> allHorses;
    List<Contact> allContacts;
    List<Permission> allPermissions;
    User user;
    Contact contact;
    boolean resourcesLoaded;
    List<String> breedOptions;
    List<String> colorOptions;
    List<String> grainOptions;
    List<String> hayOptions;
    List<String> sexOptions;
    List<String> nameOptions;

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
    public List<String> getLimitedGrainOptions(){
        List<String> grainOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!grainOptions.contains(horse.getGrainType())){
                grainOptions.add(horse.getGrainType());
            }
        }
        return grainOptions;
    }
    public List<String> getLimitedHayOptions(){
        List<String> hayOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!hayOptions.contains(horse.getHay())){
                hayOptions.add(horse.getHay());
            }
        }
        return hayOptions;
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
    public List<String> getLimitedNameOptions(){
        List<String> nameOptions = new ArrayList<String>();
        for (Horse horse: allHorses){
            if(!nameOptions.contains(horse.getName())){
                nameOptions.add(horse.getName());
            }
        }
        return nameOptions;
    }
    //do this with names
    //name them "getLimited____Options"

    public void updateUser(User inputUser){
        if (user.key().matches(inputUser.key())){
            user = inputUser;
        }
        int index = 0;
        for(User eachUser: allUsers){
            if (eachUser.key().matches(inputUser.key())){
                allUsers.set(index, inputUser);
                return;
            }
            index++;
        }
        allUsers.add(user);
    }
    public void updateContact(Contact inputContact){
        if (contact.key().matches(inputContact.key())){
            contact = inputContact;
        }
        int index = 0;
        for (Contact eachContact: allContacts){
            if (eachContact.key().matches(inputContact.key())){
                allContacts.set(index, inputContact);
                return ;
            }
            index++;
        }
        allContacts.add(inputContact);
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


    public Contact getContact(String key){
        for (Contact eachContact: allContacts){
            if (eachContact.key().matches(key)){
                return eachContact;
            }
        }
        return null;
    }
    public User getUser(String key){
        Log.v("IMPORTANT", "trying to find user");
        for (User eachUser: allUsers){
            Log.v("IMPORTANT", "going thru users");
            if (eachUser.key().matches(key)){
                Log.v("IMPORTANT", "found a user");
                return eachUser;
            }
        }
        Log.v("IMPORTANT", "NEVER found a user");

        return null;
    }


    public void MyApplication(){
        resourcesLoaded = false;
    }
    public void loadingCompleted(){
        resourcesLoaded = true;
    }
    public boolean loadingIsComplete(){
        if (allUsers == null){
            return false;
        }
        if (breedOptions == null){
            return false;
        }
        if (allHorses == null){

            return false;
        }
        if (allContacts == null){

            return false;
        }
        if (allPermissions == null){

            return false;
        }
        if (user == null){

            return false;
        }
        if (contact == null){
            return false;
        }
        return true;
    }

    public void setResources(List<List<String>> resources){
        setBreedOptions(resources.get(0));
        Log.v("IMPORTANT", "breed options set");
        setColorOptions(resources.get(1));
        setGrainOptions(resources.get(2));
        setSexOptions(resources.get(4));
        setHayOptions(resources.get(3));
    }

    public List<String> getNameOptions() {
        return nameOptions;
    }

    public void setNameOptions(List<String> nameOptions) {
        this.nameOptions = nameOptions;
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
        this.breedOptions = breedOptions;

    }

    public List<String> getColorOptions() {
        return colorOptions;
    }

    public void setColorOptions(List<String> colorOptions) {
        this.colorOptions = colorOptions;
    }

    public List<String> getGrainOptions() {
        return grainOptions;
    }

    public void setGrainOptions(List<String> grainOptions) {
        this.grainOptions = grainOptions;
    }

    public List<String> getSexOptions() {
        return sexOptions;
    }

    public void setSexOptions(List<String> sexOptions) {
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

    public List<Contact> getAllContacts() {
        return allContacts;
    }

    public void setAllContacts(List<Contact> allContacts) {
        this.allContacts = allContacts;
    }

    public List<Permission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(List<Permission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}

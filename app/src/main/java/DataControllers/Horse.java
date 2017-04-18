package DataControllers;

import android.support.annotation.NonNull;

public class Horse extends DatabaseObject{

    //added comment to test pushing
    private String breed = " ";
    private String pictureURL=" ";
    private String color = " ";
    private String grainAmount = " ";
    private String grainType = " ";
    private String hay = "true";
    private String medicationInstructions = " ";
    private String name = " ";
    private String notes = " ";
    private String sex = " ";
    private String stallInstructions = " ";
    private String stallNumber = "0";
    private String owner = "0";
    private String inOutDay = "1111111";
    private String inOutNight = "1111111";


    public int compareTo(@NonNull Horse another) {
        if (stallNumber == another.stallNumber) {
            return name.compareToIgnoreCase(another.name);
        } else if (Integer.parseInt(stallNumber) < Integer.parseInt(another.stallNumber)) {
            return -1;
        } else {
            return 1;
        }
    }


    public String getHay() {
        return hay;
    }

    public String getInOutDay() {
        return inOutDay;
    }

    public void setInOutDay(String inOutDay) {
        this.inOutDay = inOutDay;
    }

    public String getInOutNight() {
        return inOutNight;
    }

    public void setInOutNight(String inOutNight) {
        this.inOutNight = inOutNight;
    }

    public Horse(){}

    public Horse(String id){
        this.setKey(id);
    }

    public String getPicture(){return pictureURL;}

    public void setPicture(String pictureURL){ this.pictureURL = pictureURL; }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGrainAmount() {
        return grainAmount;
    }

    public void setGrainAmount(String grainAmount) {
        this.grainAmount = grainAmount;
    }

    public String getGrainType() {
        return grainType;
    }

    public void setGrainType(String grainType) {
        this.grainType = grainType;
    }


    public void setHay(String hay) {
        this.hay = hay;
    }

    public String getMedicationInstructions() {
        return medicationInstructions;
    }

    public void setMedicationInstructions(String medicationInstructions) {
        this.medicationInstructions = medicationInstructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStallInstructions() {
        return stallInstructions;
    }

    public void setStallInstructions(String stallInstructions) {
        this.stallInstructions = stallInstructions;
    }

    public String getStallNumber() {
        return stallNumber;
    }

    public void setStallNumber(String stallNumber) {
        this.stallNumber = stallNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

package Model.Objects;

import android.support.annotation.NonNull;
import android.util.Log;

public class Horse extends FirebaseObject {

    //added comment to test pushing
    private String breed = " ";
    private String picture=" ";
    private String color = " ";
    private String grainAmount = " ";
    private String grainType = " ";
    private String hay = "true";
    private String medicationInstructions = " ";
    private String name = " ";
    private String notes = " ";
    private String sex = " ";
    private String stallNumber = "0";
    private String owner = "0";
    private String inOutDay = "1111111";
    private String inOutNight = "1111111";
    private String permittedRiders=" ";
    private String dateChangesMade="0";
    private String waterAmount = " ";
    private String hayAmount = " ";

    @Override
    public boolean equals(Object o) {
        Log.v("TRYING", " ");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Horse horse = (Horse) o;

        if (breed != null ? !breed.equals(horse.breed) : horse.breed != null) {
            Log.v("NOT EQUAL", "INOUTDAY1");
            return false;
        }
        if (picture != null ? !picture.equals(horse.picture) : horse.picture != null){
            Log.v("NOT EQUAL", "INOUTDAY2");
            return false;
        }
        if (color != null ? !color.equals(horse.color) : horse.color != null){
            Log.v("NOT EQUAL", "INOUTDAY3");
            return false;
        }
        if (grainAmount != null ? !grainAmount.equals(horse.grainAmount) : horse.grainAmount != null){
            Log.v("NOT EQUAL", "INOUTDAY4");
            return false;
        }
        if (grainType != null ? !grainType.equals(horse.grainType) : horse.grainType != null){
            Log.v("NOT EQUAL", "INOUTDAY5");
            return false;
        }
        if (hay != null ? !hay.equals(horse.hay) : horse.hay != null){
            Log.v("NOT EQUAL", "INOUTDAY6");
            return false;
        }
        if (medicationInstructions != null ? !medicationInstructions.equals(horse.medicationInstructions) : horse.medicationInstructions != null){
            Log.v("NOT EQUAL", "INOUTDAY7");
            return false;
        }
        if (name != null ? !name.equals(horse.name) : horse.name != null) {
            Log.v("NOT EQUAL", "INOUTDAY8");
            return false;
        }
        if (notes != null ? !notes.equals(horse.notes) : horse.notes != null){
            Log.v("NOT EQUAL", "INOUTDAY9");
            return false;
        }
        if (sex != null ? !sex.equals(horse.sex) : horse.sex != null){
            Log.v("NOT EQUAL", "INOUTDAY10");
            return false;
        }
        if (stallNumber != null ? !stallNumber.equals(horse.stallNumber) : horse.stallNumber != null){
            Log.v("NOT EQUAL", "INOUTDAY");
            return false;
        }
        if (owner != null ? !owner.equals(horse.owner) : horse.owner != null) {
            Log.v("NOT EQUAL", "INOUTDAY");
            return false;
        }
        if (inOutDay != null ? !inOutDay.equals(horse.inOutDay) : horse.inOutDay != null){
            Log.v("NOT EQUAL", "INOUTDAY");
            return false;
        }
        if (inOutNight != null ? !inOutNight.equals(horse.inOutNight) : horse.inOutNight != null){
            Log.v("NOT EQUAL", "INOUTNIGHT");
            return false;
        }
        if (permittedRiders != null ? !permittedRiders.equals(horse.permittedRiders) : horse.permittedRiders != null) {
            return false;
        }
        if (dateChangesMade != null ? !dateChangesMade.equals(horse.dateChangesMade) : horse.dateChangesMade != null){
            return false;
        }
        if (waterAmount != null ? !waterAmount.equals(horse.waterAmount) : horse.waterAmount != null){
            return false;
        }
        return hayAmount != null ? hayAmount.equals(horse.hayAmount) : horse.hayAmount == null;

    }

    @Override
    public int hashCode() {
        int result = breed != null ? breed.hashCode() : 0;
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (grainAmount != null ? grainAmount.hashCode() : 0);
        result = 31 * result + (grainType != null ? grainType.hashCode() : 0);
        result = 31 * result + (hay != null ? hay.hashCode() : 0);
        result = 31 * result + (medicationInstructions != null ? medicationInstructions.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (stallNumber != null ? stallNumber.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (inOutDay != null ? inOutDay.hashCode() : 0);
        result = 31 * result + (inOutNight != null ? inOutNight.hashCode() : 0);
        result = 31 * result + (permittedRiders != null ? permittedRiders.hashCode() : 0);
        result = 31 * result + (dateChangesMade != null ? dateChangesMade.hashCode() : 0);
        result = 31 * result + (waterAmount != null ? waterAmount.hashCode() : 0);
        result = 31 * result + (hayAmount != null ? hayAmount.hashCode() : 0);
        return result;
    }

    public int compareTo(@NonNull Horse another) {
        if (stallNumber == another.stallNumber) {
            return name.compareToIgnoreCase(another.name);
        } else if (Integer.parseInt(stallNumber) < Integer.parseInt(another.stallNumber)) {
            return -1;
        } else {
            return 1;
        }
    }
    public String getWaterAmount(){return waterAmount;}

    public void setWaterAmount(String waterAmount){this.waterAmount = waterAmount;}

    public String getHayAmount(){return hayAmount;}

    public void setHayAmount(String hayAmount){this.hayAmount = hayAmount;}

    public String getLastRevisionDate(){return dateChangesMade;}

    public void setLastRevisionDate(String dateChangesMade){this.dateChangesMade = dateChangesMade;}

    public String getHay() {
        return hay;
    }

    public String getInOutDay() {
        return inOutDay;
    }

    public String getPermittedRiders() {return permittedRiders;}

    public void setPermittedRiders(String permittedRiders) {this.permittedRiders = permittedRiders;}

    public void setInOutDay(String inOutDay) {
        this.inOutDay = inOutDay;
    }

    public String getInOutNight() {
        return inOutNight;
    }

    public void setInOutNight(String inOutNight) {
        this.inOutNight = inOutNight;
    }

    public Horse(){

    }

    public Horse(String id){
        this.setKey(id);
    }

    public String getPicture(){return picture;}

    public void setPicture(String picture){ this.picture = picture; }

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

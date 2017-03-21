package DataControllers;

public class Horse extends DatabaseObject{

    //added comment to test pushing
    private String breed = " ";
    private String color = " ";
    private String grainAmount = " ";
    private String grainType = " ";
    private boolean hay = true;
    private String medicationInstructions = " ";
    private String name = " ";
    private String notes = " ";
    private String sex = " ";
    private String stallInstructions = " ";
    private int stallNumber = 0;
    private int owner = 0;

    public int getOwner(){
        return owner;
    }
    public void setOwner(int owner){
        owner = owner;
    }
    public String getBreed(){
        return breed;
    }
    public void setBreed(String breed){
        this.breed = breed;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
    }
    public String getGrainAmount(){
        return this.grainAmount;
    }
    public void setGrainAmount(String grainAmount){
        this.grainAmount = grainAmount;
    }
    public String getGrainType(){
        return grainType;
    }
    public void setGrainType(String grainType){
        this.grainType = grainType;
    }
    public boolean getHay(){
        return hay;
    }
    public void setHay(boolean hay){
        this.hay = hay;
    }
    public String getMedicationInstructions(){
        return this.medicationInstructions;
    }
    public void setMedicationInstructions(String medicationInstructions){
        this.medicationInstructions = medicationInstructions;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getNotes(){
        return this.notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    public String getSex(){
        return sex;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public String getStallInstructions(){
        return stallInstructions;
    }
    public void setStallInstructions(String stallInstructions){
        this.stallInstructions = stallInstructions;
    }
    public int getStallNumber(){
        return stallNumber;
    }
    public void setStallNumber(int stallNumber){
        this.stallNumber = stallNumber;
    }
}

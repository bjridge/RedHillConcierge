package DataControllers;

public class DatabaseObject {

    int id = 1000;

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return id;
    }

    public String getName(){
        return "database object";
    }
}
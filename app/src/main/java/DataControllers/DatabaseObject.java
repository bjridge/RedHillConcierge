package DataControllers;

public class DatabaseObject {

    int id = 0;

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

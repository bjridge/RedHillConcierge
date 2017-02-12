package DataControllers;

public class User extends DatabaseObject {

    private String firstName;
    private String lastName;
    private String type;
    public int id = 0;

    @Override
    public String getName(){
        return firstName + " " + lastName;
    }

    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
}

package DataControllers;

import java.io.Serializable;

public class DatabaseObject implements Serializable {

    private String key = "empty object";

    public void setKey(String id){
        this.key = id;
    }

    public String key(){
        return key;
    }

}

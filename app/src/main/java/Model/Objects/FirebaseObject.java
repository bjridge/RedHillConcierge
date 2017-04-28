package Model.Objects;

import java.io.Serializable;

public class FirebaseObject implements Serializable {
    private String key = "empty object";
    public void setKey(String id){
        this.key = id;
    }
    public String key(){
        return key;
    }
}

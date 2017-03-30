package DataControllers;

public class Permission extends DatabaseObject {

    String horse;
    String user;

    public Permission(String id){
        super.setKey(id);
    }

    public Permission(){

    }

    public String getHorse() {
        return horse;
    }

    public void setHorse(String horse) {
        this.horse = horse;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

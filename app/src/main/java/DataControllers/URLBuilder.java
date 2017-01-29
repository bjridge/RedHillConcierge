package DataControllers;

public class URLBuilder {

    static String base = "https://red-hill-concierge.firebaseio.com/";

    public static String forAllUsers(){
        return base + "user";
    }


}

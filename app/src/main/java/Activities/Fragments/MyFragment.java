package Activities.Fragments;

import android.support.v4.app.Fragment;

import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.User;


public class MyFragment extends Fragment {

    private User user;
    private Contact contact;

    public MyFragment(){
        user = new User();
        Contact contact = new Contact();
        user.setType("");
        user.setFirstName("");
        user.setLastName("");
        contact.setCity("");
        contact.setName("");
        contact.setPhoto("");
        contact.setState("");
        contact.setStreetAddress("");
        contact.setZip("");
        contact.setKey("");
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }



}

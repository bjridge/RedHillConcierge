package Activities.Fragments;

import android.support.v4.app.Fragment;

import DataControllers.Contact;
import DataControllers.User;


public class MyFragment extends Fragment {

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

    private User user;
    private Contact contact;



}

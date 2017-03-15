package DataControllerTests;

import org.junit.Before;
import org.junit.Test;

import DataControllers.User;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserSetters() throws Exception {
        User u = buildTestUser();

        assertEquals(u.getFirstName(), "brad");
        assertEquals(u.getLastName(), "ridge");
        assertEquals(u.getType(), "employee");
    }
    private User buildTestUser(){
        User u = new User();
        u.setFirstName("brad");
        u.setLastName("ridge");
        u.setType("employee");
        return u;
    }

    @Test
    public void testGetName(){
        User u = buildTestUser();
        //String fullName = u.getName();
        //assertEquals(fullName, "brad ridge");
    }

}
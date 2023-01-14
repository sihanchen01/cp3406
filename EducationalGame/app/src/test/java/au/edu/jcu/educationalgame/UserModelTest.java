package au.edu.jcu.educationalgame;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// Test if UserModel (M in MVC) working properly
public class UserModelTest {
    UserModel user;

    @Before
    public void setup(){
        user = new UserModel(1, "testUser", "123123", 12, 11);
    }

    @Test
    public void getId() {
        assertEquals(user.getId(), 1);
    }

    @Test
    public void getUserName() {
        assertEquals(user.getUserName(), "testUser");
    }

    @Test
    public void getPinNumber() {
        assertEquals(user.getPinNumber(), "123123");
    }

    @Test
    public void getReflexScore() {
        assertEquals(user.getReflexScore(), 12);
    }

    @Test
    public void getMathScore() {
        assertEquals(user.getMathScore(), 11);
    }

    @Test
    public void testToString() {
        assertEquals(user.getId(), 1);
    }
}
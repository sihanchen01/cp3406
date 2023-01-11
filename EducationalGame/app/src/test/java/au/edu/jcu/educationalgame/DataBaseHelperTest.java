package au.edu.jcu.educationalgame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataBaseHelperTest {
    DataBaseHelper dataBaseHelper = new DataBaseHelper(null);

    public void setup() throws Exception {
    }

    @Test
    public void validateNonExistUser() {
        // Tried to log in a non-exist user
        boolean result = dataBaseHelper.validateUser("noSuchUser", "123123");
        assertFalse(result);
    }

    @Test
    public void validateUserWrongPinNumber() {
        // Tried to log in user with wrong pin number
        boolean result = dataBaseHelper.validateUser("test", "123123");
        assertFalse(result);
    }

    @Test
    public void validateUserWrongUsernameCase() {
        // Tried to log in a user with correct pin number, but wrong case for user name
        boolean result = dataBaseHelper.validateUser("TesT", "123111");
        assertFalse(result);
    }

    @Test
    public void validateUserCorrectInput() {
        // Tried to log in a user with correct inputs
        boolean result = dataBaseHelper.validateUser("test", "123111");
        assertTrue(result);
    }
}
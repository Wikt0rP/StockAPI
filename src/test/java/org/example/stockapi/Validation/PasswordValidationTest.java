package org.example.stockapi.Validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class PasswordValidationTest {
    private PasswordValidation passwordValidation;

    @BeforeEach
    void setUp() {
        passwordValidation = new PasswordValidation();
    }


    @Test
    void testValidPasswordAllValid() {
        String password = "123456789@Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertTrue("Password should be valid", result);
    }

    @Test
    void testPasswordTooShort(){
        String password = "123";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 0);
        assertFalse("Password should be invalid", result);
    }

    @Test
    void testPasswordContainsSpecialCharacter() {
        String password = "password12345Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password does not contain special character and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordContainsUsername(){
        String password = "user12345@Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password contains username and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordContainsDigit() {
        String password = "password@Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password does not contain digit and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordContainsUpperCase() {
        String password = "password12345@!";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password does not contain uppercase letter and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordIsNotLongerThan8Chars() {
        String password = "1234@Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password is not longer than 8 characters and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordIsEmpty() {
        String password = "";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password is empty and passwordStrength is 5/5. Should be invalid", result);
    }

    @Test
    void testPasswordContainsUsernameButInUpperCase() {
        String password = "User12345@Aa";
        String user = "user";
        boolean result = passwordValidation.isValidPassword(password, user, 5);
        assertFalse("Password contains username and passwordStrength is 5/5. Should be invalid", result);
    }
}

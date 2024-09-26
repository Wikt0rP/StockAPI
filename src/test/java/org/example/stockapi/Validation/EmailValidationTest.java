package org.example.stockapi.Validation;

import org.example.stockapi.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class EmailValidationTest {
    @InjectMocks
    private EmailValidation emailValidation;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidEmailAllValid() {
        String email = "tefhghstmail@mail.con";
        boolean result = emailValidation.isValidEmail(email);
        assertTrue("Email should be valid", result);
    }

    @Test
    void testMailEmpty(){
        String email = "";
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid - mail empty", result);
    }
    @Test
    void testMailNull(){
        String email = null;
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid - mail is null", result);
    }
    @Test
    void testMailNoAt(){
        String email = "mail.com";
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid - lack of @", result);
    }
    @Test
    void testMailNoDot(){
        String email = "mail@mailcom";
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid - not a mail format, no dot", result);
    }
    @Test
    void testMailBadFormat(){
        String email = "mail@mail.";
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid - not a mail format, ends with dot", result);
    }
    @Test
    void testMailStartWithAt(){
        String email = "@mail.com";
        boolean result = emailValidation.isValidEmail(email);
        assertFalse("Email should be invalid, not a mail format, starts with @", result);
    }

    @Test
    void testMailAlreadyExists(){
        String mail = "test@mail.com";
        when(userRepository.existsByEmail(mail)).thenReturn(true);
        boolean result = emailValidation.isValidEmail(mail);
        assertFalse("Email should be invalid, mail already exist", result);
    }
}

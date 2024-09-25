package org.example.stockapi.Validation;

import org.example.stockapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidation {
    @Autowired
    private UserRepository userRepository;

    /**
     * Checks if email is valid and unique
     * @return true if email is valid and unique
     */
    public boolean isValidEmail(String email){
        boolean isEmail, isUnique;
        if(isEmailEmpty(email)){
            return false;
        }

        isEmail = isEmailValid(email);
        isUnique = !userRepository.existsByEmail(email);

        return isEmail && isUnique;
    }


    private boolean isEmailEmpty(String email) {
        return email == null || email.isEmpty();
    }
    private boolean isEmailValid(String email){
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,8}$";
        return email.matches(emailRegex);
    }
}

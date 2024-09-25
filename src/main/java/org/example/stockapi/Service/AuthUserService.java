package org.example.stockapi.Service;

import org.example.stockapi.Entity.User;
import org.example.stockapi.Repository.UserRepository;
import org.example.stockapi.Request.RegisterRequest;
import org.example.stockapi.Security.Config.PasswordEncoderConfig;
import org.example.stockapi.Validation.EmailValidation;
import org.example.stockapi.Validation.PasswordValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailValidation emailValidation;
    @Autowired
    private PasswordValidation passwordValidation;
    @Autowired
    private PasswordEncoderConfig passwordEncoder;

    /**
     * Validate email and password, if successful register user.
     * @return Response entity with a message
     */
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest){
        if(registerValidations(registerRequest).getStatusCode().is4xxClientError()){
            return registerValidations(registerRequest);
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(),
                passwordEncoder.passwordEncoder().encode(registerRequest.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");


    }




    //Might change password strength here
    private ResponseEntity<String> registerValidations(RegisterRequest registerRequest){
        if(!emailValidation.isValidEmail(registerRequest.getEmail())){
            return ResponseEntity.badRequest().body("Email validation unsuccessful");
        }
        if(!passwordValidation.isValidPassword(registerRequest.getPassword(), registerRequest.getUsername(), 3)){
            return ResponseEntity.badRequest().body("Password validation unsuccessful");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }
        if(registerRequest.getUsername().length() < 3){
            return ResponseEntity.badRequest().body("Username must be at least 3 characters long");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username already exists");
        }
        return ResponseEntity.ok("Validations successful");
    }

}

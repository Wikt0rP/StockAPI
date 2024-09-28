package org.example.stockapi.Service;

import org.example.stockapi.Entity.User;
import org.example.stockapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuthUserService {

    @Autowired
    private UserRepository userRepository;

    public void processOAuthPostLogin(String email, String givenName, String sub){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            User user = new User(givenName, email, sub, true);
            userRepository.save(user);
        }
    }
}

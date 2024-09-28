package org.example.stockapi.Controller;

import org.example.stockapi.Request.AuthRequest;
import org.example.stockapi.Request.RegisterRequest;
import org.example.stockapi.Service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthUserService authUserService;

    @PostMapping("/signin")
    public ResponseEntity<?> customAuth(@RequestBody AuthRequest authRequest){
        return ResponseEntity.status(HttpStatus.OK).body("OK-test");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> userRegisterCustom(@RequestBody RegisterRequest registerRequest){
        return authUserService.registerUser(registerRequest);
    }
}

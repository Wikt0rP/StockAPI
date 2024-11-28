package org.example.stockapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.stockapi.Request.AuthRequest;
import org.example.stockapi.Request.RegisterRequest;
import org.example.stockapi.Service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "Sign in user", description = "Sign in user with given data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User signed in successfully, get token"),
            @ApiResponse(responseCode = "400", description = "Bad credentials")
    })
    @PostMapping("/signin")
    public ResponseEntity<?> customAuth(@RequestBody AuthRequest authRequest){
        return authUserService.signIn(authRequest);
    }

    @Operation(summary = "Sign up user", description = "Sign up user with given data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User signed up successfully"),
            @ApiResponse(responseCode = "400", description = "User could not be signed up")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> userRegisterCustom(@RequestBody RegisterRequest registerRequest){
        return authUserService.registerUser(registerRequest);
    }
}

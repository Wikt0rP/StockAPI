package org.example.stockapi.Service;

import org.example.stockapi.Repository.UserRepository;
import org.example.stockapi.Request.AuthRequest;
import org.example.stockapi.Request.RegisterRequest;
import org.example.stockapi.Response.JwtResponse;
import org.example.stockapi.Security.Config.PasswordEncoderConfig;
import org.example.stockapi.Security.Impl.UserDetailsImpl;
import org.example.stockapi.Security.Jwt.JwtUtils;
import org.example.stockapi.Validation.EmailValidation;
import org.example.stockapi.Validation.PasswordValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class AuthUserServiceTest {

    @InjectMocks
    private AuthUserService authUserService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailValidation emailValidation;
    @Mock
    private PasswordValidation passwordValidation;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordEncoderConfig passwordEncoderConfig;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsImpl userDetails;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);

    }

    @Test
    void successfulSignUpTest(){
        String username = "username";
        String email = "email@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        // Mokowanie metod
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);

        assertTrue("Successful register", response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void failedSignupTestUsernameIsTaken(){
        String username = "username";
        String email = "mail@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue("Register is not successful, username is taken.", response.getStatusCode().is4xxClientError());

    }

    @Test
    void failedSignupTestEmailIsTaken(){
        String username = "username";
        String email = "mail@gmail.com";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue("Register is not successful, username is taken.", response.getStatusCode().is4xxClientError());

    }

    @Test
    void failedSignupTestEmailIsInvalid() {
        String username = "username";
        String email = "email@gm";
        String password = "Password123@";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(true);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue("Register is not successful, email is invalid.", response.getStatusCode().is4xxClientError());
    }

    @Test
    void failedSignupTestPasswordIsInvalid() {
        String username = "username";
        String email = "mail@gmail.com";
        String password = "error";
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(emailValidation.isValidEmail(registerRequest.getEmail())).thenReturn(true);
        when(passwordValidation.isValidPassword(registerRequest.getPassword(), username, 3)).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authUserService.registerUser(registerRequest);
        assertTrue("Register is not successful, password is invalid.", response.getStatusCode().is4xxClientError());
    }

    @Test
    void successfulSignInTest() {
        String username = "username";
        String password = "Password123@";
        AuthRequest authRequest = new AuthRequest(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getEmail()).thenReturn("expected@mail.com");
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwtToken");

        ResponseEntity<?> response = authUserService.signIn(authRequest);

        assertTrue("Respond should be successful", response.getStatusCode().is2xxSuccessful());
        assertEquals("jwtToken", ((JwtResponse) Objects.requireNonNull(response.getBody())).getToken(), "Token should be jwtToken");
    }

}
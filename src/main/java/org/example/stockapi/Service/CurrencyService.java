package org.example.stockapi.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Entity.FollowedCurrency;
import org.example.stockapi.Entity.FollowedStock;
import org.example.stockapi.Entity.User;
import org.example.stockapi.Repository.FollowedCurrencyRepository;
import org.example.stockapi.Repository.UserRepository;
import org.example.stockapi.Request.FollowCurrencyRequest;
import org.example.stockapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.stockapi.Security.Jwt.JwtUtils.getJwtFromRequest;

@Service
public class CurrencyService {
    private String url = "https://api.nbp.pl/api/exchangerates/tables/A/";
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowedCurrencyRepository followedCurrencyRepository;

    public ResponseEntity<?> addCurrencyToFav(FollowCurrencyRequest followCurrencyRequest, HttpServletRequest request){
        String token = getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                if(addCurrencyToUser(followCurrencyRequest, user.get())){
                    return ResponseEntity.ok().body("Currency added!");
                } else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error adding currency to favorites");
                }
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }
    public ResponseEntity<?> getFollowedCurrencies(HttpServletRequest request){
        String token = getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                return ResponseEntity.ok().body(followedCurrencies(user.get()));
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }

    public ResponseEntity<?> getFollowedCurrenciesData(HttpServletRequest request){
        String token = getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                List<Object> response = followedCurrencyApiRequest(followedCurrencies(user.get()));
                return ResponseEntity.ok().body(response);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }

    private List<FollowedCurrency> followedCurrencies(User user){
        return followedCurrencyRepository.findByUserId(user.getId());
    }

    private List<Object> followedCurrencyApiRequest(List<FollowedCurrency> followedCurrencies){
        List<Object> response = new ArrayList<>();

        for(FollowedCurrency followedCurrency: followedCurrencies){
            String url = "https://api.nbp.pl/api/exchangerates/rates/"+ followedCurrency.getCurrenctTable()+"/"+followedCurrency.getSymbol()+"/";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .build();
            try{
                java.net.http.HttpResponse<String> responseString = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(responseString.body());
                response.add(jsonNode);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    private boolean addCurrencyToUser(FollowCurrencyRequest followCurrencyRequest, User user){
        if(followedCurrencyRepository.findByUserIdAndSymbol(user.getId(), followCurrencyRequest.getCurrency()).isPresent()){
            return false;
        }else {
            FollowedCurrency followedCurrency = new FollowedCurrency(followCurrencyRequest.getCurrency(), followCurrencyRequest.getFullName(), user, followCurrencyRequest.getCurrencyTable());
            followedCurrencyRepository.save(followedCurrency);
            return true;
        }
    }

}

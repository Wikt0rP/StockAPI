package org.example.stockapi.Service;

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
                List<FollowedCurrency> followedCurrencies = followedCurrencyRepository.findByUserId(user.get().getId());
//                List<String> stocks = new ArrayList<>();
//                for(FollowedStock followedStock: followedStocks){
//                    stocks.add(followedStock.getSymbol());
//                }
                return ResponseEntity.ok().body(followedCurrencies);
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }

    private boolean addCurrencyToUser(FollowCurrencyRequest followCurrencyRequest, User user){
        if(followedCurrencyRepository.findByUserIdAndSymbol(user.getId(), followCurrencyRequest.getCurrency()).isPresent()){
            return false;
        }else {
            FollowedCurrency followedCurrency = new FollowedCurrency(followCurrencyRequest.getCurrency(), user);
            followedCurrencyRepository.save(followedCurrency);
            return true;
        }
    }

}

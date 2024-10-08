package org.example.stockapi.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Entity.FollowedStock;
import org.example.stockapi.Entity.User;
import org.example.stockapi.Repository.FollowedStockRepository;
import org.example.stockapi.Repository.UserRepository;
import org.example.stockapi.Request.FollowStockRequest;
import org.example.stockapi.Request.TickerRequest;
import org.example.stockapi.Security.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.example.stockapi.Security.Jwt.JwtUtils.getJwtFromRequest;

@Service
public class StockService {

    @Value("${api.stock.secret}")
    private String apiKey;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowedStockRepository followedStockRepository;



    public ResponseEntity<?> addStockToFav(FollowStockRequest followStockRequest, HttpServletRequest request){
        String token = getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                if(addStockToUser(followStockRequest, user.get())){
                    return ResponseEntity.ok().body("Stock added to favorites");
                } else{
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error adding stock to favorites");
                }
            } else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }

    public ResponseEntity<?> getFollowedStocks(HttpServletRequest request){
        String token = getJwtFromRequest(request);

        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                followedStocks(user.get());
                return ResponseEntity.ok().body(followedStocks(user.get()));
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }

        }
    }

    public ResponseEntity<?> getFollowedStocksData(HttpServletRequest request){
        String token = getJwtFromRequest(request);
        if(token == null || !jwtUtils.validateToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }else{
            String username = jwtUtils.extractUsername(token);
            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                List<Object> response = followedStocksApiRequest(followedStocks(user.get()));
                return ResponseEntity.ok().body(response);
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User not found");
            }
        }
    }

    private List<FollowedStock> followedStocks(User user){
        return followedStockRepository.findByUserId(user.getId());
    }
    private List<Object> followedStocksApiRequest(List<FollowedStock> followedStocks){
        List<Object> response = new ArrayList<>();

        for(FollowedStock followedStock: followedStocks){
            String url = "https://api.polygon.io/v3/reference/tickers/"+followedStock.getSymbol()+"?apiKey=" + apiKey;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            try{
                HttpResponse<String> responseApi = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(responseApi.body());
                response.add(node);
            } catch (IOException | InterruptedException e) {
                response.add("Can't add stock: " + followedStock.getSymbol());
            }

        }
        return response;

    }

    private boolean addStockToUser(FollowStockRequest followStockRequest, User user){

        if(followedStockRepository.findByUserIdAndSymbol(user.getId(), followStockRequest.getTicker()).isPresent()){
            return false;
        } else{
            FollowedStock followedStock = new FollowedStock(followStockRequest.getTicker(), followStockRequest.getFullName(), user);
            followedStockRepository.save(followedStock);
            return true;
        }
    }



//    public ResponseEntity<?> getALlStocks(){
//        String url = "https://api.polygon.io/v3/reference/tickers?active=true&limit=1000&apiKey=" + apiKey;
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .build();
//
//        try{
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if(response.statusCode() == 200){
//                return ResponseEntity.ok().header("Content-Type", "application/json")
//                        .body(response.body());
//            } else{
//                return ResponseEntity.status(response.statusCode())
//                        .body("Error: "+ response.body());
//            }
//
//        } catch (IOException | InterruptedException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error: "+ e.getMessage());
//        }
//    }
//
//    public ResponseEntity<?> getDetails(String ticker){
//        String url = "https://api.polygon.io/v3/reference/tickers/"+ticker+"?apiKey=" + apiKey;
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .build();
//
//        try{
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            if(response.statusCode() == 200){
//                return ResponseEntity.ok().header("Content-Type", "application/json")
//                        .body(response.body());
//            } else{
//                return ResponseEntity.status(response.statusCode())
//                        .body("Error: "+ response.body());
//            }
//
//        } catch (IOException | InterruptedException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error: "+ e.getMessage());
//        }
//    }
}


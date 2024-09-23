package org.example.stockapi.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.stockapi.Request.TickerRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Service
public class StockService {

    @Value("${api.stock.secret}")
    private String apiKey;


    public ResponseEntity<?> getALlStocks(){
        String url = "https://api.polygon.io/v3/reference/tickers?active=true&limit=1000&apiKey=" + apiKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return ResponseEntity.ok().header("Content-Type", "application/json")
                        .body(response.body());
            } else{
                return ResponseEntity.status(response.statusCode())
                        .body("Error: "+ response.body());
            }

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: "+ e.getMessage());
        }
    }

    public ResponseEntity<?> getDetails(String ticker){
        String url = "https://api.polygon.io/v3/reference/tickers/"+ticker+"?apiKey=" + apiKey;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200){
                return ResponseEntity.ok().header("Content-Type", "application/json")
                        .body(response.body());
            } else{
                return ResponseEntity.status(response.statusCode())
                        .body("Error: "+ response.body());
            }

        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: "+ e.getMessage());
        }
    }
}


package org.example.stockapi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @GetMapping("/test")
    public ResponseEntity<?> testUsd(){
        String url = "https://api.nbp.pl/api/exchangerates/rates/c/usd/today/";
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

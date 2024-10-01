package org.example.stockapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Request.FollowCurrencyRequest;
import org.example.stockapi.Request.FollowStockRequest;
import org.example.stockapi.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/followCurrency")
    public ResponseEntity<?> addCurrencyToFav(@RequestBody FollowCurrencyRequest followCurrencyRequest, HttpServletRequest request){
        return currencyService.addCurrencyToFav(followCurrencyRequest, request);
    }
    @GetMapping("/getFollowedCurrencies")
    public ResponseEntity<?> getFollowedCurrencies(HttpServletRequest request){
        return currencyService.getFollowedCurrencies(request);
    }

    @GetMapping("/getFollowedCurrenciesData")
    public ResponseEntity<?> getFollowedCurrenciesData(HttpServletRequest request) throws IOException, InterruptedException {
        return currencyService.getFollowedCurrenciesData(request);
    }
}

package org.example.stockapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Request.FollowCurrencyRequest;
import org.example.stockapi.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Operation(summary = "Follow currency", description = "Follow currency with given data, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency followed successfully"),
            @ApiResponse(responseCode = "400", description = "Currency could not be followed")
    })
    @PostMapping("/followCurrency")
    public ResponseEntity<?> addCurrencyToFav(@RequestBody FollowCurrencyRequest followCurrencyRequest, HttpServletRequest request){
        return currencyService.addCurrencyToFav(followCurrencyRequest, request);
    }
    @Operation(summary = "Get followed currencies", description = "Get followed currencies, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currencies fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Currencies could not be fetched")
    })
    @GetMapping("/getFollowedCurrencies")
    public ResponseEntity<?> getFollowedCurrencies(HttpServletRequest request){
        return currencyService.getFollowedCurrencies(request);
    }

    @Operation(summary = "Get followed currencies data", description = "Get followed currencies data, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currencies data fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Currencies data could not be fetched")
    })
    @GetMapping("/getFollowedCurrenciesData")
    public ResponseEntity<?> getFollowedCurrenciesData(HttpServletRequest request){
        return currencyService.getFollowedCurrenciesData(request);
    }
}

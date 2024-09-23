package org.example.stockapi.Controller;

import org.example.stockapi.Request.TickerRequest;
import org.example.stockapi.Service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;
    Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping("/getStocks")
    public ResponseEntity<?> getStocks(){
        return stockService.getALlStocks();
    }

    @GetMapping("/getTicker")
    public ResponseEntity<?> getTicker(@RequestBody TickerRequest tickerRequest){
        logger.info("Ticker: " + tickerRequest.getTicker());
       return stockService.getDetails(tickerRequest.getTicker());
    }
}

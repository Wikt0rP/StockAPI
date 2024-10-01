package org.example.stockapi.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Request.FollowStockRequest;
import org.example.stockapi.Request.TickerRequest;
import org.example.stockapi.Service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    Logger logger = LoggerFactory.getLogger(StockController.class);

//    @GetMapping("/getStocks")
//    public ResponseEntity<?> getStocks(){
//        return stockService.getALlStocks();
//    }
//
//    @GetMapping("/getTicker")
//    public ResponseEntity<?> getTicker(@RequestBody TickerRequest tickerRequest){
//        logger.info("Ticker: " + tickerRequest.getTicker());
//       return stockService.getDetails(tickerRequest.getTicker());
//    }

    @PostMapping("/followStock")
    public ResponseEntity<?> addStockToFav(@RequestBody FollowStockRequest followStockRequset, HttpServletRequest request){
        logger.info("Ticker: " + followStockRequset.getTicker());
        logger.info("Request: " + request);
        return stockService.addStockToFav(followStockRequset, request);
    }
    @GetMapping("/getFollowedStocks")
    public ResponseEntity<?> getFollowedStocks(HttpServletRequest request){
        return stockService.getFollowedStocks(request);
    }

    @GetMapping("/getFollowedStocksData")
    public ResponseEntity<?> getFollowedStocksData(HttpServletRequest request){
        return stockService.getFollowedStocksData(request);
    }
}

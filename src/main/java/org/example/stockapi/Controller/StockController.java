package org.example.stockapi.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.example.stockapi.Request.FollowStockRequest;
import org.example.stockapi.Service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    Logger logger = LoggerFactory.getLogger(StockController.class);

    @Operation(summary = "Follow stock", description = "Follow stock with given data, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock followed successfully"),
            @ApiResponse(responseCode = "400", description = "Stock could not be followed")
    })
    @PostMapping("/followStock")
    public ResponseEntity<?> addStockToFav(@RequestBody FollowStockRequest followStockRequset, HttpServletRequest request){
        logger.info("Ticker: " + followStockRequset.getTicker());
        logger.info("Request: " + request);
        return stockService.addStockToFav(followStockRequset, request);
    }

    @Operation(summary = "Get followed stocks", description = "Get followed stocks, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stocks fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Stocks could not be fetched")
    })
    @GetMapping("/getFollowedStocks")
    public ResponseEntity<?> getFollowedStocks(HttpServletRequest request){
        return stockService.getFollowedStocks(request);
    }

    @Operation(summary = "Get followed stocks data", description = "Get followed stocks data, requries user to be logged in")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stocks data fetched successfully"),
            @ApiResponse(responseCode = "400", description = "Stocks data could not be fetched")
    })
    @GetMapping("/getFollowedStocksData")
    public ResponseEntity<?> getFollowedStocksData(HttpServletRequest request){
        return stockService.getFollowedStocksData(request);
    }
}

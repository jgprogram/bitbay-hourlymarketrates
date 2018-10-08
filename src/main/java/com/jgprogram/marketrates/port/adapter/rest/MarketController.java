package com.jgprogram.marketrates.port.adapter.rest;

import com.jgprogram.marketrates.application.MarketRateService;
import com.jgprogram.marketrates.application.dto.HourlyMarketRatesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/markets")
public class MarketController {

    private final MarketRateService marketRateService;

    @Autowired
    public MarketController(MarketRateService marketRateService) {
        this.marketRateService = marketRateService;
    }

    @GetMapping(path = "/{marketCode}/rates/{day}")
    public ResponseEntity<HourlyMarketRatesDTO> getHourlyMarketRates(
            @PathVariable String marketCode,
            @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date day,
            @RequestParam Integer[] hours
    ) {
        return ResponseEntity.ok(
                marketRateService.getMarketRatesHourly(marketCode, day, hours));
    }
}

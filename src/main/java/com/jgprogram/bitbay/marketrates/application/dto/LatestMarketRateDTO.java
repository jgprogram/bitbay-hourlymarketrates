package com.jgprogram.bitbay.marketrates.application.dto;

import java.util.Date;

public class LatestMarketRateDTO {

    private Date date;

    public LatestMarketRateDTO(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}

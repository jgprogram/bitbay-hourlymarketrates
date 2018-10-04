package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import java.util.Date;

public interface MarketRateDataAdapter {
    void loadDataSince(Date previousFullHour);
}

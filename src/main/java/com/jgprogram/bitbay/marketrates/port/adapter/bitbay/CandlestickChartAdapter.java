package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import java.util.Date;
import java.util.Set;

public interface CandlestickChartAdapter {
    Set<CandlestickChartData> getSince(Date previousFullHour);
}

package com.jgprogram.marketrates.bitbay;

import java.util.Objects;

public class MarketData {
    private String code;

    public MarketData(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketData market = (MarketData) o;
        return Objects.equals(code, market.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

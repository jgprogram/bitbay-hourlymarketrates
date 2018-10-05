package com.jgprogram.bitbay.marketrates.port.adapter.bitbay;

import java.util.Objects;

class Market {
    private String code;

    public Market(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return Objects.equals(code, market.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}

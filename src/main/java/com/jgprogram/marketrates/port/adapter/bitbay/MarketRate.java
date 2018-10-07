package com.jgprogram.marketrates.port.adapter.bitbay;

import java.util.Date;
import java.util.Objects;

public class MarketRate {

    private final String code;
    private final Date date;
    private final Double o;
    private final Double c;
    private final Double h;
    private final Double l;
    private final Double v;

    public MarketRate(String code, Date date, Double o, Double c, Double h, Double l, Double v) {
        this.code = code;
        this.date = date;
        this.o = o;
        this.c = c;
        this.h = h;
        this.l = l;
        this.v = v;
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public Double getO() {
        return o;
    }

    public Double getC() {
        return c;
    }

    public Double getH() {
        return h;
    }

    public Double getL() {
        return l;
    }

    public Double getV() {
        return v;
    }

    @Override
    public boolean equals(Object o1) {
        if (this == o1) return true;
        if (o1 == null || getClass() != o1.getClass()) return false;
        MarketRate that = (MarketRate) o1;
        return Objects.equals(code, that.code) &&
                Objects.equals(date, that.date) &&
                Objects.equals(o, that.o) &&
                Objects.equals(c, that.c) &&
                Objects.equals(h, that.h) &&
                Objects.equals(l, that.l) &&
                Objects.equals(v, that.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, date, o, c, h, l, v);
    }
}

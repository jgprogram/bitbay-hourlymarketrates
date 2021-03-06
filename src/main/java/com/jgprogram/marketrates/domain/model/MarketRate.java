package com.jgprogram.marketrates.domain.model;

import com.jgprogram.common.domain.model.DomainEntity;
import com.jgprogram.common.util.TimeFullUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class MarketRate extends DomainEntity {
    private String marketCode;
    private Date date;
    private Date day;
    private Integer hour;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal highest;
    private BigDecimal lowest;
    private BigDecimal average;

    public MarketRate(String marketCode, Date date, Double open, Double close, Double highest, Double lowest) {
        setMarketCode(marketCode);
        setDate(date);
        setOpen(open);
        setClose(close);
        setHighest(highest);
        setLowest(lowest);
        calculateDay(date);
        calculateHour(date);
        calculateAverage();
    }

    public String marketCode() {
        return marketCode;
    }

    public Date date() {
        return date;
    }

    public Date day() {
        return day;
    }

    public Integer hour() {
        return hour;
    }

    public BigDecimal open() {
        return open;
    }

    public BigDecimal close() {
        return close;
    }

    public BigDecimal highest() {
        return highest;
    }

    public BigDecimal lowest() {
        return lowest;
    }

    public BigDecimal average() {
        return average;
    }

    private void calculateAverage() {
        average = open
                .add(close)
                .divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
    }

    private void calculateDay(Date date) {
        this.day = TimeFullUnit.dayFrom(date);
    }

    private void calculateHour(Date date) {
        this.hour = TimeFullUnit.hourFrom(date);
    }

    private void setMarketCode(String marketCode) {
        assertArgumentNotNull(marketCode, "MarketData code is required");
        this.marketCode = marketCode;
    }

    private void setDate(Date date) {
        assertArgumentNotNull(date, "Date is required");
        this.date = date;
    }

    private void setOpen(BigDecimal open) {
        assertArgumentNotNull(open, "Open is required");
        assertArgumentTrue(open.compareTo(BigDecimal.ZERO) >= 0,
                "Open must be gt or eq 0");
        this.open = open;
    }

    private void setOpen(Double open) {
        assertArgumentNotNull(open, "Open is required");
        setOpen(BigDecimal.valueOf(open));
    }

    private void setClose(BigDecimal close) {
        assertArgumentNotNull(close, "Close is required");
        assertArgumentTrue(close.compareTo(BigDecimal.ZERO) >= 0,
                "Close must be gt or eq 0");
        this.close = close;
    }

    private void setClose(Double close) {
        assertArgumentNotNull(close, "Close is required");
        setClose(BigDecimal.valueOf(close));
    }

    private void setHighest(BigDecimal highest) {
        assertArgumentNotNull(highest, "Highest is required");
        assertArgumentTrue(highest.compareTo(BigDecimal.ZERO) >= 0,
                "Highest must be gt or eq 0");
        this.highest = highest;
    }

    private void setHighest(Double highest) {
        assertArgumentNotNull(highest, "Highest is required");
        setHighest(BigDecimal.valueOf(highest));
    }

    private void setLowest(BigDecimal lowest) {
        assertArgumentNotNull(lowest, "Lowest is required");
        assertArgumentTrue(lowest.compareTo(BigDecimal.ZERO) >= 0,
                "Lowest must be gt or eq 0");
        this.lowest = lowest;
    }

    private void setLowest(Double lowest) {
        assertArgumentNotNull(lowest, "Lowest is required");
        setLowest(BigDecimal.valueOf(lowest));
    }
}

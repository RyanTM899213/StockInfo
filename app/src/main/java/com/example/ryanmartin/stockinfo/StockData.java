package com.example.ryanmartin.stockinfo;

/**
 * Created by ryanmartin on 2016-01-16.
 */
public class StockData {

    private String symbol = null;
    private String averageDailyVolume = null;
    private String change = null;
    private String daysLow = null;
    private String daysHigh = null;
    private String yearLow = null;
    private String yearHigh = null;
    private String marketCapitalization = null;
    private String lastTradePriceOnly = null;
    private String daysRange = null;
    private String name = null;
    private String volume = null;
    private String stockExchange = null;

    public StockData(String sym, String avgDailyV, String chng, String dysLo, String dysHi, String yrLo,
                     String yrHi, String mrktCap, String lastTrdPrxOnly, String dysRng, String nam, String vol, String stckExchng) {
        symbol = sym;
        averageDailyVolume = avgDailyV;
        change = chng;
        daysLow = dysLo;
        daysHigh = dysHi;
        yearLow = yrLo;
        yearHigh = yrHi;
        marketCapitalization = mrktCap;
        lastTradePriceOnly = lastTrdPrxOnly;
        daysRange = dysRng;
        name = nam;
        volume = vol;
        stockExchange = stckExchng;
    }

    public String getSymbol() { return symbol; }

    public String getAverageDailyVolume() {
        return averageDailyVolume;
    }

    public String getChange() {
        return change;
    }

    public String getDaysLow() {
        return daysLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public String getYearLow() {
        return yearLow;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public String getMarketCapitalization() {
        return marketCapitalization;
    }

    public String getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }

    public String getDaysRange() {
        return daysRange;
    }

    public String getName() {
        return name;
    }

    public String getVolume() {
        return volume;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public boolean isEmpty() {
        return (averageDailyVolume.equals("null") && change.equals("null") && daysLow.equals("null") && daysHigh.equals("null") &&
                yearLow.equals("null") && yearHigh.equals("null") && marketCapitalization.equals("null") && lastTradePriceOnly.equals("null")
                && daysRange.equals("null") && name.equals("null") && volume.equals("null") && stockExchange.equals("null"));
    }
}
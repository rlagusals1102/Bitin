package com.example.community.domain;

public class CoinAssetInfo {
    private String coinSymbol;
    private Double balance;
    private Double avgBuyBalance;

    public CoinAssetInfo(String coinSymbol, Double balance, Double avgBuyBalance) {
        this.coinSymbol = coinSymbol;
        this.balance = balance;
        this.avgBuyBalance = avgBuyBalance;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getAvgBuyBalance() {
        return avgBuyBalance;
    }
}

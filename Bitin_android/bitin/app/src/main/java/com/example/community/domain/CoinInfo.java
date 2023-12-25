package com.example.community.domain;

import java.util.ArrayList;

public class CoinInfo {
    private String coinName;
    private String coinSymbol;
    private Double coinPrice;
    private Double changePercent;
    private ArrayList<Integer> lineData;
    private Double propertyAmount;
    private Double propertySize;

    public CoinInfo(String coinName, String coinSymbol, Double coinPrice, Double changePercent, ArrayList<Double> lineData, Double propertyAmount, Double propertySize) {
        this.coinName = coinName;
        this.coinSymbol = coinSymbol;
        this.coinPrice = coinPrice;
        this.changePercent = changePercent;

        this.lineData = new ArrayList<>();
        for (Double price : lineData) {
            this.lineData.add(price.intValue());
        }

        this.propertyAmount = propertyAmount;
        this.propertySize = propertySize;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public Double getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(Double coinPrice) {
        this.coinPrice = coinPrice;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public ArrayList<Integer> getLineData() {
        return lineData;
    }

    public void setLineData(ArrayList<Integer> lineData) {
        this.lineData = lineData;
    }

    public Double getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(Double propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public Double getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(Double propertySize) {
        this.propertySize = propertySize;
    }
}

package com.valdizz.exchangerates.model.network.entity;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class DailyExRates {

    @Attribute(name = "Date")
    private String date;

    @Element
    List<CurrencyRate> currencyRates;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CurrencyRate> getCurrencyRates() {
        return currencyRates;
    }

    public void setCurrencies(List<CurrencyRate> currencyRates) {
        this.currencyRates = currencyRates;
    }
}

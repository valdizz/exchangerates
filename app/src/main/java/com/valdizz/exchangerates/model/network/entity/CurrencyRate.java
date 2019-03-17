package com.valdizz.exchangerates.model.network.entity;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "Currency")
public class CurrencyRate {

    @Attribute(name = "Id")
    private String id;

    @PropertyElement(name = "NumCode")
    private String numCode;

    @PropertyElement(name = "CharCode")
    private String charCode;

    @PropertyElement(name = "Scale")
    private int scale;

    @PropertyElement(name = "Name")
    private String name;

    @PropertyElement(name = "Rate")
    private double rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}

package com.valdizz.exchangerates.model.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Currency {

    @PrimaryKey
    @NonNull
    private String numCode;
    private String charCode;
    private String name;
    private int scale;
    private int visible;
    private int orderNum;

    public Currency(@NonNull String numCode, String charCode, String name, int scale, int visible, int orderNum) {
        this.numCode = numCode;
        this.charCode = charCode;
        this.name = name;
        this.scale = scale;
        this.visible = visible;
        this.orderNum = orderNum;
    }

    @NonNull
    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(@NonNull String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}

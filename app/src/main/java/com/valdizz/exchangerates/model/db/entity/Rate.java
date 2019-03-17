package com.valdizz.exchangerates.model.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"numCode", "date"})
public class Rate {

    @NonNull
    private String numCode;
    @NonNull
    private String date;
    private double rate;

    public Rate(@NonNull String numCode, String date, double rate) {
        this.numCode = numCode;
        this.date = date;
        this.rate = rate;
    }

    @NonNull
    public String getNumCode() {
        return numCode;
    }

    public String getDate() {
        return date;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "numCode='" + numCode + '\'' +
                ", date='" + date + '\'' +
                ", rate=" + rate +
                '}';
    }
}

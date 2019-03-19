package com.valdizz.exchangerates.model.db;

import com.valdizz.exchangerates.model.db.entity.Currency;
import com.valdizz.exchangerates.model.db.entity.ExchangeRate;
import com.valdizz.exchangerates.model.db.entity.Rate;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface NBRBRatesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCurrency(Currency currency);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCurrency(Currency... currencies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRate(Rate rate);

    @Transaction
    @Query("select * from Currency order by orderNum")
    LiveData<List<Currency>> getCurrencies();

    @Query("select Currency.charCode, Currency.name, Currency.scale, Rate1.rate as rate1, Rate2.rate as rate2 " +
            "from Currency, Rate as Rate1, Rate as Rate2 " +
            "where (Currency.numCode = Rate1.numCode and Currency.visible = 1 and Rate1.date = :date1) " +
            "and (Currency.numCode = Rate2.numCode and Currency.visible = 1 and Rate2.date = :date2)" +
            "order by Currency.orderNum")
    LiveData<List<ExchangeRate>> getCurrencyRates(String date1, String date2);

}

package com.valdizz.exchangerates.model.db;

import android.content.Context;

import com.valdizz.exchangerates.model.db.entity.Currency;
import com.valdizz.exchangerates.model.db.entity.Rate;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Currency.class, Rate.class}, version = 1)
public abstract class DatabaseService extends RoomDatabase {

    private static volatile DatabaseService databaseServiceInstance;
    public abstract NBRBRatesDAO nbrbRatesDAO();

    static DatabaseService getDatabase(final Context context) {
        if (databaseServiceInstance == null) {
            synchronized (DatabaseService.class) {
                if (databaseServiceInstance == null) {
                    databaseServiceInstance = Room.databaseBuilder(context.getApplicationContext(), DatabaseService.class, "exchange_rates_db")
                            .build();
                }
            }
        }
        return databaseServiceInstance;
    }
}

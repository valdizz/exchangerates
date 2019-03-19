package com.valdizz.exchangerates.model.db;

import android.app.Application;
import android.os.AsyncTask;

import com.valdizz.exchangerates.model.db.entity.Currency;
import com.valdizz.exchangerates.model.db.entity.ExchangeRate;
import com.valdizz.exchangerates.model.db.entity.Rate;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RatesRepository {

    private NBRBRatesDAO nbrbRatesDAO;
    private LiveData<List<Currency>> currencies;

    public RatesRepository(Application application) {
        DatabaseService database = DatabaseService.getDatabase(application);
        nbrbRatesDAO = database.nbrbRatesDAO();
        currencies = nbrbRatesDAO.getCurrencies();
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencies;
    }

    public LiveData<List<ExchangeRate>> getCurrencyRates(String date1, String date2) {
        return nbrbRatesDAO.getCurrencyRates(date1, date2);
    }

    public void insertCurrency(Currency currency) {
        new insertAsyncTask(nbrbRatesDAO).execute(currency);
    }

    public void updateCurrency(Currency... currencyData) {
        new updateAsyncTask(nbrbRatesDAO).execute(currencyData);
    }

    public void insertRate(Rate rate) {
        new insertAsyncTask(nbrbRatesDAO).execute(rate);
    }

    private static class insertAsyncTask extends AsyncTask<Object, Void, Void> {

        private NBRBRatesDAO asyncTaskDao;

        insertAsyncTask(NBRBRatesDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Object... params) {
            if (params[0] instanceof Currency) {
                asyncTaskDao.insertCurrency((Currency) params[0]);
            }
            else if (params[0] instanceof Rate) {
                asyncTaskDao.insertRate((Rate) params[0]);
            }
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Currency, Void, Void> {

        private NBRBRatesDAO asyncTaskDao;

        updateAsyncTask(NBRBRatesDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Currency... currencies) {
            asyncTaskDao.updateCurrency(currencies);
            return null;
        }
    }

}

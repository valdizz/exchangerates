package com.valdizz.exchangerates.viewmodel;

import android.app.Application;

import com.valdizz.exchangerates.model.db.RatesRepository;
import com.valdizz.exchangerates.model.db.entity.Currency;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SettingsViewModel extends AndroidViewModel {

    private RatesRepository ratesRepository;
    private LiveData<List<Currency>> currencies;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        ratesRepository = new RatesRepository(application);
    }

    public LiveData<List<Currency>> getCurrencies() {
        currencies = ratesRepository.getCurrencies();
        return currencies;
    }

    public void saveCurrencies(List<Currency> currencyData) {
        int i = 0;
        for (Currency currency : currencyData) {
            currency.setOrderNum(i);
            i++;
        }
        ratesRepository.updateCurrency(currencyData.toArray(new Currency[currencyData.size()]));
    }
}

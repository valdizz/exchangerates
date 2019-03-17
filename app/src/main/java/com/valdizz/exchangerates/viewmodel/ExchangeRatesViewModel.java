package com.valdizz.exchangerates.viewmodel;

import android.app.Application;
import android.util.Log;

import com.valdizz.exchangerates.model.db.RatesRepository;
import com.valdizz.exchangerates.model.db.entity.Currency;
import com.valdizz.exchangerates.model.db.entity.ExchangeRate;
import com.valdizz.exchangerates.model.db.entity.Rate;
import com.valdizz.exchangerates.model.network.NetworkService;
import com.valdizz.exchangerates.model.network.entity.CurrencyRate;
import com.valdizz.exchangerates.model.network.entity.DailyExRates;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ExchangeRatesViewModel extends AndroidViewModel {

    private RatesRepository ratesRepository;
    private MutableLiveData<List<ExchangeRate>> currencyRates;
    private MutableLiveData<Boolean> isRefreshing;
    private MutableLiveData<Boolean> isServiceError;
    private MutableLiveData<String> date1;
    private MutableLiveData<String> date2;
    private CompositeDisposable disposables;
    private volatile boolean isPreviousDate;
    private DateFormat dateFormat;
    private static final List<String> VISIBLE_CURRENCIES = Arrays.asList("USD", "EUR", "RUB");

    public ExchangeRatesViewModel(@NonNull Application application) {
        super(application);
        currencyRates = new MutableLiveData<>();
        disposables = new CompositeDisposable();
        ratesRepository = new RatesRepository(application);
        isRefreshing = new MutableLiveData<>();
        isServiceError = new MutableLiveData<>();
        date1 = new MutableLiveData<>();
        date2 = new MutableLiveData<>();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    }

    private Observable<DailyExRates> getDailyExRatesObservable(String date) {
        return NetworkService.getInstance()
                .getNBRBXmlRatesApi()
                .getDailyExRates(date);
    }

    public LiveData<List<ExchangeRate>> getCurrencyRates() {
        loadCurrencyRates();
        return currencyRates;
    }

    private void loadCurrencyRates() {
        isRefreshing.postValue(true);
        isServiceError.postValue(false);
        isPreviousDate = false;
        Calendar date = new GregorianCalendar();
        Calendar nextDate = new GregorianCalendar();
        Calendar prevDate = new GregorianCalendar();
        nextDate.add(Calendar.DAY_OF_MONTH, 1);
        prevDate.add(Calendar.DAY_OF_MONTH, -1);

        disposables.add(Observable.concat(getDailyExRatesObservable(dateFormat.format(date.getTime())), getDailyExRatesObservable(dateFormat.format(nextDate.getTime())), getDailyExRatesObservable(dateFormat.format(prevDate.getTime())))
                .filter(dailyExRates -> (dailyExRates.getCurrencyRates() != null && !dailyExRates.getCurrencyRates().isEmpty()))
                .take(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DailyExRates>() {
                    @Override
                    public void onNext(DailyExRates dailyExRates) {
                        List<CurrencyRate> currencyRates = dailyExRates.getCurrencyRates();
                        if (currencyRates != null && !currencyRates.isEmpty()) {
                            isPreviousDate = dailyExRates.getDate().equals(dateFormat.format(prevDate.getTime()));
                            for (CurrencyRate currencyRate : currencyRates) {
                                Currency currency = new Currency(currencyRate.getNumCode(), currencyRate.getCharCode(), currencyRate.getName(), currencyRate.getScale(), (VISIBLE_CURRENCIES.contains(currencyRate.getCharCode()) ? 1 : 0), 0);
                                insertCurrency(currency);
                                Rate rate = new Rate(currencyRate.getNumCode(), dailyExRates.getDate(), currencyRate.getRate());
                                insertRate(rate);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isRefreshing.postValue(false);
                        isServiceError.postValue(true);
                    }

                    @Override
                    public void onComplete() {
                        isRefreshing.postValue(false);
                        if (isPreviousDate) {
                            getCurrencyRatesFromDB(prevDate, date);
                        }
                        else {
                            getCurrencyRatesFromDB(date, nextDate);
                        }
                    }
                }));
    }

    private void getCurrencyRatesFromDB(Calendar date, Calendar dateNext) {
        DateFormat dateFormatView = new SimpleDateFormat("dd.MM.yy");
        date1.postValue(dateFormatView.format(date.getTime()));
        date2.postValue(dateFormatView.format(dateNext.getTime()));
        try {
            currencyRates.postValue(ratesRepository.getCurrencyRates(dateFormat.format(date.getTime()), dateFormat.format(dateNext.getTime())));
        } catch (ExecutionException | InterruptedException e) {
            isRefreshing.postValue(false);
            isServiceError.postValue(true);
        }
    }

    private void insertCurrency(Currency currency) {
        ratesRepository.insertCurrency(currency);
    }

    private void insertRate(Rate rate) {
        ratesRepository.insertRate(rate);
    }

    public LiveData<Boolean> getRefreshing() {
        return isRefreshing;
    }

    public LiveData<Boolean> getServiceError() {
        return isServiceError;
    }

    public LiveData<String> getDate1() {
        return date1;
    }

    public LiveData<String> getDate2() {
        return date2;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.dispose();
    }
}

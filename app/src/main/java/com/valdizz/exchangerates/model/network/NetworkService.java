package com.valdizz.exchangerates.model.network;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class NetworkService {

    private static volatile NetworkService networkServiceInstance;
    private Retrofit retrofit;
    private static final String NBRB_URL = "http://www.nbrb.by/Services/";

    private NetworkService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(NBRB_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(true).build()))
                .build();
    }

    public static NetworkService getInstance() {
        if (networkServiceInstance == null) {
            synchronized (NetworkService.class) {
                if (networkServiceInstance == null) {
                    networkServiceInstance = new NetworkService();
                }
            }
        }
        return networkServiceInstance;
    }

    public NBRBXmlRatesApi getNBRBXmlRatesApi() {
        return retrofit.create(NBRBXmlRatesApi.class);
    }
}

package com.valdizz.exchangerates.model.network;

import com.valdizz.exchangerates.model.network.entity.DailyExRates;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NBRBXmlRatesApi {

    @GET("XmlExRates.aspx")
    Observable<DailyExRates> getDailyExRates(@Query("ondate") String date);
}

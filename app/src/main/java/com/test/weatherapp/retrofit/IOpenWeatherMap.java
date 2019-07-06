package com.test.weatherapp.retrofit;

import com.test.weatherapp.model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByCoordinates(@Query("lat") String lat,
                                                      @Query("lon") String lon,
                                                      @Query("appId") String appId,
                                                      @Query("units") String units);
}

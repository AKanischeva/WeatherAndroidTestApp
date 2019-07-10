package com.test.weatherapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.test.weatherapp.common.Common;
import com.test.weatherapp.model.WeatherResult;
import com.test.weatherapp.retrofit.IOpenWeatherMap;
import com.test.weatherapp.retrofit.RetrofitClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentLocationWeatherFragment extends Fragment {

    static CurrentLocationWeatherFragment instance;
    ImageView imgWeather;
    TextView txtCityName, txtHumidity, txtPressure, txtTemperature, txtDescription, txtDateTime, txtWind;
    LinearLayout weatherPanel;
    ProgressBar loading;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;
    SwipeRefreshLayout swipeLayout;

    public CurrentLocationWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    public static CurrentLocationWeatherFragment getInstance() {
        if (instance == null) {
            instance = new CurrentLocationWeatherFragment();
        }
        return instance;
    }

    public static CurrentLocationWeatherFragment getNewInstance(){
        return new CurrentLocationWeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_today_weather, container, false);
        imgWeather = (ImageView) itemView.findViewById(R.id.img_weather);
        txtCityName = (TextView) itemView.findViewById(R.id.txt_city_name);
        txtHumidity = (TextView) itemView.findViewById(R.id.txt_humidity);
        txtPressure = (TextView) itemView.findViewById(R.id.txt_pressure);
        txtTemperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
        txtDateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
        txtWind = (TextView) itemView.findViewById(R.id.txt_wind);

        weatherPanel = (LinearLayout) itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar) itemView.findViewById(R.id.loading);

        getWeatherInformation();

        // Getting SwipeContainerLayout
        swipeLayout = itemView.findViewById(R.id.swipe_container);
        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Updating information!", Toast.LENGTH_LONG).show();
                new Handler().post(new Runnable() {
                    @Override public void run() {
                        if(isNetworkAvailable()){
                            getWeatherInformation();
                        } else {
                            Toast.makeText(getActivity(), "No internet available!", Toast.LENGTH_LONG).show();
                        }
                        swipeLayout.setRefreshing(false);
                    }
                });
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        return itemView;
    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByCoordinates(String.valueOf(Common.currentLocation.getLatitude()),
                String.valueOf(Common.currentLocation.getLongitude()),
                String.valueOf(Common.appId),
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) {
                        //Load image
                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/wn/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(imgWeather);

                        txtCityName.setText(weatherResult.getName());
                        txtHumidity.setText(new StringBuilder(weatherResult.getMain().getHumidity()).append("%"));
                        txtPressure.setText(new StringBuilder(weatherResult.getMain().getPressure()).append(" hpa"));
                        txtTemperature.setText(new StringBuilder(weatherResult.getMain().getTemp()).append("°C"));
                        txtDescription.setText(new StringBuilder("Weather in ").append(weatherResult.getName()));
                        txtDateTime.setText(Common.convertUnixToDate(weatherResult.getDt()));
//                        txtDateTime.setText(Common.getCurrentTimeUsingDate());
                        txtWind.setText(weatherResult.getWind().toString());

                        weatherPanel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private boolean isNetworkAvailable() {
        if (getContext() != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
}


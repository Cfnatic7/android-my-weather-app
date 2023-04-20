package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherForecastFragment extends Fragment {

    private RecyclerView forecastRecyclerView;
    private WeatherForecastAdapter weatherForecastAdapter;

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forecastRecyclerView = view.findViewById(R.id.forecastRecyclerView);
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            WeatherData weatherData = (WeatherData) getArguments().getSerializable("weatherData");
            if (weatherData != null) {
                weatherForecastAdapter = new WeatherForecastAdapter(weatherData.getDays());
                forecastRecyclerView.setAdapter(weatherForecastAdapter);
            }
        }
    }
}

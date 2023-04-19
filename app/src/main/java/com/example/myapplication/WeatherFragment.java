package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class WeatherFragment extends Fragment {

    private TextView cityNameTextView;
    private TextView coordinatesTextView;
    private TextView dateTimeTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView weatherDescriptionTextView;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Initialize views
        cityNameTextView = view.findViewById(R.id.cityNameTextView);
        coordinatesTextView = view.findViewById(R.id.coordinatesTextView);
        dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        weatherDescriptionTextView = view.findViewById(R.id.weatherDescriptionTextView);

        return view;
    }

    public void updateWeatherData(WeatherData weatherData) {
        // Update views with the new weather data
        cityNameTextView.setText(weatherData.getCityName());
        coordinatesTextView.setText("latidute: " + weatherData.getLatitude() + ", longitude: " + weatherData.getLongitude());
        dateTimeTextView.setText(weatherData.getDays().get(0).getDatetime());
        temperatureTextView.setText(String.valueOf(weatherData.getDays().get(0).getTemp()));
        pressureTextView.setText(String.valueOf(weatherData.getDays().get(0).getPressure()));

        // You might need to create a WeatherData class to hold and manage the weather data properties.
    }
}


package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        WeatherData weatherData = (WeatherData) getArguments().getSerializable("weatherData");

        cityNameTextView.setText(weatherData.getCityName());
        coordinatesTextView.setText("latitude: " + weatherData.getLatitude() + ", longitude: " + weatherData.getLongitude());
        dateTimeTextView.setText("current date: " + weatherData.getDays().get(0).getDatetime());
        temperatureTextView.setText(String.valueOf(weatherData.getDays().get(0).getTemp()) + " °C");
        pressureTextView.setText(String.valueOf(weatherData.getDays().get(0).getPressure()) + " hPa");
        weatherDescriptionTextView.setText(weatherData.getDays().get(0).getDescription());

        return view;
    }
}


package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AdditionalWeatherFragment extends Fragment {

    private TextView windSpeedTextView;
    private TextView windDirectionTextView;
    private TextView humidityTextView;

    public AdditionalWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_additional_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        windSpeedTextView = view.findViewById(R.id.windSpeedTextView);
        windDirectionTextView = view.findViewById(R.id.windDirectionTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);

        if (getArguments() != null) {
            WeatherData weatherData = (WeatherData) getArguments().getSerializable("weatherData");
            if (weatherData != null) {
                if (MainActivity.speedUnitId == R.id.action_kmh) {
                    String.format("Wind Speed: %.1f km/h", weatherData.getDays().get(0).getWindspeed());
                    windSpeedTextView.setText(String.format("Wind Speed: %.1f km/h", weatherData.getDays().get(0).getWindspeed()));
                }
                else if (MainActivity.speedUnitId == R.id.action_ms) {
                    windSpeedTextView.setText(String.format("Wind Speed: %.1f m/s", weatherData.getDays().get(0).getWindspeed()));
                }

                windDirectionTextView.setText("Wind Direction: " + weatherData.getDays().get(0).getWinddir() + "°");
                humidityTextView.setText("Humidity: " + weatherData.getDays().get(0).getHumidity() + "%");
            }
        }
    }

}

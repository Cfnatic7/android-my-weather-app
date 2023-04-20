package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WeatherPagerAdapter extends FragmentStateAdapter {
    private final WeatherData weatherData;

    public WeatherPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, WeatherData weatherData) {
        super(fragmentManager, lifecycle);
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                WeatherFragment weatherFragment = new WeatherFragment();
                Bundle weatherArgs = new Bundle();
                weatherArgs.putSerializable("weatherData", weatherData);
                weatherFragment.setArguments(weatherArgs);
                return weatherFragment;
            case 1:
                AdditionalWeatherFragment additionalWeatherFragment = new AdditionalWeatherFragment();
                Bundle additionalWeatherArgs = new Bundle();
                additionalWeatherArgs.putSerializable("weatherData", weatherData);
                additionalWeatherFragment.setArguments(additionalWeatherArgs);
                return additionalWeatherFragment;
            case 2:
                WeatherForecastFragment weatherForecastFragment = new WeatherForecastFragment();
                Bundle weatherForecastArgs = new Bundle();
                weatherForecastArgs.putSerializable("weatherData", weatherData);
                weatherForecastFragment.setArguments(weatherForecastArgs);
                return weatherForecastFragment;
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

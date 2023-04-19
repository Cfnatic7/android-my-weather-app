package com.example.myapplication;

import android.content.Context;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WeatherUtils {
    public static WeatherData getWeatherDataFromFile(Context context, String cityName) {
        File file = new File(context.getFilesDir(), cityName + ".json");
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                Gson gson = new Gson();
                WeatherData weatherData = gson.fromJson(fileReader, WeatherData.class);
                weatherData.setCityName(cityName);
                fileReader.close();
                return weatherData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}


package com.example.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;

public class CityNameValidator extends AsyncTask<String, Void, Boolean> {
    private OnValidationResultListener onValidationResultListener;

    public CityNameValidator(OnValidationResultListener onValidationResultListener) {
        this.onValidationResultListener = onValidationResultListener;
    }

    @Override
    protected Boolean doInBackground(String... cities) {
        String city = cities[0];
        try {
            URL url = new URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + city + "/" + LocalDate.now(ZoneId.systemDefault()) + "/" + LocalDate.now(ZoneId.systemDefault()).plusDays(3) + "?unitGroup=metric&key=" + MainActivity.apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            urlConnection.disconnect();

            return !stringBuilder.toString().contains("Invalid location found");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        onValidationResultListener.onValidationResult(result);
    }

    public interface OnValidationResultListener {
        void onValidationResult(Boolean isValid);
    }
}

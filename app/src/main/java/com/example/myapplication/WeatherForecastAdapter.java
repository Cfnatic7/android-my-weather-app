package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private final List<WeatherDay> weatherDays;

    public WeatherForecastAdapter(List<WeatherDay> weatherDays) {
        this.weatherDays = weatherDays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherDay weatherDay = weatherDays.get(position);
        holder.dateTextView.setText(weatherDay.getDatetime());
        if (MainActivity.temperatureUnitId == R.id.action_fahrenheit) {
            holder.temperatureTextView.setText(String.format("Temperature: %.1f °F", weatherDay.getTemp()));
        }
        else if (MainActivity.temperatureUnitId == R.id.action_celsius) {
            holder.temperatureTextView.setText(String.format("Temperature: %.1f °C", weatherDay.getTemp()));
        }

        holder.conditionsTextView.setText(weatherDay.getConditions());
    }

    @Override
    public int getItemCount() {
        return weatherDays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView temperatureTextView;
        private final TextView conditionsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureItemTextView);
            conditionsTextView = itemView.findViewById(R.id.conditionsTextView);
        }
    }
}

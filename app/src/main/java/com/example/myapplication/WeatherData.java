package com.example.myapplication;

import java.io.Serializable;
import java.util.List;

public class WeatherData implements Serializable {
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public List<WeatherDay> getDays() {
        return days;
    }

    public WeatherData(double latitude, double longitude, String cityName, String timeZone, List<WeatherDay> days) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
        this.timeZone = timeZone;
        this.days = days;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public void setDays(List<WeatherDay> days) {
        this.days = days;
    }

    private double latitude;
    private double longitude;
    private String cityName;
    private String timeZone;
    private List<WeatherDay> days;
}

class WeatherDay implements Serializable {
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPrecip() {
        return precip;
    }

    public void setPrecip(double precip) {
        this.precip = precip;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getWinddir() {
        return winddir;
    }

    public void setWinddir(int winddir) {
        this.winddir = winddir;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(double cloudCover) {
        this.cloudCover = cloudCover;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public WeatherDay() {
    }

    private String datetime;
    private double temp;
    private double maxTemp;
    private double minTemp;
    private double humidity;
    private double precip;
    private double windspeed;
    private double winddir;
    private double pressure;
    private double cloudCover;
    private String sunrise;
    private String sunset;
    private String conditions;
    private String icon;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public WeatherDay(String datetime, double temp, double maxTemp, double minTemp, int humidity, double precip, double windspeed, int winddir, double pressure, double cloudCover, String sunrise, String sunset, String conditions, String icon, String description) {
        this.datetime = datetime;
        this.temp = temp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.precip = precip;
        this.windspeed = windspeed;
        this.winddir = winddir;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.conditions = conditions;
        this.icon = icon;
        this.description = description;
    }
}


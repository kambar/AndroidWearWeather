package com.barbachowski.k.weatherwearapp;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by K on 23/11/2016.
 */

public class WeatherApplication extends Application {
    private Bitmap weatherBitmap;
    private String maxTemp;
    private String minTemp;
    private String desc;
    private boolean weatherDataInitialized;

    public WeatherApplication(){
        //data not initalized yet
        weatherDataInitialized = false;
    }

    public Bitmap getWeatherBitmap() {
        return weatherBitmap;
    }

    public void setWeatherBitmap(Bitmap bitmap) {
        this.weatherBitmap = bitmap;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isWeatherDataInitialized() {
        return weatherDataInitialized;
    }

    public void setWeatherDataInitialized(boolean weatherDataInitialized) {
        this.weatherDataInitialized = weatherDataInitialized;
    }
}

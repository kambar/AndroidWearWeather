package com.barbachowski.k.weatherwearapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class WeatherListenerService extends WearableListenerService{

    private static final long TIMEOUT_MS = 5000;
    public final String LOG_TAG = WeatherListenerService.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;

    public WeatherListenerService() {
        Log.v(LOG_TAG, "WeatherListenerService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        Log.v(LOG_TAG, "connectingGoogleApi");
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        Log.v(LOG_TAG, "onDataChanged");
        for(DataEvent dataEvent : dataEventBuffer)
        {
            Log.v(LOG_TAG, "onDataChanged dataEvent");
            if(dataEvent.getType()!=DataEvent.TYPE_CHANGED) {
                Log.v(LOG_TAG, "onDataChanged !TYPE_CHANGED");
                continue;
            }

            DataItem dataItem = dataEvent.getDataItem();

            if(!dataItem.getUri().getPath().equals("/weather")){
                continue;
            }
            Log.v(LOG_TAG, "onDataChanged /weather");
            DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);

            DataMap weatherData = dataMapItem.getDataMap();

            updateData(weatherData);
        }
    }

    private void updateData(DataMap weatherData) {
        ((WeatherApplication)this.getApplication()).setWeatherDataInitialized(true);
        ((WeatherApplication)this.getApplication()).setMaxTemp(weatherData.getString("maxTemp"));
        ((WeatherApplication)this.getApplication()).setMinTemp(weatherData.getString("minTemp"));
        ((WeatherApplication)this.getApplication()).setDesc(weatherData.getString("desc"));
        Asset weatherAsset = weatherData.getAsset("weatherImage");
        Bitmap weatherBitmap = loadBitmapFromAsset(weatherAsset);
        ((WeatherApplication)this.getApplication()).setWeatherBitmap(weatherBitmap);
    }
    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

}

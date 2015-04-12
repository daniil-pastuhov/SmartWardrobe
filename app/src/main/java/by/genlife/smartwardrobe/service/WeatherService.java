package by.genlife.smartwardrobe.service;

import android.content.Intent;
import android.os.IBinder;

import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;
import by.genlife.smartwardrobe.task.FetchWeatherTask;

public class WeatherService extends BaseBackgroundService {
    private static String weather;
    private boolean processing = false;
    private static OnTaskCompleteListener<String> listener;

    public WeatherService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getWeather(listener == null ? OnTaskCompleteListener.<String>getEmptyListener() : listener, true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getWeather(final OnTaskCompleteListener<String> listener, boolean force) {
        this.listener = listener;
        if (processing) return;
        if (weather == null || force) {
            processing = true;
            FetchWeatherTask weatherTask = new FetchWeatherTask(this, new OnTaskCompleteListener<String>() {
                @Override
                public void success(String result) {
                    weather = result;
                    listener.success(result);
                    processing = false;
                }

                @Override
                public void error(String message) {
                    listener.error(message);
                    processing = false;
                }
            });
            weatherTask.execute("Minsk", "metric");
        } else {
            listener.success(weather);
        }
    }

    public static void setListener(OnTaskCompleteListener<String> listener) {
        WeatherService.listener = listener;
    }

    public static String getWeather() {
        return weather;
    }
}

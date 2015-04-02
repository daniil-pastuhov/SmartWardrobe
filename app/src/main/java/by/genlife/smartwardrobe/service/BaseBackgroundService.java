package by.genlife.smartwardrobe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by NotePad.by on 30.03.2015.
 */
public class BaseBackgroundService extends Service {
    public interface Requester<T extends WeatherService> {
        public void requestService(T service);
    }
    @Override
    public void onCreate() {
        super.onCreate();
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
}
package com.nehvedovich.vladimir.pogoda.screens.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.Nullable;

import com.nehvedovich.vladimir.pogoda.R;

import java.util.Locale;

import static java.lang.Thread.sleep;

public class BackgroundService extends IntentService implements SensorEventListener {

    private static String infoTemperature;

    int messageId = 0;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private boolean running = true;
    private float currentTemperature, previousTemperature;

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Здесь уже идет фоновая работа, не требующая
        // создания своего потока
        startSensor();
        //для вывода уведомленя в случае изменения температуры с задержкой 3с.
        if (mTemperature != null) {
            while (running) {
                if (currentTemperature != previousTemperature) {
                    makeNote(infoTemperature);
                    previousTemperature = currentTemperature;
                }
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentTemperature = event.values[0];
        infoTemperature = String.format(Locale.US, "%.0f", currentTemperature) + " ℃";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }


    private void startSensor() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemperature == null) {
            running = false;
        } else {
            mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    // Вывод уведомления в строке состояния
    private void makeNote(String message) {

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(message);
        builder.setContentText(getString(R.string.current_temperature));
        Intent resultIntent = new Intent(this, BackgroundService.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
}

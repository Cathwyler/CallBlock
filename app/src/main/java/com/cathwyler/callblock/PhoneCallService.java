package com.cathwyler.callblock;

import android.app.Service;
import android.app.Notification;

import android.content.Intent;

import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class PhoneCallService extends Service {
    private static final String TAG = "PhoneCallService";
    @Override
    public void onCreate() {
        Logger.appendLog("Service create");
        Log.d(TAG, "Service create");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.appendLog("Service start");
        Log.d(TAG, "Service start");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            this.startForeground(100, new Notification.Builder(this).build());
        } else {
            this.startForeground(100, new Notification.Builder(this, "CHANNEL_ID").build());
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        Logger.appendLog("Service bind");
        Log.d(TAG, "Service bind");
        return null;
    }
    @Override
    public void onDestroy() {
        Logger.appendLog("Service destroy");
        Log.d(TAG, "Service destroy");
    }

}
package com.cathwyler.callblock;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class CallBlock extends Activity {
    private static final int REQUEST_PERMISSION_PHONE_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            File fConfig = new File(this.getExternalFilesDir(null), "config.properties");
            if (!fConfig.exists()) {
                Properties pConfig = new Properties();
                pConfig.setProperty("Regex", PhoneCallReceiver.REGEX_DEFAULT);
                OutputStream osConfig = new FileOutputStream(fConfig);
                pConfig.store(osConfig, "CallBlock config file");
                osConfig.close();
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                this.startService(new Intent(this, PhoneCallService.class));
                this.finish();
            } else if (
                this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                &&
                this.checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
                &&
                this.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED
                &&
                this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                &&
                this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                this.checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    this.startService(new Intent(this, PhoneCallService.class));
                } else {
                    this.startForegroundService(new Intent(this, PhoneCallService.class));
                }
                this.finish();
            } else {
                this.requestPermissions(new String[] {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED
                }, REQUEST_PERMISSION_PHONE_CODE);
            }
        } catch (Exception e) {
            Logger.appendLog(this, Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int iRequestCode, String[] arrPermission, int[] arrGrantResult) {
        super.onRequestPermissionsResult(iRequestCode, arrPermission, arrGrantResult);
        try {
            switch (iRequestCode) {
                case REQUEST_PERMISSION_PHONE_CODE :
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                        this.startService(new Intent(this, PhoneCallService.class));
                    } else {
                        this.startForegroundService(new Intent(this, PhoneCallService.class));
                    }
                    this.finish();
            }
        } catch (Exception e) {
            Logger.appendLog(this, Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }
}
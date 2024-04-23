package com.cathwyler.callblock;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();
        if (ACTION_BOOT_COMPLETED.equals(strAction)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                context.startService(new Intent(context, PhoneCallService.class));
            } else {
                context.startForegroundService(new Intent(context, PhoneCallService.class));
            }
        }
    }
}

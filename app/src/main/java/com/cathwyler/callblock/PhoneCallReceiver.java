package com.cathwyler.callblock;

import static android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED;
import static android.telephony.TelephonyManager.EXTRA_STATE;
import static android.telephony.TelephonyManager.EXTRA_STATE_RINGING;
import static android.telephony.TelephonyManager.EXTRA_STATE_OFFHOOK;
import static android.telephony.TelephonyManager.EXTRA_STATE_IDLE;
import static android.telephony.TelephonyManager.EXTRA_INCOMING_NUMBER;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import android.telephony.TelephonyManager;
import android.telecom.TelecomManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;
import java.lang.reflect.Method;
import java.lang.Math;

public class PhoneCallReceiver extends BroadcastReceiver {
    public static final String REGEX_DEFAULT = "((((\\+)|(00))33( |))|0)((1( |-|.|)62)|(1( |-|.|)63)|(2( |-|.|)70)|(2( |-|.|)71)|(3( |-|.|)77)|(3( |-|.|)78)|(4( |-|.|)24)|(4( |-|.|)25)|(5( |-|.|)68)|(5( |-|.|)69)|(9( |-|.|)48)|(9( |-|.|)49)).*";

    private static final String TAG = "PhoneCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String strAction = intent.getAction();
            if (!ACTION_PHONE_STATE_CHANGED.equals(strAction)) {
                Logger.appendLog(context, strAction);
                Log.d(TAG, strAction);
            } else {
                Bundle bundleExtras = intent.getExtras();
                String strState = bundleExtras.getString(EXTRA_STATE);
                String strPhoneNumber = bundleExtras.getString(EXTRA_INCOMING_NUMBER);
                if (!EXTRA_STATE_RINGING.equals(strState) || strPhoneNumber == null) {
                    Logger.appendLog(context, strAction + ", " + strState);
                    Log.d(TAG, strAction + ", " + strState);
                } else {
                    String strAnonymizedPhoneNumber = strPhoneNumber.substring(0, Math.max(strPhoneNumber.length() - 6, 0)) + "******";

                    File fConfig = new File(context.getExternalFilesDir(null), "config.properties");
                    Properties pConfig = new Properties();
                    InputStream isConfig = new FileInputStream(fConfig);
                    pConfig.load(isConfig);
                    isConfig.close();
                    String strRegex = pConfig.getProperty("Regex", REGEX_DEFAULT);

                    if (!Pattern.matches(strRegex, strPhoneNumber)) {
                        Logger.appendLog(context, strAction + ", " + strState + ", incoming call " + strAnonymizedPhoneNumber);
                        Log.d(TAG, strAction + ", " + strState + ", incoming call " + strAnonymizedPhoneNumber);
                    } else if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)) {
                        if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                            Logger.appendLog(context, strAction + ", " + strState + ", miss " + Manifest.permission.ANSWER_PHONE_CALLS + ", incoming call " + strAnonymizedPhoneNumber);
                            Log.d(TAG, strAction + ", " + strState + ", miss " + Manifest.permission.ANSWER_PHONE_CALLS + ", incoming call " + strAnonymizedPhoneNumber);
                        } else {
                            TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                            tm.endCall();
                            Logger.appendLog(context, strAction + ", " + strState + ", TelecomManager, blocked call " + strAnonymizedPhoneNumber);
                            Log.d(TAG, strAction + ", " + strState + ", TelecomManager, blocked call " + strAnonymizedPhoneNumber);
                        }
                    } else {
                        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            Logger.appendLog(context, strAction + ", " + strState + ", miss " + Manifest.permission.READ_PHONE_STATE + ", incoming call " + strAnonymizedPhoneNumber);
                            Log.d(TAG, strAction + ", " + strState + ", miss " + Manifest.permission.READ_PHONE_STATE + ", incoming call " + strAnonymizedPhoneNumber);
                        } else {
                            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                            Class ctm = Class.forName(tm.getClass().getName());
                            Method mGetITelephony = ctm.getDeclaredMethod("getITelephony");
                            mGetITelephony.setAccessible(true);
                            Object oITelephony = mGetITelephony.invoke(tm);

                            Class cITelephony = Class.forName(oITelephony.getClass().getName());
                            Method mEndCall = cITelephony.getDeclaredMethod("endCall");
                            mEndCall.setAccessible(true);
                            mEndCall.invoke(oITelephony);
                            Logger.appendLog(context, strAction + ", " + strState + ", TelephonyManager, blocked call " + strAnonymizedPhoneNumber);
                            Log.d(TAG, strAction + ", " + strState + ", TelephonyManager,  blocked call " + strAnonymizedPhoneNumber);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.appendLog(context, Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }
}


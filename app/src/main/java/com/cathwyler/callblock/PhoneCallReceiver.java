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

import java.util.regex.Pattern;
import java.lang.reflect.Method;

public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneCallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();
        if (ACTION_PHONE_STATE_CHANGED.equals(strAction)) {
            Log.d(TAG, strAction);
            Bundle bundleExtras = intent.getExtras();
            String strState = bundleExtras.getString(EXTRA_STATE);
            if (EXTRA_STATE_RINGING.equals(strState)) {
                String strPhoneNumber = bundleExtras.getString(EXTRA_INCOMING_NUMBER);
                if (Pattern.matches("((((\\+)|(00))33( |))|0)((1( |-|.|)62)|(1( |-|.|)63)|(2( |-|.|)70)|(2( |-|.|)71)|(3( |-|.|)77)|(3( |-|.|)78)|(4( |-|.|)24)|(4( |-|.|)25)|(5( |-|.|)68)|(5( |-|.|)69)|(9( |-|.|)48)|(9( |-|.|)49)).*", strPhoneNumber)) {
                    try {
                        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)) {
                            if (context.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                                Logger.appendLog(strState + "incoming call : " + strPhoneNumber);
                                Log.d(TAG, strState + "incoming call : " + strPhoneNumber);
                            } else {
                                TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
                                tm.endCall();
                                Logger.appendLog(strState + "blocked call : " + strPhoneNumber);
                                Log.d(TAG, strState + "blocked call : " + strPhoneNumber);
                            }
                        } else {
                            if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                Logger.appendLog(strState + "incoming call : " + strPhoneNumber);
                                Log.d(TAG, strState + "incoming call : " + strPhoneNumber);
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
                                Logger.appendLog(strState + "blocked call : " + strPhoneNumber);
                                Log.d(TAG, strState + "blocked call : " + strPhoneNumber);
                            }
                        }
                    } catch (Exception e) {
                        Logger.appendLog(e.toString());
                        e.printStackTrace();
                    }
                } else {
                    Logger.appendLog(strState + "incoming call : " + strPhoneNumber);
                    Log.d(TAG, strState + "incoming call : " + strPhoneNumber);
                }
            } else if (EXTRA_STATE_OFFHOOK.equals(strState)) {
                Logger.appendLog(strState);
                Log.d(TAG, strState);
            } else if (EXTRA_STATE_IDLE.equals(strState)) {
                Logger.appendLog(strState);
                Log.d(TAG, strState);
            }
        }
    }
}


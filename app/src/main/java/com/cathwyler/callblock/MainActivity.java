package com.cathwyler.callblock;

import android.app.Activity;

import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;

public class MainActivity extends Activity {
    private static final int REQUEST_PERMISSION_PHONE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.finish();
        } else if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            this.finish();
        } else {
            this.requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_PHONE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int iRequestCode, String[] arrPermission, int[] arrGrantResult) {
        super.onRequestPermissionsResult(iRequestCode, arrPermission, arrGrantResult);
        switch (iRequestCode) {
            case REQUEST_PERMISSION_PHONE_CODE :
                this.finish();
        }
    }
}
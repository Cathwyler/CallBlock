package com.cathwyler.callblock;

import android.content.Context;

import android.os.Environment;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
   public static void appendLog(Context context, String str) {
        try {
            File fLog = new File(context.getExternalCacheDir(), "log.txt");
            if (!fLog.exists()) {
                fLog.createNewFile();
            }
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(fLog, true));
            buf.append(str);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

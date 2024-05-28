package com.cathwyler.callblock;

import android.content.Context;

import android.os.Environment;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
   public static void appendLog(Context context, String str) {
        try {
            File fLog = new File(context.getExternalCacheDir(), "log.txt");
            if (!fLog.exists()) {
                fLog.createNewFile();
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(fLog, true));
            buf.append((new Date()) + ", " + str);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.cathwyler.callblock;

public class Logger {
    public static void appendLog(String str) {
        java.io.File logFile = new java.io.File("sdcard/log.txt");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            //BufferedWriter for performance, true to set append to file flag
            java.io.BufferedWriter buf = new java.io.BufferedWriter(new java.io.FileWriter(logFile, true));
            buf.append(str);
            buf.newLine();
            buf.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}

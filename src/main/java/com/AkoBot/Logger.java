package com.AkoBot;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private String resourceDirectory;
    public Logger(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }
    public Logger() {
        this("./src/main/resources/");
    }
    public void logError(Exception exception) {
        if (!checkForLog())
            makeNewLogFile();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            RandomAccessFile raf = new RandomAccessFile(fileNameGetter(), "rw");
            raf.seek(raf.length());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(sdf.format(new Date())).append('\n');
            stringBuilder.append(exception.getCause() == null ? "null cause " : exception.getCause().toString())
                    .append((exception.getMessage() == null ? "null message" : exception.getMessage())).append("\n");
            StackTraceElement[] stackTraceElements = exception.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (stackTraceElement.toString().startsWith("com.AkoBot"))
                    stringBuilder.append(stackTraceElement.toString()).append("\n");
            }
            stringBuilder.append("\n");
            raf.writeChars(stringBuilder.toString());
            raf.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean checkForLog() {
        return new File(fileNameGetter()).exists();
    }
    private boolean makeNewLogFile() {
        try {
            File file = new File(fileNameGetter());
            return file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private String fileNameGetter() {
        return resourceDirectory + new SimpleDateFormat("yyyy-MM").format(new Date()).concat(".txt");
    }
}


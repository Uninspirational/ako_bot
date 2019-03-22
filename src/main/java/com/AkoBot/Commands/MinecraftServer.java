package com.AkoBot.Commands;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MinecraftServer {
    private OutputStream outputStream = null;
    private Process process = null;
    private String servername = null;

    public MinecraftServer() {
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getServername() {
        return servername;
    }

    public boolean isRunning() {
        return outputStream != null;
    }
    public boolean closeMinecraftServer() {
        try {
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
            printWriter.println("/stop");
            printWriter.flush();
            printWriter.close();
            this.outputStream = null;
            this.process = null;
            this.servername = null;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

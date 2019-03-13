package com.AkoBot.Commands;

public class MillisecondConverter {
    public String millisecondConverter(long time) {
        long seconds = time / 1000;
        int minutes = (int) seconds 	/ 60;
        int hours = minutes / 60;
        int days = hours / 24;
        String result;
        result = String.format("%02d:%02d:%02d:%02d", days, hours % 24, minutes % 60, seconds % 60);
        if (days == 0) {
            result = String.format("%02d:%02d:%02d", hours % 24, minutes % 60, seconds % 60);
        }
        else if (hours == 0) {
            result = String.format("%02d:%02d", minutes % 60, seconds % 60);
        }
        return result;
    }
}

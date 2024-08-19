package com.example.myworkout.Utilities;

public class TimeFormatter {

    public static String formatTime(long currentTimeDelta) {
        int seconds = (int) (currentTimeDelta / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        int hours = minutes / 60;
        minutes %= 60;
        hours %= 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

package com.example.myworkout.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static String formatToDayMonthTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, HH:mm", Locale.US);
        return dateFormat.format(date);
    }

    public static String formatToDayMonthYear(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        return dateFormat.format(date);
    }

    public static String formatCustom(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
        return dateFormat.format(date);
    }
}

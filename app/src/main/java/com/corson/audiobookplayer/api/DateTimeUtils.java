package com.corson.audiobookplayer.api;

import java.util.Date;

public class DateTimeUtils {

    public static Date addHoursToDate(Date date1, int numHours) {
        return new Date(date1.getTime() + numHours * 60 * 60 * 1000);
    }

    public static String getTimestampFromMilli(long milli) {
        int timeInSeconds = (int) (milli / 1000);
        int seconds = timeInSeconds % 60;
        int timeInMinutes = timeInSeconds / 60;
        int minutes = timeInMinutes % 60;
        int hours = timeInMinutes / 60;

        String secondsString = (seconds < 10) ? ("0" + seconds) : seconds + "";
        String minutesString = (minutes < 10) ? ("0" + minutes) : minutes + "";
        String hoursString = (hours < 10) ? ("0" + hours) : hours + "";


        return hoursString + ":" + minutesString + ":" + secondsString;
    }
}

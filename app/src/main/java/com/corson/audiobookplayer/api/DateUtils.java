package com.corson.audiobookplayer.api;

import java.util.Date;

public class DateUtils {

    public static Date addHoursToDate(Date date1, int numHours) {
        return new Date(date1.getTime() + numHours * 60 * 60 * 1000);
    }

}

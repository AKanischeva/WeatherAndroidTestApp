package com.test.weatherapp.common;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String appId = "d61dba5f839d0f84062a029759581b1b";
    public static Location currentLocation = null;
    public static String moscow = "Moscow, ru";
    public static String sPetersburg = "Saint Petersburg, ru";

    public static String convertUnixToDate(String dt) {
        long unixSeconds = Long.valueOf(dt);
        Date date = new java.util.Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-3"));
        return sdf.format(date);
    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }
}

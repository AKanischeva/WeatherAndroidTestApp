package com.test.weatherapp.common;

import android.location.Location;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {

    public static final String APP_ID = "d61dba5f839d0f84062a029759581b1b";
    public static Location currentLocation = null;
    public static String MOSCOW = "Moscow, ru";
    public static String SPETERSBURG = "Saint Petersburg, ru";

    public static String convertUnixToDate(String dt, String tzone) {
        long unixSeconds = Long.valueOf(dt);
        long timeZone = Long.valueOf(tzone);
        Date date = new java.util.Date((unixSeconds + timeZone) * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}

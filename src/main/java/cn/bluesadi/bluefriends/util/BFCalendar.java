package cn.bluesadi.bluefriends.util;

import java.util.Calendar;

/**
 * 日期相关工具类
 * */
public class BFCalendar {

    private static Calendar calendar = Calendar.getInstance();

    public static int getYear(){
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(){
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(){
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(){
        return calendar.get(Calendar.SECOND);
    }

    public static String getDate(String format){
        return format.replaceAll("%year%",String.valueOf(getYear()))
                .replaceAll("%month%",String.valueOf(getMonth()))
                .replaceAll("%day%",String.valueOf(getDay()))
                .replaceAll("%hour%",String.valueOf(getHour()))
                .replaceAll("%minute%",String.valueOf(getMinute()))
                .replaceAll("%second%",String.valueOf(getSecond()));
    }

}

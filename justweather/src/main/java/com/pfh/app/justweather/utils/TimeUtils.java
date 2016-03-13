package com.pfh.app.justweather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TimeUtils
 *
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat MY_FORMAT = new SimpleDateFormat("MM/dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    //"date_y":"2016年02月17日",转换为02/17
    public static String getTimeFromJson(String date_y) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
        Date date = null;
        try {
            date = sdf1.parse(date_y);
            String time = sdf2.format(date);
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return "11/11";
        }
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }


    public static String getMyCurrentTimeInString() {
        return getTime(getCurrentTimeInLong(), MY_FORMAT);
    }

    //拿到显示的格式"11/12"...
    public static List<String> getTimeFromNowToSixDay() {
        List<String> dayAndMothList = new ArrayList<>();
        dayAndMothList.add(getMyCurrentTimeInString());
        for (int i = 1; i < 6; i++) {
            dayAndMothList.add(MY_FORMAT.format(getAddDayDate(i)));
        }
        return dayAndMothList;
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    //增加几天的Date
    public static Date getAddDayDate(int addDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        return calendar.getTime();
    }

    public static int getHour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }


}
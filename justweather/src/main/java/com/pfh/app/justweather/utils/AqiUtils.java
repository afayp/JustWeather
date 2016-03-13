package com.pfh.app.justweather.utils;

/**
 * 根据aqi判断污染程度
 */
public class AqiUtils {

    public static String getLevelText(int aqiValue){
        if(aqiValue <=50){
            return "优";
        }else if(aqiValue <=100){
            return "良";
        }else if(aqiValue <= 150){
            return "轻度污染";
        }else if (aqiValue <= 200){
            return "中度污染";
        }else if(aqiValue <= 300){
            return "重度污染";
        }else{
            return "严重污染";
        }
    }
}

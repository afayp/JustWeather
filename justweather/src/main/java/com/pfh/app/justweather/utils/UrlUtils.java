package com.pfh.app.justweather.utils;


public class UrlUtils {

    private static final String URL_1 = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=";

    private static final String URL_2 = "&imei=e32c8a29d0e8633283737f5d9f381d47&device=HM2013023&miuiVersion=JHBCNBD16.0&modDevice=&source=miuiWeatherApp";


    public static String getUrl(String weatherId){
        return URL_1+weatherId+URL_2;
    }

}

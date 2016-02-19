package com.pfh.app.justweather.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * 城市列表中要显示的city，即用户选择关注的city,暂时先保存下列数据
 */
public class SavedCity extends RealmObject {

    private String cityName;
    private String weatherId;
    //是否为当前要显示的城市
    private boolean isSelected;

    //未来6天温度
    private String temp1;
    private String temp2;
    private String temp3;
    private String temp4;
    private String temp5;
    private String temp6;

    //未来6天天气
    private String weather1;
    private String weather2;
    private String weather3;
    private String weather4;
    private String weather5;
    private String weather6;

    //realtime天气
    private String weather;

    //最新实时温度，对应realtime中的temp
    private String realTemp;

    //上次更新时间，对应realtime中的time
    private String lastUpdateTime;
    //aqi
    private String aqi;
    //pm2.5
    private String pm25;

    public SavedCity() {
    }

    //选择城市里创建
    public SavedCity(String cityName, String weatherId, boolean isSelected) {
        this.cityName = cityName;
        this.weatherId = weatherId;
        this.isSelected = isSelected;
    }

    public String getRealTemp() {
        return realTemp;
    }

    public void setRealTemp(String realTemp) {
        this.realTemp = realTemp;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getTemp3() {
        return temp3;
    }

    public void setTemp3(String temp3) {
        this.temp3 = temp3;
    }

    public String getTemp4() {
        return temp4;
    }

    public void setTemp4(String temp4) {
        this.temp4 = temp4;
    }

    public String getTemp5() {
        return temp5;
    }

    public void setTemp5(String temp5) {
        this.temp5 = temp5;
    }

    public String getTemp6() {
        return temp6;
    }

    public void setTemp6(String temp6) {
        this.temp6 = temp6;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }


    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public String getWeather1() {
        return weather1;
    }

    public void setWeather1(String weather1) {
        this.weather1 = weather1;
    }

    public String getWeather2() {
        return weather2;
    }

    public void setWeather2(String weather2) {
        this.weather2 = weather2;
    }

    public String getWeather3() {
        return weather3;
    }

    public void setWeather3(String weather3) {
        this.weather3 = weather3;
    }

    public String getWeather4() {
        return weather4;
    }

    public void setWeather4(String weather4) {
        this.weather4 = weather4;
    }

    public String getWeather5() {
        return weather5;
    }

    public void setWeather5(String weather5) {
        this.weather5 = weather5;
    }

    public String getWeather6() {
        return weather6;
    }

    public void setWeather6(String weather6) {
        this.weather6 = weather6;
    }


}

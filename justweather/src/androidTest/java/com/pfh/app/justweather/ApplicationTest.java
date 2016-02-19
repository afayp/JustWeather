package com.pfh.app.justweather;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testDB(){
        Context context = getContext();
        ArrayList<String> provinceList = CityQueryDao.getProvinceList(context);
        String result = provinceList.toString();
        Log.d("JustWeather" , result);
    }
}
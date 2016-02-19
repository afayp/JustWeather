package com.pfh.app.justweather.utils;


import com.pfh.app.justweather.model.SavedCity;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmUtils {


    /**
     * 把传入的savedcity.setIsSelected(false)
     * @param realm ,savedCity
     */
    public static void cancelSelected(Realm realm ,SavedCity savedCity){
        SavedCity city= realm.where(SavedCity.class).equalTo("cityName",savedCity.getCityName()).findFirst();
        realm.beginTransaction();
        city.setIsSelected(false);
        realm.commitTransaction();
    }


    /**
     * 把传入的savedcity.setIsSelected(true)
     * @param realm ,savedCity
     */
    public static void SetSelected(Realm realm ,SavedCity savedCity){
        SavedCity city= realm.where(SavedCity.class).equalTo("cityName",savedCity.getCityName()).findFirst();
        realm.beginTransaction();
        city.setIsSelected(true);
        realm.commitTransaction();
    }

    public static void cancelAndSetSelected(Realm realm ,SavedCity cancelCity,SavedCity setCity){
        SavedCity cityCancel= realm.where(SavedCity.class).equalTo("cityName",cancelCity.getCityName()).findFirst();
        SavedCity citySet= realm.where(SavedCity.class).equalTo("cityName",setCity.getCityName()).findFirst();
        realm.beginTransaction();
        cityCancel.setIsSelected(false);
        citySet.setIsSelected(true);
        realm.commitTransaction();
    }

    public static void deleteCity(Realm realm ,SavedCity savedCity){
        realm.beginTransaction();
        SavedCity deleteCity = realm.where(SavedCity.class).equalTo("cityName", savedCity.getCityName()).findFirst();
        deleteCity.removeFromRealm();
        realm.commitTransaction();
    }
}

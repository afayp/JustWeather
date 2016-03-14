package com.pfh.app.justweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.TextHttpResponseHandler;
import com.pfh.app.justweather.model.SavedCity;
import com.pfh.app.justweather.utils.BroadcastUtils;
import com.pfh.app.justweather.utils.HttpUtils;
import com.pfh.app.justweather.utils.L;
import com.pfh.app.justweather.utils.PrefsUtils;
import com.pfh.app.justweather.utils.UrlUtils;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;


public class AutoUpdateService extends Service {

    private Timer timer;
    private TimerTask task;
    private SavedCity savedCity;
    private Realm realm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int autoupdateTime = PrefsUtils.getAutoupdateTime(this);
        L.e("AutoUpdateService中 autoupdateTime = "+ autoupdateTime);

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                updateWeather();
            }
        };
        timer.schedule(task, 0, autoupdateTime * 60*1000);
//        timer.schedule(task, 3000, 5000);
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        L.e("AutoUpdateService oncreate了！");
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
        if(task != null){
            task.cancel();
        }
//        if(realm != null){
//            realm.close();
//        }//close这个就出错！
        L.e("AutoUpdateService 被destory了！");
    }

    private void updateWeather() {
        if(HttpUtils.isConnected(this)){
            savedCity = new SavedCity();
            realm = Realm.getDefaultInstance();
            RealmResults<SavedCity> allSavedCitys = realm.where(SavedCity.class).findAll();
            for (SavedCity c : allSavedCitys) {
                if (c.isSelected()) {
                    savedCity = c;
                }
            }
            Log.e("JustWeather", " Autoupdateservice中查询到的当前城市：" + savedCity.getCityName());
            if(savedCity.getWeatherId() != null){
                //用async-http就会崩溃，不知道为什么
                try {
                    String response = HttpUtils.requestData(UrlUtils.getUrl(savedCity.getWeatherId()));
                    //更新数据库的数据
                    SavedCity newCity = HttpUtils.handleJosnResponse(response);
                    realm.beginTransaction();
                    SavedCity oldcity = realm.where(SavedCity.class).equalTo("cityName", savedCity.getCityName()).findFirst();
                    oldcity.removeFromRealm();
                    realm.copyToRealm(newCity);
                    realm.commitTransaction();
                    BroadcastUtils.sendShowNotificationBroadcast(AutoUpdateService.this);
                    L.e("autoupdateservice中sendbroadcast成功！");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                //当前没有城市
                L.e("autoupdateservice中 一个城市都没有更新个毛线！");
            }
        }else {
            L.e("autoupdateservice中自动更新但是没有网啊");
//            Toast.makeText(AutoUpdateService.this,"自动更新但是没有网啊！",Toast.LENGTH_SHORT).show();//service中不能弹toast？
        }
    }
}

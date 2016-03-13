package com.pfh.app.justweather.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.pfh.app.justweather.R;
import com.pfh.app.justweather.activity.MainActivity;
import com.pfh.app.justweather.model.SavedCity;
import com.pfh.app.justweather.utils.AqiUtils;
import com.pfh.app.justweather.utils.BroadcastUtils;
import com.pfh.app.justweather.utils.L;
import com.pfh.app.justweather.utils.RealmUtils;

import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationService extends Service{

    private NotificationReceiver notificationReceiver;
    public static final String ACTION_SHOW_NOTICATION = "com.pfh.app.justweather.action_show_notication";
    private NotificationManager notificationManager;
    private SavedCity savedCity;
    private Realm realm;


    private class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播更新notification
            if(intent.getBooleanExtra("ISSHOW",true)){
                showNotification();
            }else {
                stopNotification();
            }
        }


    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        notificationReceiver = new NotificationReceiver();
        registerReceiver(notificationReceiver, new IntentFilter(ACTION_SHOW_NOTICATION));
        L.e("service中registerReceiver成功！");
//        BroadcastUtils.sendShowNotificationBroadcast(this);//为什么放这里？因为第一次启动MainActivity中没有城市的时候，广播注册的太晚了
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        showNotification();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(1024);
        if(notificationReceiver != null){
            unregisterReceiver(notificationReceiver);
        }
        if(realm != null){
            realm.close();
        }
    }

    private void showNotification() {
        L.e("service中showNotification 开始！");
        savedCity = new SavedCity();//为什么把这个放到onCreate中，在service已经启动过的情况下，还是会没有初始化，成员变量不是应该一直在吗
        realm = Realm.getDefaultInstance();
        RealmResults<SavedCity> allSavedCitys = realm.where(SavedCity.class).findAll();
        for (SavedCity c : allSavedCitys) {
            if (c.isSelected()) {
                savedCity = c;
            }
        }
        Log.e("JustWeather"," service中查询到的当前城市："+savedCity.getCityName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setShowWhen(false);
        RemoteViews remoteView;
        if(savedCity.getWeatherId() != null){
            L.e("service中showNotification 开始！不为null");
            builder.setSmallIcon(R.mipmap.ic_sun_black);
            remoteView = new RemoteViews(getPackageName(), R.layout.notification_normal);
            remoteView.setImageViewResource(R.id.iv_notifyTypeIcon,R.mipmap.ic_sun_black);//TODO 根据实际情况分类
            remoteView.setTextViewText(R.id.tv_notifyLocation, savedCity.getCityName());
            remoteView.setTextViewText(R.id.tv_notifyTypeAndTempAndAqi,savedCity.getWeather()+"  "+savedCity.getRealTemp()+"℃"+"  "+AqiUtils.getLevelText(Integer.parseInt(savedCity.getAqi())));
            remoteView.setTextViewText(R.id.tv_notifyUpdateTime,savedCity.getLastUpdateTime()+" 发布");
        }else {
            L.e("service中showNotification 开始！为null");
            builder.setSmallIcon(R.mipmap.ic_na_black);
            remoteView = new RemoteViews(getPackageName(), R.layout.notification_nodata);

        }
        builder.setContent(remoteView);
        Intent intent = new Intent(this, MainActivity.class);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1024,builder.build());
        startForeground(1024, builder.build());
        L.e("service中showNotification 结束！");

    }

    private void stopNotification(){
        stopForeground(true);
//        stopService(new Intent(this,NotificationService.class));
    }
}

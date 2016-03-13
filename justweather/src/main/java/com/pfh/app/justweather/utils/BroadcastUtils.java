package com.pfh.app.justweather.utils;


import android.content.Context;
import android.content.Intent;

import com.pfh.app.justweather.service.NotificationService;

public class BroadcastUtils {

    public static void sendShowNotificationBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(NotificationService.ACTION_SHOW_NOTICATION);
        intent.putExtra("ISSHOW",true);
        context.sendBroadcast(intent);
    }
    public static void sendStopNotificationBroadcast(Context context) {
        context.sendBroadcast(new Intent(NotificationService.ACTION_SHOW_NOTICATION).putExtra("ISSHOW",false));
    }
}

package com.pfh.app.justweather.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 把assets下的文件写到sd卡指定位置
 */
public class AssetsUtils {

    //数据库存储路径  
    private static String filePath = "data/data/com.pfh.app.justweather/location.db";

    SQLiteDatabase database;

    public static SQLiteDatabase openDatabase(Context context) {
        File dbFile = new File(filePath);
        //查看数据库文件是否存在  
        if (dbFile.exists()) {
            L.i("JustWeather", "存在数据库location.db");
            //存在则直接返回打开的数据库  
            return SQLiteDatabase.openDatabase(filePath, null, SQLiteDatabase.OPEN_READONLY);
        } else {
            try {
                //得到资源  
                AssetManager am = context.getAssets();
                //得到数据库的输入流  
                InputStream is = am.open("location.db");
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(dbFile);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
                L.i("JustWeather", "创建数据库成功");
            } catch (Exception e) {
                e.printStackTrace();
                L.e("创建数据库失败");
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了  
            return openDatabase(context);
        }
    }
}

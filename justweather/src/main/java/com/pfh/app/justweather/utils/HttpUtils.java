package com.pfh.app.justweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;
import com.pfh.app.justweather.model.SavedCity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class HttpUtils {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url,ResponseHandlerInterface handlerInterface){
        client.get(url,handlerInterface);
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context)
    {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity)
        {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected())
            {
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static SavedCity handleJosnResponse(String response){
        SavedCity savedCity = new SavedCity();
        try {
            JSONObject jsonObject = new JSONObject(response);
            //forecast
            JSONObject forecastJsonObject  = (JSONObject) jsonObject.get("forecast");
            savedCity.setCityName((String) forecastJsonObject.get("city"));
            savedCity.setWeatherId((String) forecastJsonObject.get("cityid"));
            savedCity.setIsSelected(true);
            savedCity.setTemp1((String) forecastJsonObject.get("temp1"));
            savedCity.setTemp2((String) forecastJsonObject.get("temp2"));
            savedCity.setTemp3((String) forecastJsonObject.get("temp3"));
            savedCity.setTemp4((String) forecastJsonObject.get("temp4"));
            savedCity.setTemp5((String) forecastJsonObject.get("temp5"));
            savedCity.setTemp6((String) forecastJsonObject.get("temp6"));
            savedCity.setWeather1((String) forecastJsonObject.get("weather1"));
            savedCity.setWeather2((String) forecastJsonObject.get("weather2"));
            savedCity.setWeather3((String) forecastJsonObject.get("weather3"));
            savedCity.setWeather4((String) forecastJsonObject.get("weather4"));
            savedCity.setWeather5((String) forecastJsonObject.get("weather5"));
            savedCity.setWeather6((String) forecastJsonObject.get("weather6"));
            //realtime
            JSONObject realtimeJsonObject = (JSONObject) jsonObject.get("realtime");
            savedCity.setWeather((String) realtimeJsonObject.get("weather"));
            savedCity.setRealTemp((String) realtimeJsonObject.get("temp"));
            savedCity.setLastUpdateTime((String) realtimeJsonObject.get("time"));

            //aqi
            JSONObject aqiJsonObject = (JSONObject) jsonObject.get("aqi");
            savedCity.setAqi((String) aqiJsonObject.get("aqi"));
            savedCity.setPm25((String) aqiJsonObject.get("pm25"));
            savedCity.setPm10((String) aqiJsonObject.get("pm10"));

            return savedCity;
        } catch (JSONException e) {
            e.printStackTrace();
            L.e("json解析出错拉！");
            return savedCity;
        }
    }

    public static String requestData(String address) throws Exception {
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        String data = null;
        InputStream is = null;
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            data = readFromStream(is);
        }
        if (is != null) {
            is.close();
        }
        return data;
    }

    /**
     * @param is 输入流
     * @return 返回流中获取的字符串
     * @throws IOException
     */
    public static String readFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            bos.write(buff, 0, len);
        }
        is.close();
        String result = bos.toString();
        bos.close();
        return result;
    }
}

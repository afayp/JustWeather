package com.pfh.app.justweather.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pfh.app.justweather.R;
import com.pfh.app.justweather.model.SavedCity;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter{

    private Context mContext;
    private List<SavedCity> mCityList;

    public DrawerListAdapter(Context context,List<SavedCity> cityList){
        mContext = context;
        mCityList = cityList;
    }

    public void refreshData(List<SavedCity> cities){
        mCityList.clear();
        mCityList = cities;
        notifyDataSetChanged();
    }




    @Override
    public int getCount() {
        return mCityList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SavedCity savedCity = mCityList.get(position);
        if(mCityList.size() > 0){
            MyViewHolder holder;
            if (convertView == null){
                holder = new MyViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_drawer_list,null);
                holder.tv_city = (TextView) convertView.findViewById(R.id.tv_item);
                holder.tv_temp = (TextView) convertView.findViewById(R.id.item_temp);
                holder.isSelected = (ImageView) convertView.findViewById(R.id.item_is_selected);
                holder.weather = (ImageView) convertView.findViewById(R.id.item_weather);
                convertView.setTag(holder);
            }else {
                holder = (MyViewHolder) convertView.getTag();
            }
            holder.tv_city.setText(savedCity.getCityName());
            if(savedCity.isSelected()){
                holder.isSelected.setVisibility(View.VISIBLE);
            }
            if(savedCity.getRealTemp() != null){
                holder.tv_temp.setText(savedCity.getRealTemp()+"°");
                holder.tv_temp.setVisibility(View.VISIBLE);
            }
            if(savedCity.getWeather() != null){
//            holder.weather.setImageResource();
                //TODO 根据weather设置图标。。。
                holder.weather.setVisibility(View.VISIBLE);
            }
            return convertView;
        }else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_drawer_list,null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_item);
            tv.setText("空空如也...");
            tv.setTextColor(Color.BLUE);
            return convertView;
        }

    }


    class MyViewHolder{
        TextView tv_city;
        ImageView isSelected;
        TextView tv_temp;
        ImageView weather;
    }
}

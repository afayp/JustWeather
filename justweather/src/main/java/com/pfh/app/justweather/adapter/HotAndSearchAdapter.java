package com.pfh.app.justweather.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pfh.app.justweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 热门城市和搜索结果显示的adapter
 */

public class HotAndSearchAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mCityList;

    public HotAndSearchAdapter(Context context,List<String> list){
        mContext = context;
        mCityList = list;
    }


    public void refreshData(List<String> newDataList){
        mCityList.clear();
        mCityList = newDataList;
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
        MyViewHoder viewHoder;
        if(convertView == null){
            viewHoder = new MyViewHoder();
            convertView =LayoutInflater.from(mContext).inflate(R.layout.item_city,null,false);
            viewHoder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHoder);
        }else {
            viewHoder = (MyViewHoder) convertView.getTag();
        }
        viewHoder.tv.setText(mCityList.get(position));
        return convertView;
    }


    class MyViewHoder{
        TextView tv;
    }
}

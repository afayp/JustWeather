package com.pfh.app.justweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pfh.app.justweather.R;

import java.util.List;

/**
 * 根据省市选择的adapter
 */
public class SelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mCityList;
    
    public SelectAdapter(Context context,List<String> mCityList){
        mContext = context;
        this.mCityList = mCityList;
        
    }
    
    public void refreshData(List<String> newData){
        mCityList.clear();
        mCityList = newData;
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
        MyViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city,null);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.tv.setText(mCityList.get(position));
        return convertView;
    }
    
    class MyViewHolder{
        TextView tv;
    }
}

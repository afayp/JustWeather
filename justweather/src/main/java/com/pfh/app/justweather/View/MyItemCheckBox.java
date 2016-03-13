package com.pfh.app.justweather.View;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.app.justweather.R;

public class MyItemCheckBox extends RelativeLayout{

    private TextView tv_title;
    private TextView tv_desc;
    private CheckBox checkBox;
    private String title;
    private String desc_on;
    private String desc_off;

    public MyItemCheckBox(Context context) {
        this(context, null, 0);
    }

    public MyItemCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyItemCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.myitemcheckbox_layout, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        checkBox = (CheckBox) view.findViewById(R.id.cb_checkbox);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView, defStyleAttr, 0);
        title = (String) typedArray.getText(R.styleable.MyCustomView_itemcheckbox_title);
        desc_on = (String) typedArray.getText(R.styleable.MyCustomView_itemcheckbox_desc_on);
        desc_off = (String) typedArray.getText(R.styleable.MyCustomView_itemcheckbox_desc_off);
        tv_title.setText(title);
        tv_desc.setText(desc_on);

        typedArray.recycle();
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }

    public void setChecked(boolean isChecked){
        checkBox.setChecked(isChecked);
        if(isChecked){
            tv_desc.setText(desc_on);
        }else {
            tv_desc.setText(desc_off);
        }

    }

    public void changeCheckedState(){
        if(checkBox.isChecked()){
            checkBox.setChecked(false);
            setChecked(false);
        }else {
            checkBox.setChecked(true);
            setChecked(true);
        }
    }

    public void setDesc(String desc){
        tv_desc.setText(desc);

    }

}

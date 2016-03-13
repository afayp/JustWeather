package com.pfh.app.justweather.View;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pfh.app.justweather.R;

public class MyItemSelectView extends RelativeLayout{

    private TextView tv_title;
    private TextView tv_desc;
    private String title;
    private String desc;

    public MyItemSelectView(Context context) {
        this(context, null, 0);
    }

    public MyItemSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyItemSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.myitemselectview_layout, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCustomView, defStyleAttr, 0);
        title = (String) typedArray.getText(R.styleable.MyCustomView_itemselectview_title);
        desc = (String) typedArray.getText(R.styleable.MyCustomView_itemselectview_desc);
        tv_title.setText(title);
        tv_desc.setText(desc);
        typedArray.recycle();
    }

    public void setDesc(String text){
        tv_desc.setText(text);
    }


}

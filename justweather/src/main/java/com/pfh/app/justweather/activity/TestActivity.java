package com.pfh.app.justweather.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuView;
import com.pfh.app.justweather.R;
import com.pfh.app.justweather.adapter.DrawerListAdapter;
import com.pfh.app.justweather.model.SavedCity;
import com.pfh.app.justweather.utils.L;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.drawer_listview);

        List<SavedCity> cities  = new ArrayList<>();
        cities.add(new SavedCity("东阳", "1111", true));
        DrawerListAdapter drawerListAdapter = new DrawerListAdapter(TestActivity.this,cities);
        L.e("创建creator");
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(TestActivity.this);
                openItem.setBackground(R.color.openItem);
                openItem.setWidth(20);
                openItem.setTitle("×");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.BLACK);
                menu.addMenuItem(openItem);
            }
        };
        swipeMenuListView.setAdapter(drawerListAdapter);
        L.e("设置adapter完毕");
        swipeMenuListView.setMenuCreator(creator);
        L.e("设置creator完毕");

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                }
                return false;
            }
        });
        swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
//        swipeMenuListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(TestActivity.this,"dian l ",Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

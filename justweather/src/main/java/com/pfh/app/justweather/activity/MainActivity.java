package com.pfh.app.justweather.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pfh.app.justweather.R;
import com.pfh.app.justweather.adapter.DrawerListAdapter;
import com.pfh.app.justweather.model.SavedCity;
import com.pfh.app.justweather.utils.DensityUtils;
import com.pfh.app.justweather.utils.HttpUtils;
import com.pfh.app.justweather.utils.L;
import com.pfh.app.justweather.utils.RealmUtils;
import com.pfh.app.justweather.utils.TimeUtils;
import com.pfh.app.justweather.utils.UrlUtils;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_temp;
    private TextView tv_city;
    private TextView tv_weatehr;
    private ImageView iv_aqi;
    private ImageView iv_setting;
    private SwipeMenuListView swipeMenuListView;
    //0为关闭，1为打开
    private static int currentDrawerState = 0;
    private SwipeRefreshLayout drawerSwipeRefreshLayout;

    private SavedCity mCurrentCity = new SavedCity();
    private static final String ACTION_TIME_CHANGED = Intent.ACTION_TIME_CHANGED;

    private Realm realm;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==1){
                initDataToView();
                setUpDrawer();
            }
            super.handleMessage(msg);
        }
    };
    private ImageView iv_about;
    private ImageView iv_add;
    private LineChartView lineChartView;
    private DrawerListAdapter drawerListAdapter;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        initView();
        initData();
        checkTime();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkTime() {
        L.e("现在时间："+TimeUtils.getHour());
        if(TimeUtils.getHour() >= 17){
            relativeLayout.setBackground(getResources().getDrawable(R.mipmap.night_bg));
        }
    }

    private void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.bg);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelayout);
        tv_temp = (TextView) findViewById(R.id.real_temp);
        tv_city = (TextView) findViewById(R.id.real_city);
        tv_weatehr = (TextView) findViewById(R.id.real_weather);
        iv_aqi = (ImageView) findViewById(R.id.aqi);
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.drawer_listview);
        iv_add = (ImageView) findViewById(R.id.drawer_manage);
        iv_setting = (ImageView) findViewById(R.id.drawer_settings);
        iv_about = (ImageView) findViewById(R.id.drawer_about);
        drawerSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.drawer_swipe);
        lineChartView = (LineChartView) findViewById(R.id.chart);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.menu2);
        ab.setDisplayHomeAsUpEnabled(true);
//        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                currentDrawerState = 1;
//                super.onDrawerOpened(drawerView);
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                currentDrawerState = 0;
//                super.onDrawerClosed(drawerView);
//            }
//        });
        setUpSwipe();

//        setUpDrawer();
        mSwipeRefreshLayout.setEnabled(false);


    }

    private void setUpSwipe() {
        //主界面下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mianRefresh();

            }
        });
        //drawer的下拉刷新
        drawerSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drawerSwipeRefreshLayout.setRefreshing(true);
                drawerRefresh();

            }
        });
    }

    private void mianRefresh() {
        initData();

    }

    private void drawerRefresh() {
        setUpDrawer();
    }


    //初始化drawer,从数据库里读取城市列表;
    //TODO 处理滑动冲突
    private void setUpDrawer() {
        RealmResults<SavedCity> allSavedCitys = realm.where(SavedCity.class).findAll();
        allSavedCitys.sort("isSelected", Sort.DESCENDING);//把选中的当前城市排第一个
        final List<SavedCity> cities = new ArrayList<>();
        for (SavedCity c : allSavedCitys){
            cities.add(c);
        }
//        realm.close();为什么close就崩了。。。
//        List<SavedCity> cities = new ArrayList<>();
//        cities.add(new SavedCity("东阳","111",true));
        drawerListAdapter = new DrawerListAdapter(MainActivity.this, cities);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(MainActivity.this);
                openItem.setBackground(R.color.openItem);
                openItem.setWidth(250);
                openItem.setTitle("×");
                openItem.setTitleSize(30);
                openItem.setTitleColor(Color.BLACK);
                menu.addMenuItem(openItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setAdapter(drawerListAdapter);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        SavedCity city = (SavedCity) drawerListAdapter.getItem(position);
//                        Toast.makeText(MainActivity.this, "滑动了" + city.getCityName()+"mcurrentCity:"+mCurrentCity.getCityName(), Toast.LENGTH_LONG).show();
                        //删除数据库对应城市，删除前先判断是否是当前城市，如果不是直接删;如果是把下一个savedCity.setSelected(true),mCurrentCity换一个
                        if (mCurrentCity.getCityName().equals(city.getCityName())) {
                            RealmResults<SavedCity> all = realm.where(SavedCity.class).findAll();
                            L.e("all.size():"+all.size());
                            if(all.size() > 1){
                                //如果还有其他的，要把其他一个设为selected
                                for (int i = all.size() - 1; i >= 0; i--) {
                                    if (all.get(i).getCityName().equals(city.getCityName())) {
                                        if(i-1 >= 0){
                                            RealmUtils.SetSelected(realm,all.get(i-1));
                                        }else {
                                            RealmUtils.SetSelected(realm, all.get(i + 1));
                                        }
                                    }
                                }
                            }
                            RealmUtils.deleteCity(realm, city);
                            mCurrentCity = new SavedCity();
                            initData();
                        } else {
                            //如果删除的不是当前城市，只需更新drawer
                            RealmUtils.deleteCity(realm, city);
                            setUpDrawer();
                        }
                        break;
                }
                return false;
            }
        });
        swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的city设为当前城市
                SavedCity clickedCity = (SavedCity) drawerListAdapter.getItem(position);
                RealmUtils.SetSelected(realm, clickedCity);
                //把当前的city设为不选中
                RealmUtils.cancelSelected(realm, mCurrentCity);
//                RealmUtils.cancelAndSetSelected(realm,mCurrentCity,clickedCity);
                initData();
                mDrawerLayout.closeDrawer(GravityCompat.START);

            }
        });
//        选择添加城市activity
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开添加城市activity
                Intent intent = new Intent(MainActivity.this, CityChooseActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO　打开设置activity

            }
        });
        iv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                showAboutWindow();
            }
        });
        drawerSwipeRefreshLayout.setRefreshing(false);
    }

    private void initData() {
        RealmResults<SavedCity> allSavedCitys = realm.where(SavedCity.class).findAll();
        for (SavedCity c : allSavedCitys){
            if(c.isSelected()){
                mCurrentCity = c;
            }
        }
//        有网就从网上加载，没网就本地读取
        if (HttpUtils.isConnected(MainActivity.this)) {
            if (mCurrentCity.getWeatherId() != null) {
                HttpUtils.get(UrlUtils.getUrl(mCurrentCity.getWeatherId()), new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(MainActivity.this, "请检查网络设置哟", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        mCurrentCity = HttpUtils.handleJosnResponse(responseString);
                        realm.beginTransaction();
                        SavedCity oldcity = realm.where(SavedCity.class).equalTo("cityName", mCurrentCity.getCityName()).findFirst();
                        oldcity.removeFromRealm();
                        realm.copyToRealm(mCurrentCity);
                        realm.commitTransaction();

                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });

            } else {
                //第一次进来，数据库还没城市
                Toast.makeText(MainActivity.this, "快去添加城市吧！", Toast.LENGTH_SHORT).show();
                setUpDrawer();
                initDataToView();
            }

        } else {
            Toast.makeText(MainActivity.this, "当前没有网络，数据可能过期！", Toast.LENGTH_SHORT).show();
            setUpDrawer();
            initDataToView();
        }

    }

    private void initDataToView() {

        if(mCurrentCity.getCityName() != null){
            tv_city.setText(mCurrentCity.getCityName());
            tv_temp.setText(mCurrentCity.getRealTemp() + "°");
            tv_weatehr.setText(mCurrentCity.getWeather());
            L.e(mCurrentCity.getCityName() + "  " + mCurrentCity.getWeatherId());
            List<String> tempList = new ArrayList<>();
            tempList.add(mCurrentCity.getTemp1());
            tempList.add(mCurrentCity.getTemp2());
            tempList.add(mCurrentCity.getTemp3());
            tempList.add(mCurrentCity.getTemp4());
            tempList.add(mCurrentCity.getTemp5());
            tempList.add(mCurrentCity.getTemp6());
            L.e(tempList.toString());
            showChart(handleTemps(tempList));
        }else {
            tv_city.setText("宇宙");
            tv_temp.setText("-273" + "°");
            tv_weatehr.setText("外太空");
            List<Integer> fuckTempList = new ArrayList<>();
            for(int i=0;i<12;i++){
                fuckTempList.add(0);
            }
            showChart(fuckTempList);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
//        initData();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    if(data.getBooleanExtra("hasSelected",false)){
                        initData();
                    }

                }
        }

    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int drawerX = DensityUtils.dp2px(MainActivity.this, 310);
//        float x = event.getX();
//        if(x < drawerX && currentDrawerState == 1){
////            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//            mDrawerLayout.openDrawer(GravityCompat.START);
////            mDrawerLayout.setEnabled(false);
////            return false;
//        }else {
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//
////            return false;
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.refresh:
                mSwipeRefreshLayout.setRefreshing(true);
                initData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param tempList
     */
    private void showChart(List<Integer> tempList) {
        //两组坐标点：今后5天的最高温和最低温
        List<PointValue> valuesUp = new ArrayList<PointValue>();
        List<PointValue> valuesDown = new ArrayList<PointValue>();
        for (int i = 0; i < 10; i = i + 2) {
            valuesUp.add(new PointValue(i / 2, getMinY(tempList)));
        }
        for (int i = 1; i < 10; i = i + 2) {
            valuesDown.add(new PointValue((i - 1) / 2, getMinY(tempList)));
        }
        Line lineUp = new Line(valuesUp).setColor(getResources().getColor(R.color.linecharup)).setCubic(true);
        Line lineDown = new Line(valuesDown).setColor(getResources().getColor(R.color.linechardown)).setCubic(true);
//        lineUp.setShape(ValueShape.CIRCLE);
//        lineDown.setShape(ValueShape.CIRCLE);
//        line.setHasPoints(false);去掉坐标的圆点
        lineUp.setPointRadius(3);//圆点半径
        lineDown.setPointRadius(3);
        lineUp.setStrokeWidth(2);
        lineDown.setStrokeWidth(2);
//        lineUp.setHasLabelsOnlyForSelected(true);
//        lineDown.setHasLabelsOnlyForSelected(true);
        lineUp.setHasLabels(true);//显示坐标数据
        lineDown.setHasLabels(true);
//        line.setAreaTransparency(0);
        List<Line> lines = new ArrayList<Line>();
        //确定几条线
        lines.add(lineUp);
        lines.add(lineDown);

        LineChartData data = new LineChartData(lines);
//        data.setLines(lines);
        //坐标轴:
        Axis axisX = new Axis(); //X轴
//        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);
        axisX.setHasSeparationLine(false);//去掉分割线
//        axisX.setName("采集时间");
//        axisX.setAutoGenerated(true);
//        axisX.setMaxLabelChars(2);
//        AxisAutoValues axisAutoValues = new AxisAutoValues();
        List<String> timeList = TimeUtils.getTimeFromNowToSixDay();
        List<AxisValue> axisValues = new ArrayList<>();
//        axisValues.add(new AxisValue(-1).setLabel(" "));//空出一格
        axisValues.add(new AxisValue(0).setLabel("今天"));
        axisValues.add(new AxisValue(1).setLabel("明天"));
        axisValues.add(new AxisValue(2).setLabel(timeList.get(2)));
        axisValues.add(new AxisValue(3).setLabel(timeList.get(3)));
        axisValues.add(new AxisValue(4).setLabel(timeList.get(4)));
//        axisValues.add(new AxisValue(5).setLabel(timeList.get(5)));
        axisX.setValues(axisValues);
        data.setAxisXBottom(axisX);//axisX.setHasLines(true)
        // Y
        Axis axisY = new Axis();
        axisY.setHasSeparationLine(false);
//        axisY.setLineColor(Color.TRANSPARENT);
//        axisY.setTextColor(Color.BLUE);
//        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
        List<AxisValue> axisYValues = new ArrayList<>();
        axisYValues.add(new AxisValue(-10).setLabel("-10°"));
        axisYValues.add(new AxisValue(0).setLabel("0°"));
        axisYValues.add(new AxisValue(10).setLabel("10°"));
        axisYValues.add(new AxisValue(20).setLabel("20°"));
        axisYValues.add(new AxisValue(30).setLabel("30°"));
        axisYValues.add(new AxisValue(40).setLabel("40°"));
        axisYValues.add(new AxisValue(50).setLabel("50°"));
        axisY.setValues(axisYValues);
        data.setAxisYLeft(axisY);
        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.startDataAnimation(2000);
        lineChartView.setInteractive(false);
        lineChartView.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);

        lineChartView.setViewportCalculationEnabled(false);
        int maxY = getMaxY(tempList);
        int minY = getMinY(tempList);
        Viewport v = new Viewport(0, maxY + 8, 4.2f, minY - 1.5f);
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);

        Line newLineUp  = data.getLines().get(0).setColor(getResources().getColor(R.color.linecharup)).setCubic(true);
        Line newLineDown = data.getLines().get(1).setColor(getResources().getColor(R.color.linechardown)).setCubic(true);
        List<Integer> upList = new ArrayList<>();
        List<Integer> downList = new ArrayList<>();
        for(int i =0;i <tempList.size();i++){
            if(i%2 == 0){
                upList.add(tempList.get(i));
            }else{
                downList.add(tempList.get(i));
            }
        }
        for(PointValue value :newLineUp.getValues()){
            value.setTarget(value.getX(),upList.get((int)value.getX()));
        }
        for (PointValue value :newLineDown.getValues()){
            value.setTarget(value.getX(),downList.get((int) value.getX()));
        }
        lineChartView.startDataAnimation(1500);
    }

    private int getMaxY(List<Integer> tempList) {
        int tempMax = tempList.get(0);
        for (int i = 2; i < 12; i = i + 2) {
            if (tempList.get(i) > tempMax) {
                tempMax = tempList.get(i);
            }
        }
        return tempMax;
    }

    private int getMinY(List<Integer> tempList) {
        int tempMin = tempList.get(1);
        for (int i = 3; i < 12; i = i + 2) {
            if (tempList.get(i) < tempMin) {
                tempMin = tempList.get(i);
            }
        }
        return tempMin;
    }

    /**
     * @param temps:"temp1":"10℃~0℃", "temp2":"11℃~1℃",
     *                                "temp3":"10℃~-1℃",
     *                                "temp4":"8℃~-3℃",
     *                                "temp5":"9℃~-2℃",
     *                                "temp6":"0℃~0℃",
     */
    private List<Integer> handleTemps(List<String> temps) {
        List<Integer> tempList = new ArrayList<>();
        for (String temp : temps) {
            String[] singleTemp = temp.split("~");
            tempList.add(Integer.parseInt(singleTemp[0].replace("℃", "")));
            tempList.add(Integer.parseInt(singleTemp[1].replace("℃", "")));
        }
        L.d("今后6天的最高温和最低温:" + tempList.toString());
        return tempList;
    }

    private void showAboutWindow() {
        View aboutView = LayoutInflater.from(MainActivity.this).inflate(R.layout.about_layout,null);
        TextView tv_dizhi = (TextView) aboutView.findViewById(R.id.github_dizhi);
        TextView tv_author = (TextView) aboutView.findViewById(R.id.github_author);
        tv_dizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/afayp/JustWeather");
                Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        tv_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/afayp/JustWeather");
                Intent intent  = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        PopupWindow popupWindow = new PopupWindow(aboutView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.about_bg)));
        popupWindow.showAtLocation(mDrawerLayout, Gravity.CENTER, 0, 0);
    }


    @Override
    protected void onDestroy() {
        if(realm != null){
            realm.close();
        }
        super.onDestroy();
    }

}

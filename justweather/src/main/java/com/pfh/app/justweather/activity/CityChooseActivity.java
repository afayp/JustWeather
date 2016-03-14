package com.pfh.app.justweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pfh.app.justweather.R;
import com.pfh.app.justweather.adapter.HotAndSearchAdapter;
import com.pfh.app.justweather.adapter.SelectAdapter;
import com.pfh.app.justweather.db.CityQueryDao;
import com.pfh.app.justweather.model.SavedCity;
import com.pfh.app.justweather.utils.KeyboardUtils;
import com.pfh.app.justweather.utils.L;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class CityChooseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    //键入城市名搜索，要监听其变化
    private EditText et_search;

    //输入城市名后，要从热门城市变成搜索结果
    private TextView tv_list_state;

    //点击按省市选择
    private TextView tv_allCity;

    //显示热门城市和搜索结果
    private ListView lv_location_search;

    //显示按省市查询
    private ListView lv_location;

    //点击按省市选择要隐藏
    private LinearLayout ll_search;

    //进入按省市选择界面，从搜索城市变成添加城市
    private TextView tv_toolbar;
    /**
     * 省级
     */
    private final int LEVEL_PROVINCE = 1;
    /**
     * 市级
     */
    private final int LEVEL_CITY = 2;
    /**
     * 县级
     */
    private final int LEVEL_AREA = 3;
    /**
     * 搜索城市页面
     */
    private final int VIEW_SEARCH_CITY = 4;
    /**
     * 根据省市选择城市页面
     */
    private final int VIEW_SELECT_CITY = 5;

    /**
     * 保存当前根据省市选择的listview显示的层级，默认为省级
     */
    private int currentLevel = LEVEL_PROVINCE;
    /**
     * 当前是搜索城市界面还是根据省市选择界面，默认为搜索城市界面
     */
    private int currentView = VIEW_SEARCH_CITY;


    /**
     * 保存选择的当前省份
     */
    private String currentProvince;
    /**
     * 保存选择的当前市
     */
    private String currentCity;

    //保存热门城市和搜索结果的list
    private List<String> hotAndSearchCityList = new ArrayList<>();

    //保存按省市选择的List
    private List<String> selectCityList = new ArrayList<>();

    private HotAndSearchAdapter hotAndSearchAdapter;
    private SelectAdapter selectAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_list_state = (TextView) findViewById(R.id.tv_listState);
        tv_allCity = (TextView) findViewById(R.id.tv_allCity);
        lv_location_search = (ListView) findViewById(R.id.lv_location_search);
        lv_location = (ListView) findViewById(R.id.lv_location);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_toolbar = (TextView) findViewById(R.id.tv_toolbar);

        //初始化toolbar
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * 监听edittext,根据键入值改变显示
         */
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String result = s.toString().trim();
                if (!TextUtils.isEmpty(result)) {
                    tv_list_state.setText("搜索结果");
                    hotAndSearchCityList = CityQueryDao.searchByKeyWord(CityChooseActivity.this, result);
                } else {
                    //删除了键入的值后要还原显示热门城市
                    tv_list_state.setText("热门城市");
                    initHotCity();
                    //TODO 传数据过去不能正确给mCityList赋值？？
                }
                hotAndSearchAdapter.refreshData(hotAndSearchCityList);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /**
         * 初始化热门城市显示
         */
        initHotCity();
        hotAndSearchAdapter = new HotAndSearchAdapter(CityChooseActivity.this, hotAndSearchCityList);
        lv_location_search.setAdapter(hotAndSearchAdapter);
        /**
         * 监听lv_location_search
         */
        lv_location_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCityName = (String) parent.getAdapter().getItem(position);
                saveSelectedCity(selectedCityName);
                Intent intent = new Intent();
                intent.putExtra("hasSelected",true);
                setResult(RESULT_OK, intent);
                finish();
                L.e("选择了" +selectedCityName);
            }
        });
        /**
         * 滑动时隐藏键盘
         */
        lv_location_search.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    KeyboardUtils.hintKeyBoard(CityChooseActivity.this);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        /**
         *  点击显示根据省市选择界面
         */
        tv_allCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = VIEW_SELECT_CITY;
                tv_toolbar.setText("添加城市");
                lv_location.setVisibility(View.VISIBLE);
                ll_search.setVisibility(View.INVISIBLE);
                KeyboardUtils.hintKeyBoard(CityChooseActivity.this);
            }
        });
        /**
         * 初始化lv_location
         */
        selectCityList = CityQueryDao.getProvinceList(CityChooseActivity.this);
        currentLevel = LEVEL_PROVINCE;
        selectAdapter = new SelectAdapter(CityChooseActivity.this, selectCityList);
        lv_location.setAdapter(selectAdapter);

        /**
         * 监听根据省市选择的listview
         */
        lv_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    currentProvince = (String) parent.getAdapter().getItem(position);
                    tv_toolbar.setText(currentProvince);
                    selectCityList = CityQueryDao.getCityListByProvince(CityChooseActivity.this, currentProvince);
                    selectAdapter.refreshData(selectCityList);
                    currentLevel = LEVEL_CITY;
                } else if (currentLevel == LEVEL_CITY) {
                    currentCity = (String) parent.getAdapter().getItem(position);
                    tv_toolbar.setText(currentCity);
                    selectCityList = CityQueryDao.getAreaListByCity(CityChooseActivity.this, currentCity);
                    selectAdapter.refreshData(selectCityList);
                    currentLevel = LEVEL_AREA;
                } else {
                    String selectedCityName = (String) parent.getAdapter().getItem(position);
                    saveSelectedCity(selectedCityName);
                    //通知drawer刷新数据
                    Intent intent = new Intent();
                    intent.putExtra("hasSelected",true);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                lv_location.setSelection(0);
            }
        });

    }

    //保存到本地,首先把以前选中的city的selected设为false
    private void saveSelectedCity(String selectedCityName) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SavedCity> cityListByName = realm.where(SavedCity.class).equalTo("cityName", selectedCityName).findAll();
        if(cityListByName.size() > 0){
            return;
        }
        String weatherId = CityQueryDao.getWeatherIdByAreaName(CityChooseActivity.this, selectedCityName);
        SavedCity savedCity = new SavedCity(selectedCityName, weatherId, true);
        RealmResults<SavedCity> savedCitys = realm.where(SavedCity.class).equalTo("isSelected", true).findAll();
        realm.beginTransaction();
        for(int i = savedCitys.size() -1; i >=0; i--){
            SavedCity city = savedCitys.get(i);
            city.setIsSelected(false);
        }
        realm.commitTransaction();

        realm.beginTransaction();
        realm.copyToRealm(savedCity);
        realm.commitTransaction();
        realm.close();
    }


    @Override
    public void onBackPressed() {
        if (currentView == VIEW_SELECT_CITY) {
            if (currentLevel == LEVEL_PROVINCE) {
                tv_toolbar.setText("搜索城市");
                lv_location.setVisibility(View.INVISIBLE);
                ll_search.setVisibility(View.VISIBLE);
                currentView = VIEW_SEARCH_CITY;
            } else if (currentLevel == LEVEL_CITY) {
                tv_toolbar.setText("添加城市");
                selectCityList = CityQueryDao.getProvinceList(CityChooseActivity.this);
                selectAdapter.refreshData(selectCityList);
                currentLevel = LEVEL_PROVINCE;
            } else if (currentLevel == LEVEL_AREA) {
                tv_toolbar.setText(currentProvince);
                selectCityList = CityQueryDao.getCityListByProvince(CityChooseActivity.this, currentProvince);
                selectAdapter.refreshData(selectCityList);
                currentLevel = LEVEL_CITY;
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        删除所有数据，方便调试
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<SavedCity> results = realm.where(SavedCity.class).findAll();
//        realm.beginTransaction();
//        results.clear();
//        realm.commitTransaction();
//        realm.close();
    }

    /**
     * 初始化热门城市
     */
    private void initHotCity() {
        hotAndSearchCityList.clear();
        hotAndSearchCityList.add("北京");
        hotAndSearchCityList.add("上海");
        hotAndSearchCityList.add("广州");
        hotAndSearchCityList.add("杭州");
        hotAndSearchCityList.add("深圳");
        hotAndSearchCityList.add("武汉");
        hotAndSearchCityList.add("重庆");
        hotAndSearchCityList.add("南京");
        hotAndSearchCityList.add("苏州");
        hotAndSearchCityList.add("西安");
        hotAndSearchCityList.add("成都");
        hotAndSearchCityList.add("石家庄");
        hotAndSearchCityList.add("长沙");
        hotAndSearchCityList.add("南宁");
        hotAndSearchCityList.add("昆明");
    }
}

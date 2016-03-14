package com.pfh.app.justweather.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pfh.app.justweather.R;
import com.pfh.app.justweather.View.MyItemCheckBox;
import com.pfh.app.justweather.View.MyItemSelectView;
import com.pfh.app.justweather.service.AutoUpdateService;
import com.pfh.app.justweather.service.NotificationService;
import com.pfh.app.justweather.utils.BroadcastUtils;
import com.pfh.app.justweather.utils.L;
import com.pfh.app.justweather.utils.PrefsUtils;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends AppCompatActivity{

    private MyItemCheckBox item_notification;
    private Toolbar mToolbar;
    private MyItemCheckBox item_is_update;
    private MyItemSelectView item_update_time;
    private MaterialDialog materialDialog;
    private int autoupdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        item_notification = (MyItemCheckBox) findViewById(R.id.item_notification);
        item_is_update = (MyItemCheckBox) findViewById(R.id.item_is_update);
        item_update_time = (MyItemSelectView) findViewById(R.id.item_update_time);

        mToolbar.setTitle("设置");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        item_notification.setChecked(PrefsUtils.isShowNotification(this));
        item_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_notification.isChecked()) {
                    stopService(new Intent(SettingActivity.this, NotificationService.class));
                    item_notification.setChecked(false);
                    PrefsUtils.changeNotificationState(SettingActivity.this, false);
                } else {
                    startService(new Intent(SettingActivity.this, NotificationService.class));
                    item_notification.setChecked(true);
                    PrefsUtils.changeNotificationState(SettingActivity.this, true);

                }
            }
        });

        boolean isAutoUpdate = PrefsUtils.isAutoUpdate(this);
        item_is_update.setChecked(isAutoUpdate);
        item_is_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_is_update.isChecked()) {
                    item_update_time.setVisibility(View.INVISIBLE);
                    stopService(new Intent(SettingActivity.this, AutoUpdateService.class));
                    item_is_update.setChecked(false);
                    PrefsUtils.changeAutoUpdateState(SettingActivity.this, false);
                } else {
                    startService(new Intent(SettingActivity.this, AutoUpdateService.class));
                    item_is_update.setChecked(true);
                    PrefsUtils.changeAutoUpdateState(SettingActivity.this, true);
                    item_update_time.setVisibility(View.VISIBLE);
                }
            }
        });


        if(!isAutoUpdate){
            item_update_time.setVisibility(View.INVISIBLE);
        }
        autoupdateTime = PrefsUtils.getAutoupdateTime(this);
        switch (autoupdateTime){
            case 30:
                item_update_time.setDesc("30分钟");
                break;
            case 60:
                item_update_time.setDesc("1小时");
                break;
            case 120:
                item_update_time.setDesc("2小时");
                break;
            case 180:
                item_update_time.setDesc("3小时");
                break;
        }
        item_update_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateTimeDialog();
            }
        });

    }

    private void showUpdateTimeDialog() {
        L.e("showUpdateTimeDialog中的autoupdateTime： "+autoupdateTime);
        autoupdateTime = PrefsUtils.getAutoupdateTime(this);
        materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("更新频率");
        View view = View.inflate(this, R.layout.update_time_selection_layout, null);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        RadioButton time_30 = (RadioButton) view.findViewById(R.id.time_30);
        RadioButton time_1 = (RadioButton) view.findViewById(R.id.time_1);
        RadioButton time_2 = (RadioButton) view.findViewById(R.id.time_2);
        RadioButton time_3 = (RadioButton) view.findViewById(R.id.time_3);
        switch (autoupdateTime){
            case 30:
                time_30.setChecked(true);
                break;
            case 60:
                time_1.setChecked(true);
                break;
            case 120:
                time_2.setChecked(true);
                break;
            case 180:
                time_3.setChecked(true);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int minutes = 30;
                switch (checkedId){
                    case R.id.time_30:
                        minutes = 30;
                        item_update_time.setDesc("30分钟");
                        break;
                    case R.id.time_1:
                        minutes = 60;
                        item_update_time.setDesc("1小时");
                        break;
                    case R.id.time_2:
                        minutes = 120;
                        item_update_time.setDesc("2小时");
                        break;
                    case R.id.time_3:
                        minutes = 180;
                        item_update_time.setDesc("3小时");
                        break;
                }
                PrefsUtils.changeAutoupdateTime(SettingActivity.this,minutes);
                //重新onStartAutoservice
                startService(new Intent(SettingActivity.this,AutoUpdateService.class));
                materialDialog.dismiss();
            }
        });

        materialDialog.setContentView(view);
        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();

    }

}

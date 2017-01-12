/**
 *
 */
package com.foxconn.rfid.theowner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.foxconn.rfid.theowner.adapter.SettingAdapter;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfidbike_sustain.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author WT00111
 */
public class SettingActivity extends BaseActivity implements Header.headerListener {

    @BindView(R.id.lv_setting)
    ListView mLvSetting;
    @BindView(R.id.header)
    Header mHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initView();
        mHeader.setListener(this);
    }

    protected void initView() {
        mLvSetting.setAdapter(new SettingAdapter(this));
        mLvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        startActivity(new Intent(SettingActivity.this, PersonalInfoActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SettingActivity.this, UpdatePasswordActivity
                                .class));
                        break;
                    case 2:
                        startActivity(new Intent(SettingActivity.this, VersionUpdateActivity
                                .class));
                        break;
                    case 3:
                        PreferenceData.saveLoginAccount(SettingActivity.this, -1);
                        Intent in = new Intent(SettingActivity.this, LoginActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                                .FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        break;
                }
            }
        });
    }

    @Override
    public void onClickLeftIcon() {
        super.onBackPressed();
    }

    @Override
    public void onClickRightIcon() {

    }
}

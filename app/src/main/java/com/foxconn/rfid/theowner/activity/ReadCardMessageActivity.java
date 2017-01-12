package com.foxconn.rfid.theowner.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foxconn.rfid.theowner.adapter.AdapterReadCardMsg;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.ReadCardMsgEntity;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfid.app.response.Device;
import com.yzh.rfidbike_sustain.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by appadmin on 2016/12/30.
 */

public class ReadCardMessageActivity extends BaseActivity implements View.OnClickListener, Header.headerListener, AdapterReadCardMsg.OnDeleteClickListener {

    private Header header;
    private ListView listView;
    private AdapterReadCardMsg adapter;
    private RelativeLayout rl_config, rl_mapshow;

    private LinearLayout ll_bs_device;
    private List<ReadCardMsgEntity> list;
    private Device.DeviceMessage deviceMessage;

    private TextView delete_all_tv;
    private AlertDialog deleteAlertDialog, deleteAlertDialog2;
    private TextView tv_cancel, tv_sure;
    private ImageView iv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readcard_msg);
        initView();
        initDialog1();
        initDialog2();
    }

    //头部标题栏返回键的点击接口回调
    @Override
    public void onClickLeftIcon() {
        finish();
    }

    @Override
    public void onClickRightIcon() {

    }


    private void initView() {
        Intent intent = getIntent();
        deviceMessage = (Device.DeviceMessage) intent.getSerializableExtra("DeviceMessage");

        ll_bs_device = findViewByIds(R.id.ll_bs_device);
        ll_bs_device.setVisibility(View.GONE);
        list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ReadCardMsgEntity entity = new ReadCardMsgEntity("读卡消息" + i, System.currentTimeMillis() + 300 * 1000 * i, "恭喜发财，红包拿来"+20*i);
            list.add(entity);
        }

        header = findViewByIds(R.id.readcard_header);
        header.setTitle(deviceMessage.getDeviceIdDecimal());
        header.setListener(this);


        listView = findViewByIds(R.id.lv_readcard_msg);
        adapter = new AdapterReadCardMsg(this);
        listView.setAdapter(adapter);

        delete_all_tv = (TextView) findViewById(R.id.delete_all_tv);
        delete_all_tv.setVisibility(View.VISIBLE);
        delete_all_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //设置dialog的样式属性
                Window dialogView = deleteAlertDialog2.getWindow();
                int width = getResources().getDisplayMetrics().widthPixels;
                WindowManager.LayoutParams lp = dialogView.getAttributes();
                dialogView.setBackgroundDrawable(new BitmapDrawable());
                lp.width = width - 160;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                //        lp.height = height / 3;
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
                //lp.x = 100;
//        lp.y = -300;
                dialogView.setAttributes(lp);


                //点击显示AlertDialog
                deleteAlertDialog2.show();

                iv_cancel = (ImageView) dialogView.findViewById(R.id.delete);
                tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
                tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
                iv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog2.cancel();
                    }
                });
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog2.cancel();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAlertDialog2.cancel();

                        list.clear();
                        adapter.notifyDataSetChanged();
                        listView.setVisibility(View.GONE);
                        ll_bs_device.setVisibility(View.VISIBLE);
                        delete_all_tv.setVisibility(View.GONE);
                    }
                });

            }
        });


        rl_config = findViewByIds(R.id.rl_config);
        rl_mapshow = findViewByIds(R.id.rl_mapshow);
        rl_config.setFocusable(true);
        rl_mapshow.setFocusable(true);
        rl_config.setOnClickListener(this);
        rl_mapshow.setOnClickListener(this);
        initDatas();
    }

    private void initDatas() {

        if (list.size() == 0) {
            listView.setVisibility(View.GONE);
            ll_bs_device.setVisibility(View.VISIBLE);
            delete_all_tv.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            ll_bs_device.setVisibility(View.GONE);

            adapter.setDatas(list);
            adapter.setOnDeleteClickListener(this);
        }
    }

    private void initDialog1() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout_delete_one, null);
        deleteAlertDialog = new AlertDialog.Builder(this).setView(layout).create();
    }

    private void initDialog2() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout_delete_all, null);
        deleteAlertDialog2 = new AlertDialog.Builder(this).setView(layout).create();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_config:
                Intent rl1_intent = new Intent(ReadCardMessageActivity.this, ParameterConfigurationActivity.class);
                rl1_intent.putExtra("session", PreferenceData.loadSession(ReadCardMessageActivity.this));
                rl1_intent.putExtra("DeviceId", deviceMessage.getDeviceId());
                rl1_intent.putExtra("companyId", deviceMessage.getCompanyId());
                startActivity(rl1_intent);
                break;
            case R.id.rl_mapshow:
                Intent rl2_intent = new Intent(ReadCardMessageActivity.this, BaseStationMapActivity.class);
                rl2_intent.putExtra("lat", deviceMessage.getLatitude());
                rl2_intent.putExtra("lng", deviceMessage.getLongitude());
                rl2_intent.putExtra("addr", deviceMessage.getDeviceAddress());
                startActivity(rl2_intent);
                break;
        }
    }

    @Override
    public void onClickListen(View view, final int position) {
        //设置dialog的样式属性
        Window dialogView = deleteAlertDialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams lp = dialogView.getAttributes();
        dialogView.setBackgroundDrawable(new BitmapDrawable());
        lp.width = width - 160;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //        lp.height = height / 3;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        //lp.x = 100;
//        lp.y = -300;
        dialogView.setAttributes(lp);


        //点击显示AlertDialog
        deleteAlertDialog.show();

        iv_cancel = (ImageView) dialogView.findViewById(R.id.delete);
        tv_cancel = (TextView) dialogView.findViewById(R.id.tv_cancel);
        tv_sure = (TextView) dialogView.findViewById(R.id.tv_sure);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertDialog.cancel();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertDialog.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertDialog.cancel();
                list.remove(position);
                adapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    delete_all_tv.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    ll_bs_device.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }
}

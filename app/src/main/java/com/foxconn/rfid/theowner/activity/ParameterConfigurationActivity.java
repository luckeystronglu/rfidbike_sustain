package com.foxconn.rfid.theowner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.socket.SocketDeviceClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.CommonByDeviceIdRequest;
import com.yzh.rfid.app.request.ConfigRFInfoRequest;
import com.yzh.rfid.app.request.GetDevicesByCompanyDeviceIdRequest;
import com.yzh.rfid.app.response.CommonResponse;
import com.yzh.rfid.app.response.Device;
import com.yzh.rfid.app.response.GetDevicesResponse;
import com.yzh.rfidbike_sustain.R;

import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMAND_ID_CONFIG_RF_PARAMS;
import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMAND_ID_REBOOT_DEVICE;


/**
 * Created by appadmin on 2016/12/30.
 */

public class ParameterConfigurationActivity extends BaseActivity implements View.OnClickListener, Header.headerListener {
    private Header configHeader;
    private RelativeLayout rl_setting, rl_reboot;

    private TextView tv_bs_id, tv_bs_classes, tv_bs_name, tv_bs_addr, tv_bs_lng, tv_bs_lat; //Xml Part1的tv
    private TextView tv_bs_temperature, tv_bs_battery, tv_bs_imeiNum, tv_bs_imsiNum, tv_bs_msisdnNum, tv_bs_smsVersion; //Xml Part2的tv
    private TextView tv_bs_softVersion, tv_bs_solidVersion, tv_bs_powerStyle, tv_bs_battery_status, tv_bs_isOnline; //Xml Part3的tv

    private EditText et_bs_24value, et_bs_24channel, et_bs_433value, et_bs_433channel, et_bs_filterSeconds; //EditText
    private String deviceId;
    private GetDevicesResponse.GetDevicesResponseMessage getDevicesResponseMessage;
    private long companyId;
    private String session;

    private int MIN_24GVALUE = 0;
    private int MAX_24GVALUE = 127;
    private int MIN_24GCHANNEL = 0;
    private int MAX_24GCHANNEL = 99;

    private int MIN_433MVALUE = 0;
    private int MAX_433MVALUE = 127;
    private int MIN_433MCHANNEL = 0;
    private int MAX_433MCHANNEL = 99;

    private int MIN_FILTERSECONDS = 0;
    private int MAX_FILTERSECONDS = 255;
    private Device.DeviceMessage device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params_configuration);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        deviceId = intent.getStringExtra("DeviceId");
        companyId = intent.getLongExtra("companyId", -1);

        configHeader = findViewByIds(R.id.config_header_btn);
        configHeader.setListener(this); //头部
        rl_setting = findViewByIds(R.id.rl_setting);
        rl_reboot = findViewByIds(R.id.rl_reboot);
        rl_setting.setFocusable(true);
        rl_reboot.setFocusable(true);
        rl_setting.setOnClickListener(this);
        rl_reboot.setOnClickListener(this);

        tv_bs_id = findViewByIds(R.id.tv_bs_id);
        tv_bs_classes = findViewByIds(R.id.tv_bs_classes);
        tv_bs_name = findViewByIds(R.id.tv_bs_name);
        tv_bs_addr = findViewByIds(R.id.tv_bs_addr);
        tv_bs_lng = findViewByIds(R.id.tv_bs_lng);
        tv_bs_lat = findViewByIds(R.id.tv_bs_lat);
        tv_bs_temperature = findViewByIds(R.id.tv_bs_temperature);
        tv_bs_battery = findViewByIds(R.id.tv_bs_battery);
        tv_bs_imeiNum = findViewByIds(R.id.tv_bs_imeiNum);
        tv_bs_imsiNum = findViewByIds(R.id.tv_bs_imsiNum);
        tv_bs_msisdnNum = findViewByIds(R.id.tv_bs_msisdnNum);
        tv_bs_smsVersion = findViewByIds(R.id.tv_bs_smsVersion);
        tv_bs_isOnline = findViewByIds(R.id.tv_bs_isOnline);

        tv_bs_solidVersion = findViewByIds(R.id.tv_bs_solidVersion);
        tv_bs_powerStyle = findViewByIds(R.id.tv_bs_powerStyle);
        tv_bs_battery_status = findViewByIds(R.id.tv_bs_battery_status);

        //软件版本
        tv_bs_softVersion = findViewByIds(R.id.tv_bs_softVersion);


        //2.4G 值
        et_bs_24value = findViewByIds(R.id.et_bs_24value);
        et_bs_24value.setSelection(et_bs_24value.getText().length());
        et_bs_24value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start > 2) {
                    if (MIN_24GVALUE != -1 && MAX_24GVALUE != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_24GVALUE) {
                            s = String.valueOf(MAX_24GVALUE);

                        } else if (num < MIN_24GVALUE) {
                            s = String.valueOf(MIN_24GVALUE);
                        }
                        et_bs_24value.setText(s);
                        return;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_24GVALUE != -1 && MAX_24GVALUE != -1) {
                        int markVal;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = MIN_24GVALUE;
                        }

                        if (markVal > MAX_24GVALUE) {
                            ToastUtils.showTextToast(ParameterConfigurationActivity.this,"2.4G衰减值不能超过"+MAX_24GVALUE);
                            et_bs_24value.setText(String.valueOf(MAX_24GVALUE));
                            et_bs_24value.findFocus();
                            et_bs_24value.setSelection(et_bs_24value.getText().length());
                        }
                        return;
                    }
                }
            }
        });



        //2.4G 频道
        et_bs_24channel = findViewByIds(R.id.et_bs_24channel);
        et_bs_24channel.setGravity(Gravity.CENTER);
        et_bs_24channel.setSelection(et_bs_24channel.getText().toString().trim().length());
        et_bs_24channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start > 1) {
                    if (MIN_24GCHANNEL != -1 && MAX_24GCHANNEL != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_24GCHANNEL) {
                            s = String.valueOf(MAX_24GCHANNEL);
                        } else if (num < MIN_24GCHANNEL) {
                            s = String.valueOf(MIN_24GCHANNEL);
                        }
                        et_bs_24channel.setText(s);
                        return;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_24GCHANNEL != -1 && MAX_24GCHANNEL != -1) {
                        int markVal;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = MIN_24GCHANNEL;
                        }

                        if (markVal > MAX_24GCHANNEL) {
                            ToastUtils.showTextToast(ParameterConfigurationActivity.this,"2.4G通信频道不能超过"+MAX_24GCHANNEL);
                            et_bs_24channel.setText(String.valueOf(MAX_24GCHANNEL));
                            et_bs_24channel.findFocus();
                            et_bs_24channel.setSelection(et_bs_24channel.length());
                        }
                        return;
                    }
                }
            }
        });



        //433M 值
        et_bs_433value = findViewByIds(R.id.et_bs_433value);
        et_bs_433value.setGravity(Gravity.CENTER);
        et_bs_433value.setSelection(et_bs_433value.getText().toString().trim().length());
        et_bs_433value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start > 2) {
                    if (MIN_433MVALUE != -1 && MAX_433MVALUE != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_433MVALUE) {
                            s = String.valueOf(MAX_433MVALUE);

                        } else if (num < MIN_433MVALUE) {
                            s = String.valueOf(MIN_433MVALUE);
                        }
                        et_bs_433value.setText(s);
                        return;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_433MVALUE != -1 && MAX_433MVALUE != -1) {
                        int markVal;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = MIN_433MVALUE;
                        }

                        if (markVal > MAX_433MVALUE) {
                            ToastUtils.showTextToast(ParameterConfigurationActivity.this,"433M衰减值不能超过"+MAX_433MVALUE);
                            et_bs_433value.setText(String.valueOf(MAX_433MVALUE));
                            et_bs_433value.findFocus();
                            et_bs_433value.setSelection(et_bs_433value.length());
                        }
                        return;
                    }
                }
            }
        });

        //433M 频道
        et_bs_433channel = findViewByIds(R.id.et_bs_433channel);
        et_bs_433channel.setGravity(Gravity.CENTER);
        et_bs_433channel.setSelection(et_bs_433channel.getText().toString().trim().length());
        et_bs_433channel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start > 1) {
                    if (MIN_433MCHANNEL != -1 && MAX_433MCHANNEL != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_433MCHANNEL) {
                            s = String.valueOf(MAX_433MCHANNEL);

                        } else if (num < MIN_433MCHANNEL) {
                            s = String.valueOf(MIN_433MCHANNEL);
                        }
                        et_bs_433channel.setText(s);
                        return;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_433MCHANNEL != -1 && MAX_433MCHANNEL != -1) {
                        int markVal;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = MIN_433MCHANNEL;
                        }

                        if (markVal > MAX_433MCHANNEL) {
                            ToastUtils.showTextToast(ParameterConfigurationActivity.this,"433M通信频道不能超过"+MAX_433MCHANNEL);
                            et_bs_433channel.setText(String.valueOf(MAX_433MCHANNEL));
                            et_bs_433channel.findFocus();
                            et_bs_433channel.setSelection(et_bs_433channel.length());
                        }
                        return;
                    }
                }
            }
        });

        //过滤时间
        et_bs_filterSeconds = findViewByIds(R.id.et_bs_filterSeconds);
//        et_bs_filterSeconds.setGravity(Gravity.CENTER);
//        et_bs_filterSeconds.setSelection(et_bs_filterSeconds.getText().toString().trim().length());
        et_bs_filterSeconds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start > 2) {
                    if (MIN_FILTERSECONDS != -1 && MAX_FILTERSECONDS != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_FILTERSECONDS) {
                            s = String.valueOf(MAX_FILTERSECONDS);

                        } else if (num < MIN_FILTERSECONDS) {
                            s = String.valueOf(MIN_FILTERSECONDS);
                        }
                        et_bs_filterSeconds.setText(s);

                        return;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_FILTERSECONDS != -1 && MAX_FILTERSECONDS != -1) {
                        int markVal;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = MIN_FILTERSECONDS;
                        }

                        if (markVal > MAX_FILTERSECONDS) {
                            ToastUtils.showTextToast(ParameterConfigurationActivity.this,"过滤时间不能超过"+MAX_FILTERSECONDS);
                            et_bs_filterSeconds.setText(String.valueOf(MAX_FILTERSECONDS));
                            et_bs_filterSeconds.findFocus();
                            et_bs_filterSeconds.setSelection(et_bs_filterSeconds.length());
                        }
                        return;
                    }
                }
            }
        });

        doSocket();

    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final GetDevicesByCompanyDeviceIdRequest.GetDevicesByCompanyDeviceIdRequestMessage.Builder deviceMessage = GetDevicesByCompanyDeviceIdRequest.GetDevicesByCompanyDeviceIdRequestMessage.newBuilder();
//        deviceMessage.setSession(PreferenceData.loadSession(this));
//        deviceMessage.setSession("3ef012dac6b805437898f4e10ce442dc5a");
        deviceMessage.setSession(session);
        deviceMessage.setDeviceId(deviceId);
        deviceMessage.setCompanyId(companyId);

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_GET_DEVICE_STATUS, deviceMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_GET_DEVICE_STATUS) {
                getDevicesResponseMessage = GetDevicesResponse.GetDevicesResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (getDevicesResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(ParameterConfigurationActivity.this, getDevicesResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {

                    device = getDevicesResponseMessage.getDevice(0);
                    tv_bs_id.setText(device.getDeviceIdDecimal());
                    tv_bs_classes.setText(device.getDeviceType());
                    tv_bs_name.setText(device.getDeviceName());
                    tv_bs_addr.setText(device.getDeviceAddress());
                    tv_bs_lat.setText(String.valueOf(device.getLatitude()));
                    tv_bs_lng.setText(String.valueOf(device.getLongitude()));
                    tv_bs_temperature.setText(String.valueOf(device.getTemperature()));
                    tv_bs_battery.setText(String.valueOf(device.getBattery()));
                    tv_bs_imeiNum.setText(device.getDeviceImei());
                    tv_bs_imsiNum.setText(device.getDeviceImsi());
                    tv_bs_msisdnNum.setText(device.getDeviceMsisdn());
                    tv_bs_smsVersion.setText(device.getSmsVersion());
                    if (device.getIsOnline()) {
                        tv_bs_isOnline.setText("在线");
                    } else {
                        tv_bs_isOnline.setText("离线");
                    }
                    tv_bs_solidVersion.setText(device.getHardwareVersion());
                    tv_bs_softVersion.setText(device.getSoftwareVersion());
                    tv_bs_powerStyle.setText(device.getBatteryStatus() == 0 ? "外电" : "电池");
                    tv_bs_battery_status.setText(device.getIsConnectBattery() == 0 ? "未连接" : "已连接");


                    et_bs_24value.setText(String.valueOf(device.getDamp24G()));
                    et_bs_24channel.setText(String.valueOf(device.getChannel24G()));
                    et_bs_433value.setText(String.valueOf(device.getDamp433M()));
                    et_bs_433channel.setText(String.valueOf(device.getChannel433M()));
                    et_bs_filterSeconds.setText(String.valueOf(device.getFilterSeconds()));

                }
            } else if (eventPackage.getCommandId() == COMMAND_ID_CONFIG_RF_PARAMS) {
                CommonResponse.CommonResponseMessage commonMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (commonMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(ParameterConfigurationActivity.this, commonMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    et_bs_24value.setText(et_bs_24value.getText().toString().trim());

                    et_bs_24channel.setText(et_bs_24channel.getText().toString().trim());

                    et_bs_433value.setText(et_bs_433value.getText().toString().trim());

                    et_bs_433channel.setText(et_bs_433channel.getText().toString().trim());

                    et_bs_filterSeconds.setText(et_bs_filterSeconds.getText().toString().trim());

                }
            }
            else if (eventPackage.getCommandId() == COMMAND_ID_REBOOT_DEVICE){
                CommonResponse.CommonResponseMessage commonMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (commonMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(ParameterConfigurationActivity.this, commonMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this,"重启成功");
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

    private void updateDeviceMessage() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
        final ConfigRFInfoRequest.ConfigRFInfoRequestMessage.Builder configBuilder = ConfigRFInfoRequest.ConfigRFInfoRequestMessage.newBuilder();
        configBuilder.setDeviceId(deviceId);
        configBuilder.setDamp24G(Integer.parseInt(et_bs_24value.getText().toString().trim()));
        configBuilder.setChannel24G(Integer.parseInt(et_bs_24channel.getText().toString().trim()));
        configBuilder.setDamp433M(Integer.parseInt(et_bs_433value.getText().toString().trim()));
        configBuilder.setChannel433M(Integer.parseInt(et_bs_433channel.getText().toString().trim()));
        configBuilder.setFilterSeconds(Integer.parseInt(et_bs_filterSeconds.getText().toString().trim()));

        new Thread() {
            public void run() {
                SocketDeviceClient client;
                client = SocketDeviceClient.getInstance();
                client.send(COMMAND_ID_CONFIG_RF_PARAMS, configBuilder.build().toByteArray());
            }
        }.start();

    }


    //头部标题栏返回键的点击接口回调

    @Override
    public void onClickLeftIcon() {

        finish();
    }

    @Override
    public void onClickRightIcon() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting: //设置保存
                boolean cancel = false;
                View focusView = null;
                //判断2.4G衰减值是否为空
                if (TextUtils.isEmpty(et_bs_24value.getText().toString().trim())) {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this, "2.4G衰减值不能为空");
                    focusView = et_bs_24value;
                    cancel = true;
                }
                //判断2.4G通信频道是否为空
                if (TextUtils.isEmpty(et_bs_24channel.getText().toString().trim())) {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this, "2.4G通信频道不能为空");
                    focusView = et_bs_24channel;
                    cancel = true;
                }
                //判断433M衰减值是否为空
                if (TextUtils.isEmpty(et_bs_433value.getText().toString().trim())) {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this, "433M衰减值不能为空");
                    focusView = et_bs_433value;
                    cancel = true;
                }
                //判断433M通信频道是否为空
                if (TextUtils.isEmpty(et_bs_433channel.getText().toString().trim())) {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this, "433M通信频道不能为空");
                    focusView = et_bs_433channel;
                    cancel = true;
                }
                //判断过滤时间是否为空
                if (TextUtils.isEmpty(et_bs_filterSeconds.getText().toString().trim())) {
                    ToastUtils.showTextToast(ParameterConfigurationActivity.this, "过滤时间不能为空");
                    focusView = et_bs_filterSeconds;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    if (et_bs_24value.getText().toString().trim().equals(String.valueOf(device.getDamp24G()))
                            && et_bs_24channel.getText().toString().trim().equals(String.valueOf(device.getChannel24G()))
                            && et_bs_433value.getText().toString().trim().equals(String.valueOf(device.getDamp433M()))
                            && et_bs_433channel.getText().toString().trim().equals(String.valueOf(device.getChannel433M()))
                            && et_bs_filterSeconds.getText().toString().trim().equals(String.valueOf(device.getFilterSeconds()))) {
                       ToastUtils.showTextToast(ParameterConfigurationActivity.this,getString(R.string.no_modification_change));
                    }else {
                        updateDeviceMessage();
                    }

                }

                break;
            case R.id.rl_reboot: //重启
                rebootDevice();
//                Toast.makeText(ParameterConfigurationActivity.this, "重启", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void rebootDevice() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
        final CommonByDeviceIdRequest.CommonByDeviceIdRequestMessage.Builder rebootMessage = CommonByDeviceIdRequest.CommonByDeviceIdRequestMessage.newBuilder();
        rebootMessage.setDeviceId(deviceId);

        new Thread() {
            public void run() {
                SocketDeviceClient client;
                client = SocketDeviceClient.getInstance();
                client.send(COMMAND_ID_REBOOT_DEVICE, rebootMessage.build().toByteArray());
            }
        }.start();
    }


}

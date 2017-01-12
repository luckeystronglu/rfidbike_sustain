package com.foxconn.rfid.theowner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.foxconn.rfid.theowner.util.string.DateUtils;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.AddDeviceRequest;
import com.yzh.rfid.app.request.GetDeviceStatusRequest;
import com.yzh.rfid.app.request.GetDeviceTypeRequest;
import com.yzh.rfid.app.response.CommonResponse;
import com.yzh.rfid.app.response.Device;
import com.yzh.rfid.app.response.SysDict;
import com.yzh.rfid.app.response.SysDictResponse;
import com.yzh.rfidbike_sustain.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMAND_ID_ADD_BASESTATION;
import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMAND_ID_ADD_BASESTATION_STATUS;
import static com.foxconn.rfid.theowner.socket.SocketAppPacket.COMMAND_ID_ADD_BASESTATION_TYPE;


public class DeviceSetupActivity extends BaseActivity {
    private Calendar calendar = Calendar.getInstance();
    private EditText et_longitude_num, et_latitude_num;//经度，纬度
    private double curLongitude, curLatitude;
    private BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient = null;
    private TextView tv_default_type_name, tv_include_company_choice, tv_status_choice;
    private TextView tv_expire_date_num, tv_SIM_expire_date_num;
    private Header install_bs_header;

    private ImageView iv_default_type, iv_status_choice;

    private EditText et_base_id_input, et_video_id_input, et_base_name_input, et_base_address_input;
    private EditText et_IMEI_num_input, et_IMSI, et_MSISDN, et_carrier_operator, et_installBs_remarks;
    private EditText et_account_num;
    private long outTimeDate, simLongFromDate;
    //    private int statusPosition = -1;
    private String companyId = "", companyName = "";
    //基站类型
    private List<String> typeLabelList = new ArrayList<>();
    private List<String> typeValueList = new ArrayList<>();
    private String strDeviceType = "";



    //使用状态
    private List<String> statusLabelList = new ArrayList<>();
    private List<String> statusValueList = new ArrayList<>();
    private String strDeviceStatus = "";
    private String geoAddress;
    private String geoLatitude;
    private String geoLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup);
        et_longitude_num = (EditText) findViewById(R.id.et_longitude_num);
        et_latitude_num = (EditText) findViewById(R.id.et_latitude_num);
        getLocation();
        initView();
    }

    private void initView() {
        et_base_id_input = findViewByIds(R.id.et_base_id_input);
        et_video_id_input = findViewByIds(R.id.et_video_id_input);
        et_base_name_input = findViewByIds(R.id.et_base_name_input);
        et_base_address_input = findViewByIds(R.id.et_base_address_input);
        et_IMEI_num_input = findViewByIds(R.id.et_IMEI_num_input);
        et_IMSI = findViewByIds(R.id.et_IMSI);
        et_MSISDN = findViewByIds(R.id.et_MSISDN);
        et_carrier_operator = findViewByIds(R.id.et_carrier_operator);
        et_installBs_remarks = findViewByIds(R.id.et_installBs_remarks);
        et_account_num = findViewByIds(R.id.et_account_num);

        tv_default_type_name = findViewByIds(R.id.tv_default_type_name);
        iv_default_type = findViewByIds(R.id.iv_default_type);
        tv_include_company_choice = findViewByIds(R.id.tv_include_company_choice);
        tv_status_choice = findViewByIds(R.id.tv_status_choice);
        iv_status_choice = findViewByIds(R.id.iv_status_choice);
        tv_expire_date_num = findViewByIds(R.id.tv_expire_date_num);
        tv_SIM_expire_date_num = findViewByIds(R.id.tv_SIM_expire_date_num);
        install_bs_header = findViewByIds(R.id.install_bs_header);
        install_bs_header.setListener(new Header.headerListener() {
            @Override
            public void onClickLeftIcon() {
                finish();
            }

            @Override
            public void onClickRightIcon() {

            }
        });


    }


    //选择基站类型
    public void DeviceStyle(View view) {
        iv_default_type.setImageDrawable(getResources().getDrawable(R.drawable.ico_up));
        getAddBaseStationTypeSocket(); //doSocket();

//        OptionPicker picker = new OptionPicker(this, new String[]{
//                "GPRS", "WIFI"
//        });

    }

    private void getAddBaseStationTypeSocket() {

        final GetDeviceTypeRequest.GetDeviceTypeRequestMessage.Builder requestMessage = GetDeviceTypeRequest.GetDeviceTypeRequestMessage.newBuilder();
        requestMessage.setSession(PreferenceData.loadSession(this));
//        requestMessage.setType(String.valueOf(typePosition));
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(COMMAND_ID_ADD_BASESTATION_TYPE, requestMessage.build().toByteArray());
            }
        }.start();

    }

    //选择归属公司名称
    public void belongCompanyChoice(View view) {

        startActivity(new Intent(DeviceSetupActivity.this, SelectCompanyActivity.class));

    }


    //选择用户使用状态
    public void useStatusChoice(View view) {

        iv_status_choice.setImageDrawable(getResources().getDrawable(R.drawable.ico_up));
        getAddBaseStationStatusSocket();

    }

    private void getAddBaseStationStatusSocket() {
        final GetDeviceStatusRequest.GetDeviceStatusRequestMessage.Builder statusRequestMsg = GetDeviceStatusRequest.GetDeviceStatusRequestMessage.newBuilder();
        statusRequestMsg.setSession(PreferenceData.loadSession(this));
//        requestMessage.setType(String.valueOf(typePosition));
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(COMMAND_ID_ADD_BASESTATION_STATUS, statusRequestMsg.build().toByteArray());
            }
        }.start();
    }

    //跳转至 基站安装地址选择的Activity
    public void addrChoiceClick(View view){
        Intent intent = new Intent(DeviceSetupActivity.this, MapChoiceAddrActivity.class);
        intent.putExtra("addrlat",curLatitude);
        intent.putExtra("addrlng",curLongitude);
        startActivity(intent);
    }

    //基站安装地址的返回方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //过期日期设置
    public void onYearMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this);
        picker.setRangeStart(2016, 6, 15);
        picker.setRangeEnd(2032, 12, 31);
        picker.setTopHeight(50);
        picker.setTopPadding(20);
        picker.setCancelTextColor(getResources().getColor(R.color.gray));
        picker.setCancelTextSize((int) getResources().getDimension(R.dimen.font_size_little));
        picker.setSubmitTextColor(getResources().getColor(R.color.red));
        picker.setSubmitTextSize((int) getResources().getDimension(R.dimen.font_size_little));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                String dateChoiced = String.format("%s-%s-%s", year, month, day);

                try {
                    outTimeDate = DateUtils.getLongFromDate(DateUtils.formatDate(dateChoiced));
//					Log.d("print", "onDatePicked: longFromDate: "+outTimeDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_expire_date_num.setText(dateChoiced);

            }
        });
        picker.show();
    }


    //SIM到期日期设置
    public void onSIMYearMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this);
        picker.setRangeStart(2016, 6, 15);
        picker.setRangeEnd(2032, 12, 31);
        picker.setTopHeight(50);
        picker.setTopPadding(20);
        picker.setCancelTextColor(getResources().getColor(R.color.gray));
        picker.setCancelTextSize((int) getResources().getDimension(R.dimen.font_size_little));
        picker.setSubmitTextColor(getResources().getColor(R.color.red));
        picker.setSubmitTextSize((int) getResources().getDimension(R.dimen.font_size_little));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String dateChoiced = String.format("%s-%s-%s", year, month, day);
                try {
                    simLongFromDate = DateUtils.getLongFromDate(DateUtils.formatDate(dateChoiced));
//					Log.d("print", "onDatePicked: simLongFromDate: "+simLongFromDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_SIM_expire_date_num.setText(dateChoiced);
            }
        });
        picker.show();
    }

    //获取定位经纬度
    private void getLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0; //每隔1秒定位一次
        //        int span= 5000; //每隔5秒定位一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //通过地图定位监听类，获取定位经纬度
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            curLongitude = location.getLongitude();
            curLatitude = location.getLatitude();
            et_longitude_num.setText(String.valueOf(curLongitude));
            et_latitude_num.setText(String.valueOf(curLatitude));
            et_base_address_input.setText(location.getAddrStr());
//            Log.d("print", "curLongitude: " + curLongitude + " curLatitude: " + curLatitude);
        }
    }



    //填写完成资料后,保存键的点击事件  4294967295为十六进制8位最大数FFFFFFFF
    public void addBaseStation(View view) {

//        tv_include_company_choice.setText(companyName);
        judgeDataBeforeSocket();
    }


    private void judgeDataBeforeSocket() {
        //doSocket前为空逻辑判断
        boolean cancel = false;
        View focusView = null;


        //判断基站id是否为空
        if (TextUtils.isEmpty(et_base_id_input.getText().toString().trim())) {
//            et_base_id_input.setError(getString(R.string.error_field_required));
            ToastUtils.showTextToast(DeviceSetupActivity.this, "请输入基站id");
            focusView = et_base_id_input;
            cancel = true;
            return;
        }

        //基站id不为空时，判断基站id范围是否超出
        if (!TextUtils.isEmpty(et_base_id_input.getText().toString().trim())) {

            if (Long.parseLong(et_base_id_input.getText().toString().trim()) > 4294967295L) {
                ToastUtils.showTextToast(DeviceSetupActivity.this, "基站id数值超出最大范围4294967295,请检查并重新输入");
                focusView = et_base_id_input;
                cancel = true;
                return;
            }

        }


        //判断基站类型是否为空
        if (TextUtils.isEmpty(tv_default_type_name.getText().toString().trim()) || tv_default_type_name.getText().toString().equals("") || tv_default_type_name.getText().toString().equals(getResources().getString(R.string.default_type_name))) {
//            tv_default_type_name.setError(getString(R.string.error_field_required));
            ToastUtils.showTextToast(DeviceSetupActivity.this, "请选择基站类型");
            focusView = tv_default_type_name;
            cancel = true;
            return;
        }

        //判断归属公司是否为空
        if (TextUtils.isEmpty(tv_include_company_choice.getText().toString().trim()) || tv_include_company_choice.getText().toString().equals("") || tv_include_company_choice.getText().toString().equals(getResources().getString(R.string.include_company_choice))) {
//            tv_include_company_choice.setError(getString(R.string.error_field_required));
            ToastUtils.showTextToast(DeviceSetupActivity.this, "请选择归属公司");
            focusView = tv_include_company_choice;
            cancel = true;
            return;
        }
        //判断基站名称是否为空
        if (TextUtils.isEmpty(et_base_name_input.getText().toString().trim())) {
//            et_base_name_input.setError(getString(R.string.error_field_required));
            ToastUtils.showTextToast(DeviceSetupActivity.this, "请输入基站名称");
            focusView = et_base_name_input;
            cancel = true;
            return;
        }

        if (cancel) {
            focusView.requestFocus();

        } else {
            doSocket();
        }
    }


    //接收EventBus的方法
    @Override
    public void onEventMainThread(EventBusMsg eventPackage) {
        super.onEventMainThread(eventPackage);
        if (eventPackage.getMsgType().equals(EventBusMsg.MsgType.MSG_COMPANY_ID)) {
            Map<String, String> map = eventPackage.getValue();
            companyId = map.get("companyId");
            companyName = map.get("companyName");
            tv_include_company_choice.setText(companyName);
        }
        //  接收基站地址选择后返回值
        else if (eventPackage.getMsgType().equals(EventBusMsg.MsgType.MSG_ADDRESS_ID)) {
            Map<String, String> map = eventPackage.getValue();
            geoAddress = map.get("geoAddress");
            geoLatitude = map.get("geoLatitude");
            geoLongitude = map.get("geoLongitude");
            et_base_address_input.setText(geoAddress);
            et_latitude_num.setText(geoLatitude);
            et_longitude_num.setText(geoLongitude);
        }
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final AddDeviceRequest.AddDeviceRequestMessage.Builder addBsMessage = AddDeviceRequest.AddDeviceRequestMessage.newBuilder();
        //        deviceMessage.setSession("3ef012dac6b805437898f4e10ce442dc5a");
        addBsMessage.setSession(PreferenceData.loadSession(this));
        //获取16进制的DeviceId字符串
//        String hexIdStr = padLeft(Long.toHexString(Long.parseLong(et_base_id_input.getText().toString().trim())).toUpperCase(), 8);

        Device.DeviceMessage.Builder deviceMessage = Device.DeviceMessage.newBuilder();

//        deviceMessage.setDeviceId(hexIdStr);
        deviceMessage.setDeviceIdDecimal(et_base_id_input.getText().toString().trim());
        deviceMessage.setVideoId(et_video_id_input.getText().toString().trim());
        deviceMessage.setDeviceName(et_base_name_input.getText().toString().trim());
        deviceMessage.setDeviceAddress(et_base_address_input.getText().toString().trim());
        deviceMessage.setDeviceImei(et_IMEI_num_input.getText().toString().trim());
        deviceMessage.setDeviceImsi(et_IMSI.getText().toString().trim());
        deviceMessage.setDeviceMsisdn(et_MSISDN.getText().toString().trim());
        deviceMessage.setOperator(et_carrier_operator.getText().toString().trim());
        deviceMessage.setRemark(et_installBs_remarks.getText().toString().trim());
        deviceMessage.setBalance(et_account_num.getText().toString().trim().equals("") ? 0.0 : Double.parseDouble(et_account_num.getText().toString().trim()));
//        deviceMessage.setDeviceType(String.valueOf(typePosition));
        deviceMessage.setDeviceType(strDeviceType);
        deviceMessage.setCompanyId(Long.parseLong(companyId)); //归属公司
//        Log.d("print", "doSocket  companyId: "+Long.parseLong(companyId));
        deviceMessage.setExpireDt(outTimeDate); //过期日期
        deviceMessage.setSimExpireDt(simLongFromDate); //SIM过期日期
        deviceMessage.setStatus(strDeviceStatus.equals("") ? "1" : strDeviceStatus); //默认为1(正常使用状态)
//        deviceMessage.setStatus(String.valueOf(statusPosition).equals("-1") ? "1":String.valueOf(statusPosition));
//        Log.d("print", "提交doSocket后 strDeviceStatus: " + strDeviceStatus);

        //能够定位取值则取值,否则取北京
        deviceMessage.setLatitude(Double.parseDouble(et_latitude_num.getText().toString().trim()) > 0.1 && Double.parseDouble(et_latitude_num.getText().toString().trim()) < 90.0 ? Double.parseDouble(et_latitude_num.getText().toString().trim()) : 39.963175);
        deviceMessage.setLongitude(Double.parseDouble(et_longitude_num.getText().toString().trim()) > 0.1 && Double.parseDouble(et_longitude_num.getText().toString().trim()) < 180.0 ? Double.parseDouble(et_longitude_num.getText().toString().trim()) : 116.400244);
        deviceMessage.setCreateBy(String.valueOf(PreferenceData.loadLoginAccount(DeviceSetupActivity.this))); //创建者

        addBsMessage.setDeviceMessage(deviceMessage);

        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(COMMAND_ID_ADD_BASESTATION, addBsMessage.build().toByteArray());
            }
        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            //添加基站
            if (eventPackage.getCommandId() == COMMAND_ID_ADD_BASESTATION) {
                CommonResponse.CommonResponseMessage commonMessage = CommonResponse.CommonResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (commonMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(DeviceSetupActivity.this, commonMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    ToastUtils.showTextToast(DeviceSetupActivity.this, "添加成功");
                    DeviceSetupActivity.this.finish();
                }
            }

            //基站Type回应Response
            if (eventPackage.getCommandId() == COMMAND_ID_ADD_BASESTATION_TYPE) {
                SysDictResponse.SysDictResponseMessage sysDictResponseMessage = SysDictResponse.SysDictResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (sysDictResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(DeviceSetupActivity.this, sysDictResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {

                    //基站Type请求成功后
                    typeValueList.clear();
                    typeLabelList.clear();
                    List<SysDict.SysDictMessage> statusMessageList = sysDictResponseMessage.getSysDictMessageList();
                    if (statusMessageList.size() != 0) {
                        for (int i = 0; i < statusMessageList.size(); i++) {
                            typeLabelList.add(statusMessageList.get(i).getLabel());
                            typeValueList.add(statusMessageList.get(i).getValue());
                        }

                        OptionPicker picker = new OptionPicker(DeviceSetupActivity.this, typeLabelList);
                        WheelView.LineConfig config = new WheelView.LineConfig();
                        config.setColor(0xFFFF00FF);//线颜色
                        config.setAlpha(100);//线透明度
                        config.setRatio(0.65f);//线比率
                        picker.setLineConfig(config);

                        picker.setOffset(1);
                        picker.setLineVisible(true);
                        picker.setTopLineColor(getResources().getColor(R.color.gray));
                        picker.setCancelTextColor(getResources().getColor(R.color.gray));
                        picker.setCancelTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setSubmitTextColor(getResources().getColor(R.color.red));
                        picker.setSubmitTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setSelectedIndex(0);
                        picker.setTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setTopPadding(10);
                        picker.setTopHeight(50);
                        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                            @Override
                            public void onOptionPicked(int position, String option) {
                                for (int i = 0; i < typeValueList.size(); i++) {
                                    if (position == i) {
                                        strDeviceType = typeValueList.get(i);
                                        break;
                                    }

                                }
                                tv_default_type_name.setText(option);
//                                Log.d("print", "onOptionPicked strDeviceType: "+strDeviceType);

                            }
                        });
                        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                iv_default_type.setImageDrawable(getResources().getDrawable(R.drawable.ico_dn));
                            }
                        });
                        picker.show();
                    }
                }
            }

            //使用状态Response
            if (eventPackage.getCommandId() == COMMAND_ID_ADD_BASESTATION_STATUS) {
                SysDictResponse.SysDictResponseMessage statusResponseMessage = SysDictResponse.SysDictResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (statusResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(DeviceSetupActivity.this, statusResponseMessage.getErrorMsg().getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {

                    //基站Type请求成功后
                    statusLabelList.clear();
                    statusValueList.clear();
                    List<SysDict.SysDictMessage> statusMessageList = statusResponseMessage.getSysDictMessageList();
                    if (statusMessageList.size() != 0) {
                        for (int i = 0; i < statusMessageList.size(); i++) {
                            statusLabelList.add(statusMessageList.get(i).getLabel());
                            statusValueList.add(statusMessageList.get(i).getValue());
                        }

                        OptionPicker picker = new OptionPicker(DeviceSetupActivity.this, statusLabelList);
                        WheelView.LineConfig config = new WheelView.LineConfig();
                        config.setColor(0xFFFF00FF);//线颜色
                        config.setAlpha(100);//线透明度
                        config.setRatio(0.65f);//线比率
                        picker.setLineConfig(config);

                        picker.setOffset(2);
                        picker.setLineVisible(true);
                        picker.setTopLineColor(getResources().getColor(R.color.gray));
                        picker.setCancelTextColor(getResources().getColor(R.color.gray));
                        picker.setCancelTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setSubmitTextColor(getResources().getColor(R.color.red));
                        picker.setSubmitTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setSelectedIndex(1);
                        picker.setTextSize((int) getResources().getDimension(R.dimen.font_size_little));
                        picker.setTopPadding(10);
                        picker.setTopHeight(50);
                        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                            @Override
                            public void onOptionPicked(int position, String option) {
                                for (int i = 0; i < statusValueList.size(); i++) {
                                    if (position == i) {
                                        strDeviceStatus = statusValueList.get(i);
                                        break;
                                    }

                                }
                                tv_status_choice.setText(option);
//                                Log.d("print", "onOptionPicked strDeviceStatus: " + strDeviceStatus);

                            }
                        });
                        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                iv_status_choice.setImageDrawable(getResources().getDrawable(R.drawable.ico_dn));
                            }
                        });
                        picker.show();
                    }
                }

            }


        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        typeLabelList.clear();
        typeValueList.clear();
        statusValueList.clear();
        statusLabelList.clear();
    }


}


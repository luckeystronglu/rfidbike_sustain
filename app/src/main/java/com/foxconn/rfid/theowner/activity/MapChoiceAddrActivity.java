package com.foxconn.rfid.theowner.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.model.EventBusMsg;
import com.foxconn.rfid.theowner.util.Utils;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfidbike_sustain.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by appadmin on 2017/1/12.
 */

public class MapChoiceAddrActivity extends BaseActivity implements Header.headerListener, OnGetGeoCoderResultListener {
    @BindView(R.id.map_addr_header)
    Header map_addr_header;
    @BindView(R.id.map_addr_choice)
    MapView map_addr_choice;
    @BindView(R.id.tv_install_address)
    TextView tv_install_address;

    private BaiduMap map;
    GeoCoder mSearch = null;

    private BDLocationListener myListener = new MyLocationListener();
    private LocationClient mLocationClient = null;
    private double curLongitude, curLatitude;
    private String geoAddress = "";
    private double geoLatitude = 39.963175,geoLongitude = 116.400244;// 反编译获得的地址
    private boolean enableChangeMapStatus=false;
    private Overlay mCircleCenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_addrchoice);
        ButterKnife.bind(this);
        initView();
        initMap();
    }

    private void initView() {
        Intent inte = getIntent();
        curLatitude = inte.getDoubleExtra("addrlat", 39.963175);
        curLongitude = inte.getDoubleExtra("addrlng", 116.400244);

        map_addr_header.setListener(this);

        //反编译地址
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(MapChoiceAddrActivity.this);

    }


    private void initMap() {

        map = map_addr_choice.getMap();
        initBaiduLocation();

        map_addr_choice.showScaleControl(false);
        map_addr_choice.showZoomControls(false);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        map.setMapStatus(msu);
        // 隐藏 百度地图logo
        View child = map_addr_choice.getChildAt(1);
        if (child != null && (child instanceof ImageView)) {
            child.setVisibility(View.INVISIBLE);
        }
        map.setBuildingsEnabled(false);
        map.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
        UiSettings settings = map.getUiSettings();
        settings.setCompassEnabled(true);
        settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势


        LatLng point = new LatLng(curLatitude, curLongitude);
        addMarker(point);

    }

    public void addMarker(LatLng markerLatLng){
        if (mCircleCenter != null) {
            mCircleCenter.remove();
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(markerLatLng);
        map.animateMapStatus(u);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.device_mark);
        OverlayOptions option = new MarkerOptions().position(markerLatLng).icon(bitmap);
        mCircleCenter = map.addOverlay(option);

        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(markerLatLng));
    }

    public void slideMapListener(){
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            LatLng startLng;
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                // TODO Auto-generated method stub
//                enableChangeMapStatus=true;



            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                // TODO Auto-generated method stub
                startLng = mapStatus.target;
                addMarker(startLng);
//				if(enableChangeMapStatus)
//				{
//					initCircleOverlay(mapStatus.target);
//				}
            }
        });
    }



    //获取定位
    private void initBaiduLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        getLocation();
        mLocationClient.start();
        slideMapListener();
    }


    //头部控件的点击监听
    @Override
    public void onClickLeftIcon() {
        finish();
    }

    @Override
    public void onClickRightIcon() {
        EventBusMsg msg = new EventBusMsg();
        msg.setMsgType(EventBusMsg.MsgType.MSG_ADDRESS_ID);
        Map<String, String> map = new HashMap<>();
        map.put("geoAddress",geoAddress);

        map.put("geoLatitude",new DecimalFormat("#.000000").format(geoLatitude));
        map.put("geoLongitude",new DecimalFormat("#.000000").format(geoLongitude));
        msg.setValue(map);
        EventBus.getDefault().post(msg);
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    MapChoiceAddrActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    //重写反编译地理编码的两个方法
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        geoAddress = reverseGeoCodeResult.getAddress();
        tv_install_address.setText(geoAddress);
        LatLng location = reverseGeoCodeResult.getLocation();
        if (location != null) {
            geoLatitude = location.latitude;
            geoLongitude = location.longitude;
        }


    }


    //通过地图定位监听类，获取定位经纬度
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            curLongitude = location.getLongitude();
            curLatitude = location.getLatitude();
            tv_install_address.setText(location.getAddrStr());
//            Log.d("print", "curLongitude: " + curLongitude + " curLatitude: " + curLatitude);
        }
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

    @Override
    protected void onDestroy() {
        // 卸载super的前后是没有却别的
        map_addr_choice.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map_addr_choice.onPause();
    }

    @Override
    protected void onResume() {
        map_addr_choice.onResume();
        super.onResume();
    }
}

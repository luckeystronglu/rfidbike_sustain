package com.foxconn.rfid.theowner.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.util.Utils;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfidbike_sustain.R;




/**
 * Created by appadmin on 2016/12/30.
 */

public class BaseStationMapActivity extends BaseActivity implements Header.headerListener {

//    private TextView basestationLocationTv;
    private MapView mMapView;
    private BaiduMap map;
    private Header map_header;
    private double lat,lng;
    private String addr;
    private Marker marker;
    private View showview;
    private TextView location_tv1;
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basestation_mapshow);
        initView();
        initMap();
    }


    //初始化控件
    private void initView() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",39.2);
        lng = intent.getDoubleExtra("lng",118.2);
        addr = intent.getStringExtra("addr");

        getIntent().getSerializableExtra("station");
//        basestationLocationTv = findViewByIds(R.id.basestation_location_tv);
        map_header = findViewByIds(R.id.map_header);
        map_header.setListener(this);
    }


    //初始化地图
    private void initMap() {
        mMapView = findViewByIds(R.id.map_mark);
        map = mMapView.getMap();

        final LatLng point = new LatLng(lat, lng);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.device_mark);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        marker = (Marker) map.addOverlay(option);
//        map.addOverlay(option);
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showview = LayoutInflater.from(BaseStationMapActivity.this).inflate(R.layout.map_marker_view, null);
                showview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        map.hideInfoWindow();
                    }
                });

                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                mInfoWindow = new InfoWindow(showview, point, 90);

                location_tv1 = (TextView) showview.findViewById(R.id.basestation_location_tv);
                location_tv1.setText(addr);
//                location_tv1.setText("中华人民共和国中华人民共和国中华人民共和国");
                //显示InfoWindow
                if (addr.equals("")) {
                    location_tv1.setText("此基站建立时没有设置地址");
                    map.showInfoWindow(mInfoWindow);
//                    ToastUtils.showTextToast(BaseStationMapActivity.this,"您添加基站时没有填写地址");
                }else {
                    map.showInfoWindow(mInfoWindow);
                }

                return true;

            }
        });
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mInfoWindow != null) {
                    map.hideInfoWindow();
                }

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });


        MapStatusUpdate mapstatusUpdatePoint = MapStatusUpdateFactory.newLatLngZoom(point, 14);
        map.setMapStatus(mapstatusUpdatePoint);

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);

        // 隐藏 百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView)) {
            child.setVisibility(View.INVISIBLE);
        }

//        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
//        map.setMapStatus(msu);

        map.setBuildingsEnabled(true);
        map.setCompassPosition(new Point(Utils.dpToPx(20, getResources()), Utils.dpToPx(20, getResources())));
        UiSettings settings = map.getUiSettings();
        settings.setOverlookingGesturesEnabled(false);// 屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);// 获取是否允许缩放手势返回:是否允许缩放手势
        settings.setCompassEnabled(true);

//        basestationLocationTv.setText(addr);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    //头部标题栏返回键的点击接口回调
    @Override
    public void onClickLeftIcon() {
        finish();
    }

    @Override
    public void onClickRightIcon() {

    }
}

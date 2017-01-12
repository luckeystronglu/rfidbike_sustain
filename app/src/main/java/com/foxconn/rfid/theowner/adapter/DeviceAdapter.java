package com.foxconn.rfid.theowner.adapter;

import android.support.v7.widget.RecyclerView;

import com.yzh.rfid.app.response.Device;
import com.yzh.rfidbike_sustain.R;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2016/12/28.
 */

public class DeviceAdapter extends BGARecyclerViewAdapter<Device.DeviceMessage> {


    public DeviceAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_device);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Device.DeviceMessage
            model) {
        helper.setText(R.id.tv_name, model.getDeviceIdDecimal()).setText(R.id.tv_address, String.format
                ("名称:%s", model.getDeviceName()));


    }

}

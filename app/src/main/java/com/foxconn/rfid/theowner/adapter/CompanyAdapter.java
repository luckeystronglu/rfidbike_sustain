package com.foxconn.rfid.theowner.adapter;

import android.support.v7.widget.RecyclerView;

import com.yzh.rfid.app.response.Company;
import com.yzh.rfidbike_sustain.R;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CompanyAdapter extends BGARecyclerViewAdapter<Company.CompanyMessage> {


    public CompanyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_company);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Company.CompanyMessage
            model) {
        helper.setText(R.id.tv_name, model.getName()).setText(R.id.tv_address, model.getAddress());


    }

}

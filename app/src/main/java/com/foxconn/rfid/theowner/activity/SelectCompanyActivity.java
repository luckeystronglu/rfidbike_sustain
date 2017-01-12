package com.foxconn.rfid.theowner.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.fragment.SelectCompanyListFragment;
import com.foxconn.rfid.theowner.util.ActivityManager;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfidbike_sustain.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectCompanyActivity extends BaseActivity implements Header.headerListener {
    @BindView(R.id.header)
    Header mHeader;
    private SelectCompanyListFragment mCompanyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        ButterKnife.bind(this);
        mHeader.setListener(this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mCompanyListFragment == null) {
            mCompanyListFragment = new SelectCompanyListFragment();
        }
        transaction.replace(R.id.ll_container, mCompanyListFragment);
        transaction.commit();
        ActivityManager.getInstance().addActivity(this);
    }


    @Override
    public void onClickLeftIcon() {
        super.onBackPressed();
    }

    @Override
    public void onClickRightIcon() {

    }
}

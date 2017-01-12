/**
 *
 */
package com.foxconn.rfid.theowner.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.model.CompanyUser;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.yzh.rfidbike_sustain.R;

import net.tsz.afinal.FinalDb;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * 个人信息
 *
 * @author WT00111
 */
public class PersonalInfoActivity extends BaseActivity implements Header.headerListener {
    @BindView(R.id.header)
    Header mHeader;
    @BindView(R.id.personal_names)
    TextView personal_names;
    @BindView(R.id.personal_id)
    TextView personal_id;
    @BindView(R.id.personal_phone)
    TextView personal_phone;
    @BindView(R.id.personal_addr)
    TextView personal_addr;
    @BindView(R.id.personal_email)
    TextView personal_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        ButterKnife.bind(this);
        initView();
        mHeader.setListener(this);
    }

    protected void initView() {

        final FinalDb mDb = FinalDb.create(this.getApplicationContext(), App.DB_NAME, true, App
                .DB_VERSION, BaseApplication.getInstance());
        //获取当前用户信息
        CompanyUser companyUser = CompanyUser.getCurUser(this);
        personal_names.setText(companyUser.getName());
        personal_id.setText(String.valueOf(companyUser.getCompanyId()));
        personal_phone.setText(companyUser.getPhone());
        personal_addr.setText(companyUser.getAddress());
        personal_email.setText(companyUser.getEmail());
//        Glide.with(this)
//                .load(companyUser.getPhoto())
//                .placeholder(R.drawable.headimage)
//                .transform(new GlideCircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .thumbnail(0.1f)
//                .crossFade(500)
//                .into(personinfo_img);
    }


    @Override
    public void onClickLeftIcon() {
        super.onBackPressed();
    }

    @Override
    public void onClickRightIcon() {

    }
}

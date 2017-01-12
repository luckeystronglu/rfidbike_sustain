package com.foxconn.rfid.theowner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foxconn.rfid.theowner.base.App;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.base.BaseApplication;
import com.foxconn.rfid.theowner.base.PreferenceData;
import com.foxconn.rfid.theowner.model.CompanyUser;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.BaiduPushUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.LoginRequest;
import com.yzh.rfid.app.response.LoginResponse;
import com.yzh.rfidbike_sustain.MainActivity;
import com.yzh.rfidbike_sustain.R;

import net.tsz.afinal.FinalDb;

import java.util.List;
import java.util.regex.Pattern;


public class LoginActivity extends BaseActivity {


    private String mUserName = "";
    private String mPassword = "";

    private EditText mUserNameView;
    private EditText mPasswordView;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mUserNameView = (EditText) findViewById(R.id.et_name);
        mPasswordView = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        CompanyUser companyUser = CompanyUser.getCurUser(this);
        if (companyUser != null) {
            mUserNameView.setText(companyUser.getName());
        }
    }


    public void attemptLogin() {

        mUserName = mUserNameView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(mUserName)) {
            ToastUtils.showTextToast(LoginActivity.this,"用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            ToastUtils.showTextToast(LoginActivity.this,"密码不能为空");
            return;
        }

        doSocket();
    }

    @Override
    protected void doSocket() {
        super.doSocket();
        final LoginRequest.LoginRequestMessage.Builder requestMessage = LoginRequest
                .LoginRequestMessage.newBuilder();
        requestMessage.setLoginName(mUserNameView.getText().toString());
        requestMessage.setPassword(mPasswordView.getText().toString());
        String channelId = BaiduPushUtils.getBaiduPushChannelId(context);
        requestMessage.setPushChannelId(channelId);
        requestMessage.setPhoneType(LoginRequest.LoginRequestMessage.PhoneType.AndroidPhone);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.COMMAND_ID_USER_LOGIIN, requestMessage.build()
                        .toByteArray());
            }

        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.COMMAND_ID_USER_LOGIIN) {
                LoginResponse.LoginResponseMessage loginResponseMessage = LoginResponse
                        .LoginResponseMessage.parseFrom(eventPackage.getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (loginResponseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(LoginActivity.this, loginResponseMessage.getErrorMsg()
                            .getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    CompanyUser companyUser = new CompanyUser();
                    companyUser.setCompanyId(loginResponseMessage.getCompanyId());
                    companyUser.setUserId((int) loginResponseMessage.getId());
                    companyUser.setName(loginResponseMessage.getName());
                    companyUser.setPassword(loginResponseMessage.getPassword());
                    companyUser.setSession(loginResponseMessage.getSession());
                    companyUser.setAddress(loginResponseMessage.getAddress());
                    companyUser.setEmail(loginResponseMessage.getEmail());
                    companyUser.setPhone(loginResponseMessage.getPhone());
                    FinalDb mDb = FinalDb.create(getApplicationContext(), App.DB_NAME, true, App
                            .DB_VERSION, BaseApplication.getInstance());

                    //判断是否存在，如存在就修改，不存在直接保存
                    List<CompanyUser> list = mDb.findAllByWhere(CompanyUser.class, "userId=" +
                            String
                                    .valueOf(loginResponseMessage.getId()));

                    if (list.size() > 0) {
                        mDb.update(companyUser);
                    } else {
                        mDb.save(companyUser);
                    }

                    PreferenceData.saveLoginAccount(this, loginResponseMessage.getId());
                    PreferenceData.saveSession(this, loginResponseMessage.getSession());
                    PreferenceData.saveLoginName(this, loginResponseMessage.getIdCard());
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是身份证，
     *
     * @param str
     * @return
     */
    public static boolean identity(String str) {
        Pattern pattern = Pattern
                .compile("^[1-9][0-9]{5}(19[0-9]{2}|200[0-9]|2010)(0[1-9]|1[0-2])" +
                        "(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9xX]$");
        return pattern.matcher(str).matches();
    }


}

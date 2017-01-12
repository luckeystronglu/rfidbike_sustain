package com.foxconn.rfid.theowner.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.foxconn.rfid.theowner.adapter.DeviceAdapter;
import com.foxconn.rfid.theowner.adapter.SearchDeviceAdapter;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.model.CompanyUser;
import com.foxconn.rfid.theowner.model.SearchDevice;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.util.logort.LogUtils;
import com.foxconn.rfid.theowner.util.logort.ToastUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.GetDevicesByUserDeviceIdRequest;
import com.yzh.rfid.app.response.Device;
import com.yzh.rfid.app.response.GetDevicesResponse;
import com.yzh.rfidbike_sustain.R;

import net.tsz.afinal.FinalDb;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;


public class SearchDeviceActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_tip)
    RecyclerView mRvTip;
    @BindView(R.id.rv_device)
    RecyclerView mRvDevice;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    private SearchDeviceAdapter mSearchDeviceAdapter;
    private DeviceAdapter mDeviceAdapter;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);
        ButterKnife.bind(this);
        mSearchDeviceAdapter = new SearchDeviceAdapter(this, mRvTip);
        mRvTip.setLayoutManager(new LinearLayoutManager(this));
        mRvTip.setAdapter(mSearchDeviceAdapter);

        mDeviceAdapter = new DeviceAdapter(mRvDevice);
        mRvDevice.setLayoutManager(new LinearLayoutManager(this));
        mRvDevice.setAdapter(mDeviceAdapter);

        final List<SearchDevice> deviceList = getSearchInfoList();
        mSearchDeviceAdapter.setData(deviceList);
        mSearchDeviceAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (position != 0) {
                    mEtSearch.setText(String.valueOf(mSearchDeviceAdapter.getItem(position)
                            .getDeviceIdDecimal()));
                    doSocket();
                }
            }
        });
        mSearchDeviceAdapter.setDeleteListener(new SearchDeviceAdapter.OnDeleteListener() {
            @Override
            public void onDelete(SearchDevice device, int position) {
                deleteSearchInfo(device);

            }

            @Override
            public void onDeleteAll() {
                deleteAllSearchInfo();

            }
        });
        mDeviceAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Device.DeviceMessage message = mDeviceAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("DeviceMessage", message);
                intent.setClass(mContext, ReadCardMessageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mEtSearch.getText().length() > 0) {
                    doSocket();
                    return true;
                }
                return false;
            }
        });
        mEtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFocus();
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<SearchDevice> devices = getSearchInfoList(editable.toString());
                mSearchDeviceAdapter.setData(devices, editable.toString());
            }
        });
        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mRvTip.setVisibility(View.VISIBLE);
                    mRvDevice.setVisibility(View.GONE);
                    showKeyBoard();
                } else {
                    mRvTip.setVisibility(View.GONE);
                    mRvDevice.setVisibility(View.VISIBLE);
                    hideKeyBoard();
                }
            }
        });

    }


    @OnClick({R.id.tv_cancel,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                hideKeyBoard();
                super.onBackPressed();
                break;

        }
    }


    @Override
    protected void doSocket() {
        super.doSocket();

        final GetDevicesByUserDeviceIdRequest.GetDevicesByUserDeviceIdRequestMessage
                .Builder requestMessage =
                GetDevicesByUserDeviceIdRequest
                        .GetDevicesByUserDeviceIdRequestMessage.newBuilder();
        CompanyUser companyUser = CompanyUser.getCurUser(this);
        requestMessage.setSession(companyUser.getSession());
        requestMessage.setUserId(companyUser.getUserId());
        requestMessage.setDeviceIdDecimal(Long.decode(mEtSearch.getText().toString()));
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.GET_SEARCH_DEVICE_LIST_DEPEND_DEVICE_ID,
                        requestMessage.build()
                                .toByteArray());
            }

        }.start();
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket
                    .GET_SEARCH_DEVICE_LIST_DEPEND_DEVICE_ID) {
                GetDevicesResponse.GetDevicesResponseMessage responseMessage =
                        GetDevicesResponse
                                .GetDevicesResponseMessage.parseFrom(eventPackage
                                .getCommandData());
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() == 0 && responseMessage
                        .getDeviceCount() != 0) {
                    setUnFocus();
                    mDeviceAdapter.setData(responseMessage.getDeviceList());
                    saveSearchInfo();

                } else {
                    LogUtils.logError("SearchDeviceActivity", responseMessage.getErrorMsg()
                            .getErrorMsg());
                    ToastUtils.showTextToast(mContext, "没有搜索结果");
                }


            }
        } catch (
                InvalidProtocolBufferException e
                )

        {
            e.printStackTrace();
        }

    }

    private void saveSearchInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FinalDb db = FinalDb.create(mContext);
                SearchDevice device = new SearchDevice();
                device.setDeviceIdDecimal(mEtSearch.getEditableText().toString());
                device.setTime(System.currentTimeMillis());
                try {
                    boolean b = db.saveBindId(device);
                    if (!b) {
                        db.update(device);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void deleteSearchInfo(final SearchDevice device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FinalDb db = FinalDb.create(mContext);
                    db.delete(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void deleteAllSearchInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FinalDb db = FinalDb.create(mContext);
                    db.deleteAll(SearchDevice.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setFocus() {
        mEtSearch.setFocusable(true);
        mEtSearch.setFocusableInTouchMode(true);
        mEtSearch.requestFocus();
    }

    private void setUnFocus() {
        mEtSearch.setFocusable(false);

    }

    private List<SearchDevice> getSearchInfoList(String keyWord) {

        FinalDb db = FinalDb.create(this);
        List<SearchDevice> devices = new ArrayList<>();
        List<SearchDevice> deviceList = db.findAll(SearchDevice.class, "time DESC");
        for (SearchDevice searchDevice : deviceList) {
            if (searchDevice.getDeviceIdDecimal().contains(keyWord)) {
                devices.add(searchDevice);
            }
        }
        return devices;
    }

    private List<SearchDevice> getSearchInfoList() {
        FinalDb db = FinalDb.create(this);
        List<SearchDevice> deviceList = db.findAll(SearchDevice.class, "time DESC");
        return deviceList;
    }

    private void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }

}

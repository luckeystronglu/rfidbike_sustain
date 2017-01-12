package com.foxconn.rfid.theowner.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foxconn.rfid.theowner.adapter.DeviceAdapter;
import com.foxconn.rfid.theowner.base.BaseActivity;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.foxconn.rfid.theowner.view.widgets.Header;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.GetDevicesByCompanyDeviceIdRequest;
import com.yzh.rfid.app.response.Device;
import com.yzh.rfid.app.response.GetDevicesResponse;
import com.yzh.rfidbike_sustain.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class DeviceListActivity extends BaseActivity implements Header.headerListener {

    @BindView(R.id.rv_baseStation)
    RecyclerView mRvDevice;
    @BindView(R.id.refresh_layout)
    PtrClassicFrameLayout mRefreshLayout;
    @BindView(R.id.header)
    Header mHeader;
    private List<Device.DeviceMessage> mDeviceList;
    private DeviceAdapter mDeviceAdapter;
    private String mSession;
    private long mCompanyId;
    private Context mContext = this;
    private boolean mPullToFresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        mHeader.setListener(this);
        final Intent intent = getIntent();
        mSession = intent.getStringExtra("session");
        mCompanyId = intent.getLongExtra("companyId", 0);

        mRvDevice.setLayoutManager(new LinearLayoutManager(this));
        mDeviceAdapter = new DeviceAdapter(mRvDevice);
        mRvDevice.setAdapter(mDeviceAdapter);
        doSocket();
        mRefreshLayout.setPtrHandler(
                new PtrHandler() {
                    @Override
                    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View
                            header) {

                        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content,
                                header);
                    }

                    @Override
                    public void onRefreshBegin(PtrFrameLayout frame) {
                        mPullToFresh = true;
                        doSocket();
                    }
                }
        );
        mDeviceAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Device.DeviceMessage message = mDeviceAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("DeviceMessage", message);
                intent.setClass(mContext, ReadCardMessageActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void doSocket() {
        super.doSocket();

        if (mPullToFresh && dlgWaiting.isShowing()) {
            dlgWaiting.dismiss();
        }

        final GetDevicesByCompanyDeviceIdRequest.GetDevicesByCompanyDeviceIdRequestMessage
                .Builder requestMessage =
                GetDevicesByCompanyDeviceIdRequest
                        .GetDevicesByCompanyDeviceIdRequestMessage.newBuilder();
        requestMessage.setSession(mSession);
        requestMessage.setCompanyId(mCompanyId);
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.GET_DEVICE_LIST_DEPEND_DEVICE_ID,
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
                    .GET_DEVICE_LIST_DEPEND_DEVICE_ID) {
                GetDevicesResponse.GetDevicesResponseMessage responseMessage =
                        GetDevicesResponse
                                .GetDevicesResponseMessage.parseFrom(eventPackage
                                .getCommandData());
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.refreshComplete();
                }
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(mContext, responseMessage.getErrorMsg()
                            .getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    mDeviceList = responseMessage.getDeviceList();
                    mDeviceAdapter.setData(mDeviceList);
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        mPullToFresh = false;
    }

    @Override
    protected void getDataErr() {
        super.getDataErr();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.refreshComplete();
        }
    }

    @Override
    public void onClickLeftIcon() {
        super.onBackPressed();
    }

    @Override
    public void onClickRightIcon() {

    }
}

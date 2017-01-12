package com.foxconn.rfid.theowner.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.foxconn.rfid.theowner.activity.DeviceListActivity;
import com.foxconn.rfid.theowner.activity.SubCompanyListActivity;
import com.foxconn.rfid.theowner.adapter.CompanyAdapter;
import com.foxconn.rfid.theowner.base.BaseFragment;
import com.foxconn.rfid.theowner.model.CompanyUser;
import com.foxconn.rfid.theowner.socket.SocketAppPacket;
import com.foxconn.rfid.theowner.socket.SocketClient;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yzh.rfid.app.request.GetCompanyListRequest;
import com.yzh.rfid.app.response.Company;
import com.yzh.rfid.app.response.GetCompanyListResponse;
import com.yzh.rfidbike_sustain.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class CompanyListFragment extends BaseFragment {
    @BindView(R.id.rv_company)
    RecyclerView mRvCompanyList;
    @BindView(R.id.refresh_layout)
    PtrClassicFrameLayout mRefreshLayout;
    private List<Company.CompanyMessage> mCompanyMessageList;
    private CompanyAdapter mCompanyAdapter;
    private boolean mPullToFresh;

    @Override
    protected int getContentView() {

        return R.layout.fragment_company;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        ButterKnife.bind(this, view);
        mRvCompanyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCompanyAdapter = new CompanyAdapter(mRvCompanyList);
        mRvCompanyList.setAdapter(mCompanyAdapter);
        mCompanyAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (mCompanyAdapter.getItem(position).getHasChild().equals("0")) {
                    Intent intent = new Intent();
                    CompanyUser companyUser = CompanyUser.getCurUser(getActivity());

                    intent.putExtra("session", companyUser.getSession());
                    intent.putExtra("companyId", mCompanyAdapter.getItem(position).getId());
                    intent.setClass(getActivity(), DeviceListActivity.class);
                    startActivity(intent);
                }
                if (mCompanyAdapter.getItem(position).getHasChild().equals("1")) {
                    Intent intent = new Intent();
                    CompanyUser companyUser = CompanyUser.getCurUser(getActivity());

                    intent.putExtra("userId", companyUser.getUserId());
                    intent.putExtra("session", companyUser.getSession());
                    intent.putExtra("companyId", mCompanyAdapter.getItem(position).getId());
                    intent.setClass(getActivity(), SubCompanyListActivity.class);
                    startActivity(intent);
                }
            }
        });


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
    }

    @Override
    protected void loadDatas() {
        super.loadDatas();
        doSocket();

    }

    @Override
    protected void doSocket() {
        super.doSocket();
        if (mPullToFresh && dlgWaiting.isShowing()) {
            dlgWaiting.dismiss();
        }

        final GetCompanyListRequest.GetCompanyListRequestMessage.Builder requestMessage =
                GetCompanyListRequest
                        .GetCompanyListRequestMessage.newBuilder();
        CompanyUser companyUser = CompanyUser.getCurUser(getActivity());
        requestMessage.setSession(companyUser.getSession());
        requestMessage.setCompanyId(0);
        requestMessage.setUserId(companyUser.getUserId());
        new Thread() {
            public void run() {
                SocketClient client;
                client = SocketClient.getInstance();
                client.send(SocketAppPacket.GET_COMPANY_LIST, requestMessage.build()
                        .toByteArray());
            }

        }.start();
    }

    @Override
    protected void getDataErr() {
        super.getDataErr();
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.refreshComplete();
        }
    }

    @Override
    public void onEventMainThread(SocketAppPacket eventPackage) {
        try {
            super.onEventMainThread(eventPackage);
            if (eventPackage.getCommandId() == SocketAppPacket.GET_COMPANY_LIST) {
                GetCompanyListResponse.GetCompanyListResponseMessage responseMessage =
                        GetCompanyListResponse
                                .GetCompanyListResponseMessage.parseFrom(eventPackage
                                .getCommandData());
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.refreshComplete();
                }
                if (dlgWaiting.isShowing()) {
                    dlgWaiting.dismiss();
                }
                mDlgWaitingHandler.removeMessages(MSG_cannt_get_data);
                if (responseMessage.getErrorMsg().getErrorCode() != 0) {
                    Toast.makeText(getActivity(), responseMessage.getErrorMsg()
                            .getErrorMsg(), Toast.LENGTH_LONG).show();
                } else {
                    mCompanyMessageList = responseMessage.getCompanyMessageList();
                    mCompanyAdapter.setData(mCompanyMessageList);
                }
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        mPullToFresh = false;

    }
}

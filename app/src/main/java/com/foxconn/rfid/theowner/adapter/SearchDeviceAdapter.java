package com.foxconn.rfid.theowner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.foxconn.rfid.theowner.model.SearchDevice;
import com.yzh.rfidbike_sustain.R;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/1/5.
 */

public class SearchDeviceAdapter extends BGARecyclerViewAdapter<SearchDevice> {
    private Context mContext;
    private String mKeyWord;
    private OnDeleteListener mDeleteListener;

    public SearchDeviceAdapter(Context context, RecyclerView recyclerView) {
        super(recyclerView);
        mContext = context;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.item_search_device_header;
        } else {
            return R.layout.item_search_device;
        }


    }

    @Override
    public void setData(List<SearchDevice> data) {
        super.setData(data);
        data.add(0, new SearchDevice());
    }

    @Override
    public void clear() {
        super.clear();
        getData().add(0, new SearchDevice());
    }

    @Override
    protected void fillData(final BGAViewHolderHelper helper, final int position, SearchDevice
            model) {
        if (position == 0) {
            TextView tvDeleteAll = helper.getTextView(R.id.tv_deleteAll);
            tvDeleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteListener.onDeleteAll();
                    clear();
                }
            });

        } else {
            String deviceIdDecimal = model.getDeviceIdDecimal();
            helper.setText(R.id.tv_device, deviceIdDecimal);
            if (mKeyWord != null && deviceIdDecimal.contains(mKeyWord)) {
                int start = deviceIdDecimal.indexOf(mKeyWord);
                int end = start + mKeyWord.length();
                SpannableString spannableString = new SpannableString(deviceIdDecimal);
                spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor
                        (R.color
                                .actionsheet_blue)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_device, spannableString);

            }
            TextView tvDelete = helper.getTextView(R.id.tv_delete);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SearchDevice device = getItem(position);
                    mDeleteListener.onDelete(device, position);
                    removeItem(position);

                }
            });
        }

    }


    public void setData(List<SearchDevice> data, String keyWord) {
        super.setData(data);
        data.add(0, new SearchDevice());
        mKeyWord = keyWord;
    }

    public void setDeleteListener(OnDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    public interface OnDeleteListener {
        void onDelete(SearchDevice device, int position);

        void onDeleteAll();
    }


}

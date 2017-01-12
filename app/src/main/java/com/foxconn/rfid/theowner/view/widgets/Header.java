package com.foxconn.rfid.theowner.view.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzh.rfidbike_sustain.R;

/**
 * Created by Administrator on 2016/12/27.
 */

public class Header extends RelativeLayout implements View.OnClickListener {
    private headerListener mListener;
    private final ImageView mIvLeft;
    private final ImageView mIvRight;
    private final TextView mTvTitle;
    private final LinearLayout mLLleft;
    private final RelativeLayout mRlRight;

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Header);
        int leftIcon = array.getResourceId(R.styleable.Header_leftIcon, R.drawable.ico_setting);
        int rightIcon = array.getResourceId(R.styleable.Header_rightIcon, R.drawable.ico_search_my);
        String title = array.getString(R.styleable.Header_headerTitle);
        int leftVisible = array.getInt(R.styleable.Header_leftIconVisible, 0);
        int rightVisible = array.getInt(R.styleable.Header_rightIconVisible, 0);
        array.recycle();
        View v = LayoutInflater.from(context).inflate(R.layout.header, this);
        mIvLeft = (ImageView) v.findViewById(R.id.iv_left);
        mIvRight = (ImageView) v.findViewById(R.id.iv_right);
        mTvTitle = (TextView) v.findViewById(R.id.tv_title);
        mLLleft = (LinearLayout) v.findViewById(R.id.ll_left);
        mRlRight = (RelativeLayout) v.findViewById(R.id.rl_right);
        mIvLeft.setImageResource(leftIcon);
        mIvRight.setImageResource(rightIcon);
        mTvTitle.setText(title);
        mLLleft.setOnClickListener(this);
        mRlRight.setOnClickListener(this);
        switch (leftVisible) {
            case 0:
                mIvLeft.setVisibility(VISIBLE);
                break;

            case 1:
                mIvLeft.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvLeft.setVisibility(GONE);

                break;
        }
        switch (rightVisible) {
            case 0:
                mIvRight.setVisibility(VISIBLE);
                break;
            case 1:
                mIvRight.setVisibility(INVISIBLE);

                break;
            case 2:
                mIvRight.setVisibility(GONE);

                break;
        }
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setTitle(int title) {
        mTvTitle.setText(title);
    }

    public void setListener(headerListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                if (mListener != null) {
                    mListener.onClickLeftIcon();
                }
                break;
            case R.id.rl_right:
                if (mListener != null) {
                    mListener.onClickRightIcon();
                }
                break;
            default:
                break;
        }

    }

    public interface headerListener {
        void onClickLeftIcon();

        void onClickRightIcon();
    }


}

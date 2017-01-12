/**
 * 
 */
package com.foxconn.rfid.theowner.view.widgets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 嵌套进ScrollView 的ListView
 * 
 * @author WT00111
 * 
 */
public class SVRecyclerView extends RecyclerView {
	public SVRecyclerView(Context context) {
		super(context);
	}

	public SVRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SVRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}

package com.art.uilibrary.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.art.uilibrary.R;
import com.art.uilibrary.wheel.OnWheelChangedListener;
import com.art.uilibrary.wheel.WheelView;
import com.art.uilibrary.wheel.adapter.ArrayWheelAdapter;

/**
 * @Author liuxian
 * @Date 2017/8/29 17:42
 * @Des
 */

public class WheelDialog extends OralDialog {

    private WheelView wheelView;
    private ArrayWheelAdapter<String> wheelAdapter;
    private TextView mWheelTitle;
    private String curentIndex;//当前的位置str

    public WheelDialog(Context context, String[] items, IOnDialogItemListener listener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.items = items;
        this.listener = listener;
    }

    public WheelDialog(String curentIndex, Context context, String[] items, IOnDialogItemListener listener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.items = items;
        this.listener = listener;
        this.curentIndex = curentIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheel);
        LinearLayout mLayout = (LinearLayout) findViewById(R.id.layout);

        wheelView = new WheelView(context);
        wheelAdapter = new ArrayWheelAdapter<>(context, items, wheelView);
        wheelView.setViewAdapter(wheelAdapter);
        wheelView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                mWheelTitle.setText(wheelAdapter.getItemText(newValue));
            }
        });
        mLayout.addView(wheelView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
        mWheelTitle = (TextView) findViewById(R.id.pop_title);
        wheelView.setInterpolator(new DecelerateInterpolator());
        if (curentIndex != null) {
            mWheelTitle.setText(curentIndex);
            wheelView.setCurrentItem(getArrayIndex(curentIndex, this.items));//设置初始状态
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.confirm) {
            listener.onDialogItemSelected(wheelView.getCurrentItem());
        }
    }

    public void invalidateData(String[] items) {
        this.items = items;
        wheelAdapter = new ArrayWheelAdapter<>(context, items);
        wheelView.setViewAdapter(wheelAdapter);
    }

    /**
     * 展示第几个item
     *
     * @param index
     */
    public void setItemSelected(int index) {
        wheelView.setCurrentItem(index);
    }

    /**
     * 展示这个item
     *
     * @param item
     */
    public void setItemSelected(String item) {
        int index = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                index = i;
                break;
            }
        }
        wheelView.setCurrentItem(index);
    }

    /**
     * 根据元素获取数组中的下标
     *
     * @param str
     * @param items
     * @return
     */
    public int getArrayIndex(String str, String[] items) {
        int k = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(str)) {
                k = i;
            }
        }
        return k;
    }
}

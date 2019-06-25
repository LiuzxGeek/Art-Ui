package com.art.uilibrary.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.art.uilibrary.R;

/**
 * @Author liuxian
 * @Date 2017/9/15 11:31
 * @Des
 */

public class OptionDialog extends OralDialog {
    private Button item1Btn;
    private Button item2Btn;
    private RelativeLayout item1Layout;
    private RelativeLayout item2Layout;
    private RelativeLayout cancelLayout;
    private LinearLayout mTitleLayout;
    private TextView mTitleTv;
    private RelativeLayout mRoot;
    private final IOnDialogItemListener listener;

    public OptionDialog(Context context, IOnDialogItemListener listener) {
        super(context);
        // TODO Auto-generated constructor stub
        this.listener = listener;
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    public void invalidate(String[] items) {
        if (items.length == 1) {
            item1Btn.setText(items[0]);
            item2Layout.setVisibility(View.GONE);
        } else if (items.length == 2) {
            item1Btn.setText(items[0]);
            item2Btn.setText(items[1]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_option);
        item1Layout = findViewById(R.id.item1_layout);
        item2Layout = findViewById(R.id.item2_layout);
        cancelLayout = findViewById(R.id.cancel_layout);
        mTitleLayout = findViewById(R.id.title_layout);
        mTitleTv = findViewById(R.id.title_tv);
        mRoot = findViewById(R.id.root);

        item1Btn = findViewById(R.id.item1_btn);
        item2Btn = findViewById(R.id.item2_btn);

        item1Layout.setOnClickListener(this);
        item2Layout.setOnClickListener(this);
        cancelLayout.setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        mRoot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.item1_layout) {
            listener.onDialogItemSelected(0);
        } else if (v.getId() == R.id.item2_layout) {
            listener.onDialogItemSelected(1);
        } else if (v.getId() == R.id.root || v.getId() == R.id.btn_cancel || v.getId() == R.id.cancel_layout) {
            disDialog();
        }
    }

    public void setTitleTv(Object res) {
        mTitleLayout.setVisibility(View.VISIBLE);
        if (res instanceof String) {
            mTitleTv.setText(res.toString());
        } else if (res instanceof Integer) {
            mTitleTv.setText((Integer) res);
        }
    }

    public View getChild(int position) {
        if (position == 0) return item1Layout;
        if (position == 1) return item2Layout;
        return mRoot;
    }
}



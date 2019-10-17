package com.art.uilibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.art.uilibrary.R;

/**
 * @Author liuxian
 * @Date 2017/8/29 13:50
 * @Des
 */

public class LoadingDialog {
    private Context mContext;
    private static Dialog mDialog;

    public LoadingDialog(Context context) {
        this.mContext = context;
    }


    public LoadingDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        // 定义Dialog布局和参数
        mDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setContentView(view);
        return this;
    }


    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    public boolean isShowing() {
        if (mDialog == null) {
            return false;
        }
        return mDialog.isShowing();
    }

}

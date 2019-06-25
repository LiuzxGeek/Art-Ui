package com.art.uilibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.art.uilibrary.R;

/**
 * @Author liuxian
 * @Date 2017/8/29 13:50
 * @Des
 */

public class AlertDialog {
    private Context mContext;
    private Dialog mDialog;
    private View mLayout;
    private TextView mTitleTv;
    private TextView mMsgTv;
    private TextView mNegBtn;
    private TextView mPosBtn;
    private View mLine;

    public AlertDialog(Context context) {
        this.mContext = context;
    }

    public AlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_alert, null);
        // 获取自定义Dialog布局中的控件
        mLayout = view.findViewById(R.id.lLayout_bg);
        mTitleTv = view.findViewById(R.id.txt_title);
        mMsgTv = view.findViewById(R.id.txt_msg);
        mNegBtn = view.findViewById(R.id.btn_neg);
        mPosBtn = view.findViewById(R.id.btn_pos);
        mLine = view.findViewById(R.id.line);

        // 定义Dialog布局和参数
        mDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        mDialog.setContentView(view);
        return this;
    }

    public AlertDialog setTitle(Object title) {
        if (title instanceof String) {
            if (!TextUtils.isEmpty(title.toString())) {
                mTitleTv.setText(title.toString());
                setTitleVisible(true);
            } else setTitleVisible(false);
        } else if (title instanceof Integer) {
            mTitleTv.setText((Integer) title);
        }
        return this;
    }

    public AlertDialog setTitleImg(int titleImg) {
        Drawable drawable = mContext.getResources().getDrawable(titleImg);
        if (titleImg != 0) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTitleTv.setCompoundDrawables(drawable, null, null, null);
        }
        return this;
    }


    public AlertDialog setMsg(Object msg) {
        if (msg instanceof Integer) {
            mMsgTv.setText((Integer) msg);
        } else if (msg instanceof String) {
            if (!TextUtils.isEmpty(msg.toString()))
                mMsgTv.setText(msg.toString());
        }
        return this;
    }

    public AlertDialog setMsgImg(int msgImg) {
        Drawable drawable = mContext.getResources().getDrawable(msgImg);
        if (msgImg != 0) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mMsgTv.setCompoundDrawables(drawable, null, null, null);
        }
        return this;
    }

    public AlertDialog setCancelable(boolean cancel) {
        mDialog.setCancelable(cancel);
        return this;
    }

    public AlertDialog setPositiveButton(Object text, final View.OnClickListener listener) {
        if (text instanceof String) {
            if (!TextUtils.isEmpty(text.toString()))
                mPosBtn.setText(text.toString());
        } else if (text instanceof Integer) {
            mPosBtn.setText((Integer) text);
        }
        mPosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(Object msg, final View.OnClickListener listener) {
        if (msg instanceof Integer) {
            mNegBtn.setText((Integer) msg);
        } else if (msg instanceof String) {
            if (!TextUtils.isEmpty(msg.toString()))
                mNegBtn.setText(msg.toString());
        }
        mNegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                mDialog.dismiss();
            }
        });
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

    /**
     * 一些透露给外部设置dialog属性的方法
     */
    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mDialog.setOnDismissListener(listener);
    }


    public boolean isShowing() {
        if (mDialog == null) {
            return false;
        }
        return mDialog.isShowing();
    }

    public void setTitleVisible(boolean visible) {
        mTitleTv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setNegInVisible() {
        mNegBtn.setVisibility(View.GONE);
        mLine.setVisibility(View.GONE);
    }

    public void setPosInVisible() {
        mPosBtn.setVisibility(View.GONE);
        mLine.setVisibility(View.GONE);
    }
}

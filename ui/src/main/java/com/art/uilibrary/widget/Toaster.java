package com.art.uilibrary.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.art.uilibrary.R;

import java.text.MessageFormat;

/**
 * @Author liuxian
 * @Date 2017/8/9 14:51
 * @Des
 */

public class Toaster {
    private static Toaster sInstance;// 静态变量
    private final Context mContext;// 成员变量
    private final Handler mHandler;// 成员变量
    private final int duration = Toast.LENGTH_SHORT;// Toast显示时间

    public static final int SYSTEM = 0;

    public static final int CUSTOM = 1;

    public static final int WARN = 2;

    public static final int SUCCESS = 1;//操作成功，用于区分显示图标
    public static final int FAIL = 0;//操作失败，用于区分显示图标

    private Toaster(Context context) {
        this.mContext = context;
        this.mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SYSTEM:
                        Toast.makeText(mContext, (String) msg.obj, duration).show();
                        break;
                    case CUSTOM:
                        customToastShow(msg.obj);
                        break;
                    case WARN:
                        warnToastShow(msg.arg1, msg.obj);
                        break;
                }

            }
        };
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new Toaster(context);
        } else {
            throw new IllegalStateException("Toaster has been inited.");
        }
    }

    public static Toaster getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Toaster has not been inited.");
        } else {
            return sInstance;
        }
    }

    /**
     * 显示string文件里的内容
     */
    public void showToast(Object resContent) {
        if (resContent == null) {
            return;
        }
        if (resContent instanceof Integer) {
            Message msg = Message.obtain(mHandler, 0, mContext.getString((Integer) resContent));
            msg.what = SYSTEM;
            msg.sendToTarget();
        } else {
            Message msg = Message.obtain(mHandler, 0, resContent);
            msg.what = SYSTEM;
            msg.sendToTarget();
        }

    }

    /**
     * 显示string文件里的内容
     *
     * @param res
     */
    public void showToast(int res, int dur) {
        Message msg = Message.obtain(mHandler, 0, mContext.getString(res));
        msg.arg1 = dur;
        msg.what = SYSTEM;
        msg.sendToTarget();
    }

    /**
     * 直接显示文字
     *
     * @param str
     */

    public void showToast(String str, int dur) {
        Message msg = Message.obtain(mHandler, 0, str);
        msg.arg1 = dur;
        msg.what = SYSTEM;
        msg.sendToTarget();
    }

    /**
     * 显示相应格式的内容
     *
     * @param formatRes
     * @param params
     */

    public void showToast(int formatRes, Object[] params) {
        Message msg = Message.obtain(mHandler, 0, MessageFormat.format(mContext.getString(formatRes), params));
        msg.what = SYSTEM;
        msg.sendToTarget();
    }

    /**
     * 显示相应格式的内容
     *
     * @param toastMessage
     */

    public void showCustomToast(Object toastMessage) {
        Message msg = Message.obtain(mHandler, 0, toastMessage);
        msg.what = CUSTOM;
        msg.sendToTarget();
    }

    private void customToastShow(Object res) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_custom_view, null);
        TextView text = layout.findViewById(R.id.text);
        if (res instanceof Integer) {
            text.setText((Integer) res);
        } else {
            text.setText(res.toString());
        }
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 显示相应格式的内容
     *
     * @param toastMessage
     */

    public void showWarnToast(int status, Object toastMessage) {
        Message msg = Message.obtain(mHandler, 0, toastMessage);
        msg.arg1 = status;
        msg.what = WARN;
        msg.sendToTarget();
    }

    private void warnToastShow(int status, Object res) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_warn_view, null);
        TextView text = layout.findViewById(R.id.text);
        ImageView imageView = layout.findViewById(R.id.image);
        if (res instanceof Integer) {
            text.setText((Integer) res);
        } else {
            text.setText(res.toString());
        }
        if (status == FAIL) {
            imageView.setImageResource(R.mipmap.icon_warn);
        } else {
            imageView.setImageResource(R.mipmap.icon_ok);
        }
        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}

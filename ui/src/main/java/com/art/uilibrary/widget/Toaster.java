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
    /**
     * 静态变量
     */
    private static Toaster sInstance;
    /**
     * 成员变量
     */
    private final Context mContext;
    /**
     * 成员变量
     */
    private final Handler mHandler;

    public static final int SYSTEM = 0;

    public static final int CUSTOM = 1;

    public static final int WARN = 2;

    public static final int LONG = 3;

    /**
     * 操作失败，用于区分显示图标
     */
    public static final int FAIL = 0;
    /**
     * 操作成功，用于区分显示图标
     */
    public static final int SUCCESS = 1;

    private Toaster(Context context) {
        this.mContext = context;
        this.mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SYSTEM:
                        Toast.makeText(mContext, (String) msg.obj, msg.arg1).show();
                        break;
                    case CUSTOM:
                        Object res = msg.obj;
                        View layout = LayoutInflater.from(mContext).inflate(R.layout.toast_custom_view, null);
                        TextView text = layout.findViewById(R.id.text);
                        if (res instanceof Integer) {
                            text.setText((Integer) res);
                        } else {
                            text.setText(res.toString());
                        }
                        Toast toast = new Toast(mContext);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        break;
                    case WARN:
                        res = msg.obj;
                        int status = msg.arg1;
                        layout = LayoutInflater.from(mContext).inflate(R.layout.toast_warn_view, null);
                        text = layout.findViewById(R.id.text);
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
                        toast = new Toast(mContext);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);
                        toast.show();
                        toast.setDuration(Toast.LENGTH_SHORT);

                        break;
                    case LONG:
                        toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                        String message = "";
                        if (msg.obj instanceof String) {
                            message = (String) msg.obj;
                        } else if (msg.obj instanceof Integer) {
                            message = mContext.getString((Integer) msg.obj);
                        }
                        toast.setText(message);
                        toast.show();
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
        showToast(resContent, Toast.LENGTH_SHORT);
    }

    /**
     * 显示内容
     *
     * @param resContent
     * @param duration
     */
    public void showToast(Object resContent, int duration) {
        if (resContent == null) {
            return;
        }
        Object obj = resContent;
        if (resContent instanceof Integer) {
            obj = mContext.getString((Integer) resContent);
        }
        Message msg = Message.obtain(mHandler, 0, obj);
        msg.what = SYSTEM;
        msg.arg1 = duration;
        msg.sendToTarget();
    }

    /**
     * 显示相应格式的内容
     *
     * @param formatRes
     * @param params
     */

    public void showToast(int formatRes, Object[] params) {
        Object obj = MessageFormat.format(mContext.getString(formatRes), params);
        Message msg = Message.obtain(mHandler, 0, obj);
        msg.what = SYSTEM;
        msg.arg1 = Toast.LENGTH_SHORT;
        msg.sendToTarget();
    }

    /**
     * 个性提醒
     *
     * @param toastMessage
     */

    public void showCustomToast(Object toastMessage) {
        Message msg = Message.obtain(mHandler, 0, toastMessage);
        msg.what = CUSTOM;
        msg.sendToTarget();
    }

    /**
     * 警告提醒
     *
     * @param toastMessage
     */

    public void showWarnToast(int status, Object toastMessage) {
        Message msg = Message.obtain(mHandler, 0, toastMessage);
        msg.arg1 = status;
        msg.what = WARN;
        msg.sendToTarget();
    }

    /**
     * 长时间提醒
     *
     * @param toastMessage
     */
    public void showLongToast(Object toastMessage) {
        Message msg = Message.obtain(mHandler, 0, toastMessage);
        msg.what = LONG;
        msg.sendToTarget();
    }

}

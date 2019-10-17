package com.art.uilibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.art.uilibrary.R;
import com.art.uilibrary.widget.dialog.AlertDialog;
import com.art.uilibrary.widget.dialog.LoadingDialog;

/**
 * @Author liuxian
 * @Date 2017/8/8 17:16
 * @Des
 */

public class UiUtils {
    private static ProgressDialog mProgressDialog;
    private static LoadingDialog mLoadingDialog;

    public static void showLoading(Context context) {
        String title = context.getResources().getString(R.string.str_prompt);
        String msg = context.getResources().getString(R.string.str_loading);
        showLoading(context, title, msg);
    }

    public static void showLoading(Context context, CharSequence title, CharSequence message) {
        mLoadingDialog = new LoadingDialog(context).builder();
        mLoadingDialog.show();
    }

    public static void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public static void showConfirmDialog(Context context, final Object resTarget, final IOnConfirmDialogClickListener listener,
                                         Object negMsg, Object posMsg, boolean negInVisible, boolean posInVisible) {
        AlertDialog mAlertDialog = new AlertDialog(context).builder();
        mAlertDialog.setMsg(resTarget);
        mAlertDialog.setNegativeButton(negMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mAlertDialog.setPositiveButton(posMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onDialogConfirm(resTarget);
            }
        });
        if (negInVisible)
            mAlertDialog.setNegInVisible();
        if (posInVisible)
            mAlertDialog.setPosInVisible();
        mAlertDialog.show();
    }

    public static void showConfirmDialog(Context context,final Object titelResTarget, final Object resTarget, final IOnConfirmDialogClickListener listener,
                                         Object negMsg, Object posMsg, boolean negInVisible, boolean posInVisible) {
        AlertDialog mAlertDialog = new AlertDialog(context).builder();
        mAlertDialog.setTitle(titelResTarget);
        mAlertDialog.setMsg(resTarget);
        mAlertDialog.setNegativeButton(negMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mAlertDialog.setPositiveButton(posMsg, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onDialogConfirm(resTarget);
            }
        });
        if (negInVisible)
            mAlertDialog.setNegInVisible();
        if (posInVisible)
            mAlertDialog.setPosInVisible();
        mAlertDialog.show();
    }

    public static void showConfirmDialog(Context context, final Object resTarget, final IOnConfirmDialogClickListener listener,
                                         Object negMsg, Object posMsg) {
        showConfirmDialog(context, resTarget, listener, negMsg, posMsg, false, false);
    }

    public static void showConfirmDialog(Context context, Object resTarget, Object negMsg, boolean negInVisible, boolean posInVisible) {
        showConfirmDialog(context, resTarget, null, negMsg, "", negInVisible, posInVisible);
    }

    public interface IOnConfirmDialogClickListener {
        void onDialogConfirm(Object resTarget);
    }

    public static void setRecycleStyle(Context context, RecyclerView recyclerView, RecycleStyle recycleStyle, int spanCount) {
        if (recycleStyle == RecycleStyle.RECYCLE_LV) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listView
        } else if (recycleStyle == RecycleStyle.RECYCLE_GV) {
            recyclerView.setLayoutManager(new GridLayoutManager(context, spanCount));//这里用线性宫格显示 类似于gridView
        } else if (recycleStyle == RecycleStyle.RECYCLE_WF) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        }
    }

    public enum RecycleStyle {
        RECYCLE_LV, RECYCLE_GV, RECYCLE_WF
    }
}

package com.art.uilibrary.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.art.uilibrary.R;

/**
 * @Author liuxian
 * @Date 2017/8/29 17:42
 * @Des
 */

public class OralDialog extends Dialog implements View.OnClickListener {
    Context context;
    protected String[] items;
    IOnDialogItemListener listener;

    protected OralDialog(Context context) {
        // TODO Auto-generated constructor stub
        super(context, R.style.BottomDialogStyle);
        this.context = context;
    }


    public void disDialog() {
        dismiss();
    }

    public void showDialog() {
        show();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        disDialog();
    }

    public interface IOnDialogItemListener {// 做列表选择使用

        void onDialogItemSelected(int index);
    }
}

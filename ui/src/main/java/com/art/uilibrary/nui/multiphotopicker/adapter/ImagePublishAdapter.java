package com.art.uilibrary.nui.multiphotopicker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.art.uilibrary.R;
import com.art.uilibrary.base.OralAdapter;
import com.art.uilibrary.nui.multiphotopicker.model.ImageItem;
import com.art.uilibrary.nui.multiphotopicker.util.CustomConstants;
import com.art.uilibrary.nui.multiphotopicker.util.ImageDisplayer;

public class ImagePublishAdapter extends OralAdapter<ImageItem> {

    public ImagePublishAdapter(Context context) {
        super(context);
    }

    public int getCount() {
        // 多返回一个用于展示添加图标
        if (list == null) {
            return 1;
        } else if (list.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return CustomConstants.MAX_IMAGE_SIZE;
        } else {
            return list.size() + 1;
        }
    }

    public Object getItem(int position) {
        if (list != null && list.size() == CustomConstants.MAX_IMAGE_SIZE) {
            return list.get(position);
        } else if (list == null || position - 1 < 0 || position > list.size()) {
            return null;
        } else {
            return list.get(position - 1);
        }
    }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
        convertView = getInflateView(R.layout.up_item_publish);
        ImageView imageIv = convertView.findViewById(R.id.item_grid_image);
        ImageButton postreq_ib =convertView.findViewById(R.id.postreq_ib);
        if (isShowAddItem(position)) {
            imageIv.setImageResource(R.mipmap.icon_oval_selected);
        } else {
            final ImageItem item = list.get(position);
            if (item.isUpload == 0) {
                postreq_ib.setVisibility(View.GONE);
            } else {
                postreq_ib.setVisibility(View.VISIBLE);
                if (item.isUpload == 1) {
                    // 成功
//                    postreq_ib.setBackgroundResource(R.drawable.img_check_orange);
                } else {
                    // 失败
//                    postreq_ib.setBackgroundResource(R.drawable.img_image_close);
                }
            }
            ImageDisplayer.getInstance(mContext).displayBmp(imageIv, item.thumbnailPath, item.sourcePath);
        }
        imageIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                listener.onItemSelected(position);
            }
        });
        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = list == null ? 0 : list.size();
        return position == size;
    }

    private IOnItemSelectedListener listener;

    public void setItemSelectedListener(IOnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface IOnItemSelectedListener {
        public void onItemSelected(int position);
    }
}

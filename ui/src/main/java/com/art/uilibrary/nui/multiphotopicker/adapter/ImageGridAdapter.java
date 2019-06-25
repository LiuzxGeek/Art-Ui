package com.art.uilibrary.nui.multiphotopicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.uilibrary.R;
import com.art.uilibrary.base.OralAdapter;
import com.art.uilibrary.nui.multiphotopicker.model.ImageItem;
import com.art.uilibrary.nui.multiphotopicker.util.ImageDisplayer;

public class ImageGridAdapter extends OralAdapter<ImageItem> {

    public ImageGridAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.up_item_image, null);
            mHolder = new ViewHolder();
            mHolder.imageIv = (ImageView) convertView.findViewById(R.id.image);
            mHolder.selectedIv = (ImageView) convertView.findViewById(R.id.selected_tag);
            mHolder.selectedBgTv = (TextView) convertView.findViewById(R.id.image_selected_bg);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ImageItem item = list.get(position);
        ImageDisplayer.getInstance(mContext).displayBmp(mHolder.imageIv, item.thumbnailPath, item.sourcePath);
        if (item.isSelected) {
            mHolder.selectedIv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_oval_selected));
            mHolder.selectedIv.setVisibility(View.VISIBLE);
        } else {
            mHolder.selectedIv.setImageDrawable(null);
            mHolder.selectedIv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView imageIv;
        private ImageView selectedIv;
        private TextView selectedBgTv;
    }

}

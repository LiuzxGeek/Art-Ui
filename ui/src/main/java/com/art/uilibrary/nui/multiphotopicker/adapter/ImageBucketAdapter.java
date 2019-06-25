package com.art.uilibrary.nui.multiphotopicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.art.uilibrary.R;
import com.art.uilibrary.base.OralAdapter;
import com.art.uilibrary.nui.multiphotopicker.model.ImageBucket;
import com.art.uilibrary.nui.multiphotopicker.util.ImageDisplayer;

public class ImageBucketAdapter extends OralAdapter<ImageBucket> {

    public ImageBucketAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.up_item_bucket, null);
            mHolder = new ViewHolder();
            mHolder.coverIv = (ImageView) convertView.findViewById(R.id.cover);
            mHolder.titleTv = (TextView) convertView.findViewById(R.id.title);
            mHolder.countTv = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final ImageBucket item = list.get(position);
        if (item.imageList != null && item.imageList.size() > 0) {
            String thumbPath = item.imageList.get(0).thumbnailPath;
            String sourcePath = item.imageList.get(0).sourcePath;
            ImageDisplayer.getInstance(mContext).displayBmp(mHolder.coverIv, thumbPath, sourcePath);
        } else {
            mHolder.coverIv.setImageBitmap(null);
        }
        mHolder.titleTv.setText(item.bucketName);
        mHolder.countTv.setText(item.count + "张");
        return convertView;
    }

    static class ViewHolder {
        private ImageView coverIv;
        private TextView titleTv;
        private TextView countTv;
    }

}

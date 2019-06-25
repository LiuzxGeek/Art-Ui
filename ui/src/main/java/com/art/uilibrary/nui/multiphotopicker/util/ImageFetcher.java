package com.art.uilibrary.nui.multiphotopicker.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.Log;

import com.art.uilibrary.nui.multiphotopicker.model.ImageBucket;
import com.art.uilibrary.nui.multiphotopicker.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 图片工具类
 */

public final class ImageFetcher {
    private static ImageFetcher instance;
    /**
     * 是否已加载过了相册集合
     */
    private boolean hasBuildImagesBucketList = false;
    private Context mContext;
    private final LinkedHashMap<String, ImageBucket> mBucketList = new LinkedHashMap<>();
    private final HashMap<String, String> mThumbnailList = new HashMap<>();

    private ImageFetcher() {
    }

    private ImageFetcher(Context context) {
        this.mContext = context;
    }

    public static ImageFetcher getInstance(Context context) {
        // if(context==null)
        // context = MyApplication.getMyApplicationContext(); TODO

        if (instance == null) {
            synchronized (ImageFetcher.class) {
                instance = new ImageFetcher(context);
            }
        }
        return instance;
    }

    /**
     * 得到图片集
     *
     * @param refresh
     * @return
     */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        mBucketList.clear();
        mThumbnailList.clear();
        //refresh为false时，当hasBuildImagesBucketList为true后，不再build
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<ImageBucket> tmpList = new ArrayList<>();
        Iterator<Entry<String, ImageBucket>> itr = mBucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ImageBucket> entry = itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 得到图片集
     */
    private void buildImagesBucketList() {
        Cursor cur = null;
        try {
            long startTime = System.currentTimeMillis();

            // 构造缩略图索引
            getThumbnail();
            String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC ";
            // 构造相册索引
            String columns[] = {Media._ID, Media.BUCKET_ID, Media.DATA, Media.BUCKET_DISPLAY_NAME};
            // 得到一个游标
            cur = mContext.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, columns, null, null, sortOrder);
            //int position = 0;
            if (cur.moveToFirst()) {
                // 获取指定列的索引
                int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
                int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);

                do {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    File file = new File(path);
                    if (file.exists() && file.length() > 0 && isSupportType(path)) {
                        String bucketName = cur.getString(bucketDisplayNameIndex);
                        String bucketId = cur.getString(bucketIdIndex);

                        ImageBucket bucket = mBucketList.get(bucketId);
                        if (bucket == null) {
                            bucket = new ImageBucket();
                            mBucketList.put(bucketId, bucket);
                            bucket.imageList = new ArrayList<>();
                            bucket.bucketName = bucketName;
                        }
                        bucket.count++;
                        ImageItem imageItem = new ImageItem();
                        imageItem.imageId = _id;
                        imageItem.sourcePath = path;
                        imageItem.thumbnailPath = mThumbnailList.get(_id);
                        imageItem.index = bucket.imageList.size();//index与当前文件夹的顺序一样
                        bucket.imageList.add(imageItem);
                    } else {
                        Log.e("ImageFetcher", path);
                    }
                } while (cur.moveToNext());
            }

            hasBuildImagesBucketList = true;
            long endTime = System.currentTimeMillis();
            Log.d(ImageFetcher.class.getName(), "use time: " + (endTime - startTime) + " ms");
        } finally {
            if (cur != null)
                cur.close();
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor cursor = null;
        try {
            String[] projection = {Thumbnails.IMAGE_ID, Thumbnails.DATA};
            cursor = mContext.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
            getThumbnailColumnData(cursor);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                int image_id = cur.getInt(image_idColumn);
                String path = cur.getString(dataColumn);
                File file = new File(path);
                if (file.exists() && file.length() > 0 && isSupportType(path))
                    mThumbnailList.put("" + image_id, path);
                else {
                    Log.e("ImageFetcher", path);
                }
            } while (cur.moveToNext());
        }
    }

    public List<ImageItem> getSystemPhotoList(Context context) {
        List<ImageItem> result = new ArrayList<>();
        Uri uri = Media.EXTERNAL_CONTENT_URI;

        ContentResolver contentResolver = context.getContentResolver();
        String columns[] = {Media._ID, Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC ";
        Cursor cursor = contentResolver.query(uri, columns, null, null, sortOrder);
        if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
        int position = 0;
        while (cursor.moveToNext()) {
            int index = cursor
                    .getColumnIndexOrThrow(Media.DATA);
            String path = cursor.getString(index); // 文件地址
            File file = new File(path);
            ImageItem imageItem;
            if (file.exists() && file.length() > 0 && isSupportType(path)) {
                imageItem = new ImageItem();
                imageItem.sourcePath = path;
                int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
                String _id = cursor.getString(photoIDIndex);
                imageItem.thumbnailPath = mThumbnailList.get(_id);
                imageItem.index = position++;
                result.add(imageItem);
            } else {
                Log.e("ImageFetcher", path);
            }
        }

        return result;
    }

    //png/jpg/jpeg/gif
    public static boolean isSupportType(String path) {
        if (TextUtils.isEmpty(path)) return false;
        String lowPath = path.toLowerCase();
        return lowPath.endsWith("png") || lowPath.endsWith("jpg") || lowPath.endsWith("jpeg") || lowPath.endsWith("gif");
    }
}

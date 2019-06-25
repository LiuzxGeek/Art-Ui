package com.art.uilibrary.nui.multiphotopicker.model;

import java.util.List;

/**
 * 相册对象
 */
public class ImageBucket {
    public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;
    public boolean selected = false;

    @Override
    public String toString() {
        return "ImageBucket [count=" + count + ", bucketName=" + bucketName + ", imageList=" + imageList + ", selected=" + selected + "]";
    }


}

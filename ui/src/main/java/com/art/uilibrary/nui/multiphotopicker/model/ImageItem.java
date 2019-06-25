package com.art.uilibrary.nui.multiphotopicker.model;

import java.io.Serializable;

/**
 * 图片对象
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String thumbnailPath; // 上传图片的压缩图路径
    public String sourcePath; // 图片的原路径
    public int isUpload; // 0未上传 1上传成功 2上传失败
    private String uploadId; // 上传成功后返回的id
    public String upFilePath;// 图片上传时候压缩保存的路径（这个是要上传的图片的路径）
    public String upSuccessHttpPath;// 图片上传成功后的路径
    public boolean isSelected = false;
    public int mSelectIndex = 0;
    public int index = 0;

    @Override
    public String toString() {
        return "ImageItem [imageId=" + imageId + ", thumbnailPath=" + thumbnailPath + ", sourcePath=" + sourcePath + ", isUpload=" + isUpload + ", uploadId="
                + uploadId + ", upFilePath=" + upFilePath + ", isSelected=" + isSelected + "]";
    }

}

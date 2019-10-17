package com.art.uilibrary.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * @Author liuxian
 * @Date 2018/1/815:45
 * @Desc
 */

public class UiSdUtils {
    /**
     * 存储卡中的根目录
     */
    private final static String FILE_FOLDER = "/ArtUi/";

    final static String FILE_LOG = "/log";

    public static String getExternalFilePath() {
        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir == null) {
            return null;
        }
        return externalDir.getAbsolutePath() + FILE_FOLDER;// 获得app的下载路径
    }

    /**
     * 获得Log信息打印路径
     */
    public static File getLogFile() {
        String path = getExternalFilePath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File dirFile = new File(path + FILE_LOG);
        if (dirFile.exists()) {
            if (dirFile.listFiles() != null && dirFile.listFiles().length > 5) {
                for (int i = 0; i < dirFile.listFiles().length; i++) {
                    dirFile.listFiles()[i].delete();
                }
            }
        } else {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        return new File(path + FILE_LOG + "/" + System.currentTimeMillis() + ".txt");
    }
}

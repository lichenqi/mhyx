package com.lianliantao.yuetuan.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by lcq on 2017/7/26.
 */

public class IOUtils {

    public static File getPathFile(String path) {
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), path);
        return outputFile;
    }

    public static void rmoveFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = getPathFile(path);
            Log.i("apk文件", file.toString());
            if (file.exists()) {
                file.delete();
            }
        }
    }
}

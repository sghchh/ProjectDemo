package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private static String SD_PATH;        // storage/sdcard/0/
    private static String FILE_STORAGE;   // Android/data/com.starstudio.projectdemo/

    private static String PICTURE_FILE;
    private static String VIDEO_FILE;

    public static void init(Context  context) {
        SD_PATH = Environment.getExternalStorageDirectory().getPath();
        FILE_STORAGE = context.getExternalFilesDir("").getPath();
        File file = new File(FILE_STORAGE + "/picture");
        if (!file.exists())
            file.mkdir();
        PICTURE_FILE = file.getAbsolutePath();

        file = new File(FILE_STORAGE + "/video");
        if (!file.exists())
            file.mkdir();
        VIDEO_FILE = file.getAbsolutePath();

    }

    public static String saveBitmap(Bitmap bitmap) {
        File save = new File(PICTURE_FILE, formatter.format(new Date()) + ".jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return save.getAbsolutePath();
    }
}

package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
        if (createFile(FILE_STORAGE, "picture"))
            PICTURE_FILE = FILE_STORAGE.concat("/picture");
        if (createFile(FILE_STORAGE, "video"))
            VIDEO_FILE = FILE_STORAGE.concat("/video");
    }

    private static boolean createFile(String parentDir, String filename) {
        File file = new File(parentDir, filename);
        if (!file.exists())
            try{
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }


    public static String saveBitmap(Bitmap bitmap) {
        File save = new File(formatter.format(new Date()) + ".jpg");
        boolean res = false;
        try {
            res = save.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!res)
            return null;
        return save.getAbsolutePath();
    }
}

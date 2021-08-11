package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.huawei.hms.image.vision.A;
import com.huawei.hms.videoeditor.sdk.p.S;
import com.starstudio.projectdemo.journal.data.AlbumData;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;

import org.luaj.vm2.ast.Str;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PermissionCollection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        File save = new File(PICTURE_FILE + "/" + "filter");
        if (!save.exists())
            save.mkdir();   // 如果该类别的文件夹不存在，则先创建
        save = new File(save.getAbsolutePath(), formatter.format(new Date()) + ".jpg");
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

    public static String copyFromPath(JournalEditActivityData.PictureWithCategory picture) throws IOException {
        String[] _ss = picture.getPicturePath().split("/");
        String finename = _ss[_ss.length - 1];
        File save;
        if (finename.endsWith(".mp4"))
            save = new File(VIDEO_FILE, finename);
        else {
            save = new File(PICTURE_FILE + "/" + picture.getCategory());
            if (!save.exists())
                save.mkdir();   // 如果该类别的文件夹不存在，则先创建
            save = new File(save.getAbsolutePath(), finename);
        }
        BufferedInputStream inputStream;
        BufferedOutputStream outputStream;
        inputStream = new BufferedInputStream(new FileInputStream(new File(picture.getPicturePath())));
        outputStream = new BufferedOutputStream(new FileOutputStream(save));
        byte[] cache = new byte[1024];
        while(inputStream.read(cache) != -1) {
            outputStream.write(cache);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return save.getAbsolutePath();
    }

    public static List<AlbumData> searchAlbumData() {
        File directory = new File(PICTURE_FILE);
        List<AlbumData> res = new ArrayList<>();
        File[] albumFiles = directory.listFiles();
        for (int i = 0; i < albumFiles.length; i ++) {
            File album = albumFiles[i];
            if (album.listFiles() == null)
                continue;
            AlbumData data = new AlbumData();
            if (album.listFiles().length == 0)
                data.setCover(null);
            else
                data.setCover(album.listFiles()[0].getAbsolutePath());
            data.setName(album.getName());
            res.add(data);
        }
        return res;
    }

    public static String[] loadAlbumPictures(String albumName) {
        File file = new File(PICTURE_FILE, albumName);
        File[] files = file.listFiles();
        String[] res = new String[files.length];
        for (int i = 0; i < res.length; i ++) {
            res[i] = files[i].getAbsolutePath();
        }
        return res;
    }
}

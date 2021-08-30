package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.starstudio.projectdemo.journal.data.AlbumData;
import com.starstudio.projectdemo.journal.data.JournalEditActivityData;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {
    private static String TAG = "FileUtil";
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

    /**
     * 滤镜服务中保存处理过后的Bitmap到指定目录
     * @param bitmap 滤镜处理后的bitmap
     * @return
     */
    public static String saveBitmap(Bitmap bitmap) {
        File save = new File(PICTURE_FILE + "/filter");
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

    /**
     * 修改头像时，将选中的图片保存到指定文件
     * @param selectedPath 所选中的图片的绝对路径
     * @return
     */
    public static String saveAvatar(String selectedPath) {
        File avatar = new File(FILE_STORAGE + "/avatar");
        if (!avatar.exists())
            avatar.mkdir();
        File save = new File(avatar.getAbsolutePath(), formatter.format(new Date()) + ".jpg");
        BufferedInputStream inputStream;
        BufferedOutputStream outputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(new File(selectedPath)));
            outputStream = new BufferedOutputStream(new FileOutputStream(save));
            byte[] cache = new byte[1024];
            while(inputStream.read(cache) != -1) {
                outputStream.write(cache);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IO错误：保存头像失败！");
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

    /**
     * 将Android绝对路径转换为Content开头的Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 将Content开头的Uri转换为Android绝对路径
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


    /**
     * 压缩图片
     */
    public static Bitmap sampleImage(String filePath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置仅解码边缘,图片不会真正加载到内存中，解码器加载返回null，
        // 但是图片的输出字段会进行赋值
        // 比如说 图片的宽，高
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        int scale = Math.max(outHeight / 300, outWidth / 300);
        //scale向下取整,真实取值 2的n次幂
        options.inSampleSize = scale;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath,options);
    }



}

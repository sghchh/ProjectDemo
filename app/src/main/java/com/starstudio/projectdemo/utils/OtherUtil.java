package com.starstudio.projectdemo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;


import com.google.gson.Gson;
import com.huawei.hms.framework.common.StringUtils;
import com.huawei.hms.videoeditor.sdk.p.S;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class OtherUtil {
    private static final HashMap<String, String> weekToEng = new HashMap(){{
        put("周一", "Mon");
        put("周二", "Tue");
        put("周三", "Wen");
        put("周四", "Thu");
        put("周五", "Fri");
        put("周六", "Sta");
        put("周日", "Sun");
    }};
    private static final HashMap<Integer, String> monthToEng = new HashMap(){{
       put(1, "January");
       put(2, "Fi");
       put(3, "3");
       put(4, "4");
       put(5, "5");
       put(6, "6");
       put(7, "7");
       put(8, "Fi");
       put(9, "9");
       put(10, "10");
       put(11, "11");
       put(12, "12");
    }};
    private static final Gson gson = new Gson();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-E");

    public static Bitmap decodeBitmapSafe(String path) {
        Bitmap res = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inDither = true;
        opts.inSampleSize = 8;
        res = BitmapFactory.decodeFile(path, opts);

        return res;
    }
    public static Bitmap scaleSquare(Bitmap originBitmap) {
        Bitmap res;
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        if (originWidth > originHeight) {
            int x = (originWidth - originHeight) / 2;
            int y = 0;
            res = Bitmap.createBitmap(originBitmap, x, y, originHeight, originHeight);
        } else {
            int y = (originHeight - originWidth) / 2;
            int x = 0;
            res = Bitmap.createBitmap(originBitmap, x, y, originWidth, originWidth);
        }
        return res;
    }

    public static String decodeObject(Object o) {
        return gson.toJson(o);
    }

    public static Object encodeString(String json) {
        return gson.toJson(json);
    }

    public static String getSystemYear() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[0];
    }

    public static String getSystemMonthToNumber() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return Integer.valueOf(ss[1]).toString();
    }

    public static String getSystemMonth() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return monthToEng.get(Integer.valueOf(ss[1]));
    }

    public static String getSystemDay() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[2];
    }

    public static String getSystemWeek() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return weekToEng.get(ss[3]);
    }

    /**
     * 将传递的毫秒时长解析为“时：分：秒”的格式
     * @param time
     * @return
     */
    public static String formatLongToTime(int time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return format.format(time);
    }

    /**
     * 将用字符串表示的两个数字进行相加 (该方法默认字符串中不包含 "+" 或 ”-“ 号)
     */
    public static String bigNumberAdd(String f, String s){
        System.out.print("加法:" + f + "+" + s + "=");
        // 翻转两个字符串，并转换成数组
        char[] a = new StringBuffer(f).reverse().toString().toCharArray();
        char[] b = new StringBuffer(s).reverse().toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;

        // f 和 s 均为 0 时的特殊情况
        if(lenA == 1 && lenB == 1 && a[0] == '0' && b[0] == '0'){
            return "0";
        }

        // 计算两个长字符串中的较长字符串的长度
        int len = lenA > lenB ? lenA : lenB;
        int[] result = new int[len + 1];
        for (int i = 0; i < len + 1; i++) {
            // 如果当前的i超过了其中的一个，就用0代替，和另一个字符数组中的数字相加
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            result[i] = aint + bint;
        }
        // 处理结果集合，如果大于10的就向前一位进位，本身进行除10取余
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= 10) {
                result[i + 1] += result[i] / 10;
                result[i] %= 10;
            }
        }
        StringBuffer sb = new StringBuffer();
        // 该字段用于标识是否有前置0，如果有就不要存储
        boolean flag = true;
        // 注意从后往前
        for (int i = len; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }
        // 结果
        System.out.println(sb.toString());
        return sb.toString();
    }


    /**
     * 将用字符串表示的两个数字进行相减 (该方法默认字符串中不包含 "+" 或 ”-“ 号)，
     */
    public static String bigNumberSub(String f, String s) {
        // 将字符串翻转并转换成字符数组
        char[] a = new StringBuffer(f).reverse().toString().toCharArray();
        char[] b = new StringBuffer(s).reverse().toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;
        // 找到最大长度
        int len = lenA > lenB ? lenA : lenB;
        int[] result = new int[len];
        // 表示结果的正负
        char sign = '+';
        // 判断最终结果的正负
        if (lenA < lenB) {
            sign = '-';
        } else if (lenA == lenB) {
            int i = lenA - 1;
            while (i > 0 && a[i] == b[i]) {
                i--;
            }
            if (a[i] < b[i]) {
                sign = '-';
            }
        }
        // 计算结果集，如果最终结果为正，那么就a-b否则的话就b-a
        for (int i = 0; i < len; i++) {
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            if (sign == '+') {
                result[i] = aint - bint;
            } else {
                result[i] = bint - aint;
            }
        }
        // 如果结果集合中的某一位小于零，那么就向前一位借一，然后将本位加上10。其实就相当于借位做减法
        for (int i = 0; i < result.length - 1; i++) {
            if (result[i] < 0) {
                result[i + 1] -= 1;
                result[i] += 10;
            }
        }

        StringBuffer sb = new StringBuffer();
        // 如果最终结果为负值，就将负号放在最前面，正号则不需要
        if (sign == '-') {
            sb.append('-');
        }
        // 判断是否有前置0
        boolean flag = true;
        for (int i = len - 1; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }
        // 如果最终结果集合中没有值，就说明是两值相等，最终返回0
        if (sb.toString().equals("")) {
            sb.append("0");
        }
        // 返回值
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 将用字符串表示的两个数字相加减进行整合为一个方法便于调用 (该方法默认字符串中正数前面不带符号，负数第一位用"-"代表)
     */
    public static String bigNumberOperation(String f, String s){
        String ans = "";

        if(f == null || s == null){
            return ans;
        }
        if(f.charAt(0) != '-' && s.charAt(0) != '-'){
            ans =  bigNumberAdd(f,s);
        }else if(f.charAt(0) == '-' && s.charAt(0) != '-'){
            ans =  bigNumberSub(s,f.substring(1));
        }else if(f.charAt(0) != '-' && s.charAt(0) == '-'){
            ans =  bigNumberSub(f, s.substring(1));
        }else if(f.charAt(0) == '-' && s.charAt(0) == '-'){
            ans =  "-" + bigNumberAdd(f.substring(1),s.substring(1));
        }
        return ans;
    }


    public static Boolean isStringToNum(String s){
//        if(s == null){
//            return false;
//        }
//
//        try {
//                // parseInt 是将字符串转换为整数类型，返回一个int类型，如果字符串中有非数字类型字符，则会抛出一个NumberFormatException的异常
//            Double.parseDouble(s);
//            return true;
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int p2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px 转换为 sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 sp 转换为 px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



}

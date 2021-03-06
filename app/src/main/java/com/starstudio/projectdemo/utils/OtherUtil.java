package com.starstudio.projectdemo.utils;

import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.CLOUDY;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.HEAVY_RAIN;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.HEAVY_RAIN_TO_STORM;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.HEAVY_SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.HEAVY_SNOW_TO_SNOWSTORM;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.LIGHT_RAIN;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.LIGHT_SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.LIGHT_TO_MODERATE_RAIN;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.LIGHT_TO_MODERATE_SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.MODERATE_RAIN;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.MODERATE_SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.MODERATE_TO_HEAVY_RAIN;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.MODERATE_TO_HEAVY_SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.OVERCAST;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.RAINFALL;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SNOW;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SNOWFALL;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SNOWSTORM;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SNOW_FLURRY;
import static com.huawei.hms.kit.awareness.status.weather.constant.CNWeatherId.SUNNY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;


import com.google.gson.Gson;
import com.huawei.hms.framework.common.StringUtils;
import com.huawei.hms.videoeditor.sdk.p.S;
import com.starstudio.projectdemo.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class OtherUtil {
    public static final HashMap<Integer, Integer> weatherId2Mipmap = new HashMap() {{
        put(SUNNY, R.mipmap.weather2_sun);
        put(CLOUDY, R.mipmap.weather2_cloud);
        put(OVERCAST, R.mipmap.weather2_overcast);
        put(SNOW, R.mipmap.weather2_snow);
        put(SNOW_FLURRY, R.mipmap.weather2_snow);
        put(SNOWFALL, R.mipmap.weather2_snow);
        put(SNOWSTORM, R.mipmap.weather2_snow);
        put(HEAVY_SNOW, R.mipmap.weather2_snow);
        put(LIGHT_SNOW, R.mipmap.weather2_snow);
        put(LIGHT_TO_MODERATE_SNOW, R.mipmap.weather2_snow);
        put(MODERATE_SNOW, R.mipmap.weather2_snow);
        put(MODERATE_TO_HEAVY_SNOW, R.mipmap.weather2_snow);
        put(HEAVY_SNOW_TO_SNOWSTORM, R.mipmap.weather2_snow);
        put(RAINFALL, R.mipmap.weather2_rain);
        put(HEAVY_RAIN, R.mipmap.weather2_rain);
        put(LIGHT_RAIN, R.mipmap.weather2_rain);
        put(MODERATE_RAIN, R.mipmap.weather2_rain);
        put(LIGHT_TO_MODERATE_RAIN, R.mipmap.weather2_rain);
        put(MODERATE_TO_HEAVY_RAIN, R.mipmap.weather2_rain);
        put(HEAVY_RAIN_TO_STORM, R.mipmap.weather2_rain);
    }};
    private static final HashMap<String, String> weekToEng = new HashMap(){{
        put("??????", "Mon.");
        put("??????", "Tue.");
        put("??????", "Wen.");
        put("??????", "Thu.");
        put("??????", "Fri.");
        put("??????", "Sta.");
        put("??????", "Sun.");
    }};
    private static final HashMap<Integer, String> monthToEng = new HashMap(){{
       put(1, "January");
       put(2, "February");
       put(3, "March");
       put(4, "April");
       put(5, "May");
       put(6, "June");
       put(7, "July");
       put(8, "August");
       put(9, "September");
       put(10, "October");
       put(11, "November");
       put(12, "December");
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
        if(ss[1].charAt(0) == '0'){
            return ss[1].charAt(1) + "";
        }else{
            return ss[1];
        }
    }

    public static String getSystemMonth() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[1];
    }

    public static String getSystemDay() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[2];
    }

    public static String getSystemWeek() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return ss[3];
    }

    public static String getSystemMonthEng() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return monthToEng.get(Integer.valueOf(ss[1]));
    }

    public static String getSystemWeekEng() {
        String[] ss = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
        return weekToEng.get(ss[3]);
    }
    /**
     * ???????????????????????????????????????????????????????????????
     * @param time ?????????????????????????????????
     * @return
     */
    public static String formatLongToTime(int time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        return format.format(time);
    }


    /**
     * ???????????????????????????????????????
     * @param time1
     * @param time2
     * @return 0:???????????????????????????1?????????????????????????????????????????????2?????????????????????????????????
     */
    public static int checkTimeLength(long time1, long time2) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String t1 = sdf.format(new Date(time1));
        String t2 = sdf.format(new Date(time2));
        String[] time1s = t1.split("-");
        String[] time2s = t2.split("-");
        if (!time1s[0].equals(time2s[0]) || !time1s[1].equals(time2s[1]))
            return 2;
        if (time1s[2].equals(time2s[2]))
            return 0;
        return Math.min(Math.abs(Integer.parseInt(time1s[2]) - Integer.parseInt(time2s[2])), 2);
    }

    /**
     * ??????????????????????????????????????? HH:mm
     * @param timestamp ?????????
     * @return ????????????
     */
    public static String getClockTime(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(timestamp);
        return time;
    }

    /**
     * ??????????????????????????????
     * @param timestamp ?????????
     * @return yyyy-MM-dd
     */
    public static String getYearMonth(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date(timestamp));
        return time;
    }


    /**
     * ????????????????????????????????????
     * @param hhmm ????????????TimePicker??????????????????
     * @return ?????????
     */
     public static long generateTimestamp(String hhmm) {
         @SuppressLint("SimpleDateFormat")
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         StringBuilder builder = new StringBuilder(getYearMonth(System.currentTimeMillis()));
         builder.append(" ");
         builder.append(hhmm);
         long res = System.currentTimeMillis();
         try {
             res = sdf.parse(builder.toString()).getTime();
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return res;
     }


    /**
     * ???????????????????????????????????????????????? (???????????????????????????????????? "+" ??? ???-??? ???)
     */
    public static String bigNumberAdd(String f, String s){
        System.out.print("??????:" + f + "+" + s + "=");
        // ??????????????????????????????????????????
        char[] a = new StringBuffer(f).reverse().toString().toCharArray();
        char[] b = new StringBuffer(s).reverse().toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;

        // f ??? s ?????? 0 ??????????????????
        if(lenA == 1 && lenB == 1 && a[0] == '0' && b[0] == '0'){
            return "0";
        }

        // ??????????????????????????????????????????????????????
        int len = lenA > lenB ? lenA : lenB;
        int[] result = new int[len + 1];
        for (int i = 0; i < len + 1; i++) {
            // ???????????????i?????????????????????????????????0???????????????????????????????????????????????????
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            result[i] = aint + bint;
        }
        // ?????????????????????????????????10??????????????????????????????????????????10??????
        for (int i = 0; i < result.length; i++) {
            if (result[i] >= 10) {
                result[i + 1] += result[i] / 10;
                result[i] %= 10;
            }
        }
        StringBuffer sb = new StringBuffer();
        // ????????????????????????????????????0???????????????????????????
        boolean flag = true;
        // ??????????????????
        for (int i = len; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }
        // ??????
        System.out.println(sb.toString());
        return sb.toString();
    }



    /**
     * ???????????????????????????????????????????????? (???????????????????????????????????? "+" ??? ???-??? ???)???
     */
    public static String bigNumberSub(String f, String s) {
        // ??????????????????????????????????????????
        char[] a = new StringBuffer(f).reverse().toString().toCharArray();
        char[] b = new StringBuffer(s).reverse().toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;
        // ??????????????????
        int len = lenA > lenB ? lenA : lenB;
        int[] result = new int[len];
        // ?????????????????????
        char sign = '+';
        // ???????????????????????????
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
        // ??????????????????????????????????????????????????????a-b???????????????b-a
        for (int i = 0; i < len; i++) {
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            if (sign == '+') {
                result[i] = aint - bint;
            } else {
                result[i] = bint - aint;
            }
        }
        // ????????????????????????????????????????????????????????????????????????????????????????????????10????????????????????????????????????
        for (int i = 0; i < result.length - 1; i++) {
            if (result[i] < 0) {
                result[i + 1] -= 1;
                result[i] += 10;
            }
        }

        StringBuffer sb = new StringBuffer();
        // ??????????????????????????????????????????????????????????????????????????????
        if (sign == '-') {
            sb.append('-');
        }
        // ?????????????????????0
        boolean flag = true;
        for (int i = len - 1; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }
        // ??????????????????????????????????????????????????????????????????????????????0
        if (sb.toString().equals("")) {
            sb.append("0");
        }
        // ?????????
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static  String decimalAdd(String f,String s){
        System.out.print("????????????:" + f + "+" + s + "=");
        // ??????????????????????????????,?????????
        char[] a = new StringBuffer(f).toString().toCharArray();
        char[] b = new StringBuffer(s).toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;
        //??????????????????
        int len = lenA > lenB ? lenA : lenB;
        if(len==0)
            return  "0";
        // f ??? s ?????? 0 ??????????????????
        if(len==0 && a[0] == '0' && b[0] == '0'){
            return "0";
        }
        //????????????????????????????????????1
        int flg=0;
        int[] result = new int[len];
        for (int i = len-1; i >=0; i--) {
            // ????????????i???????????????????????????0???????????????????????????????????????????????????
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            result[i] = aint + bint;
        }
        // ?????????????????????????????????10??????????????????????????????????????????10??????
        for (int i = len-1; i >=1; i--) {
            if (result[i] >= 10) {
                result[i-1] += 1;
                result[i] %= 10;
            }
        }
        if(result[0]>=10){
            flg=1;
            result[0]-=10;
        }
        StringBuffer sb=new StringBuffer();

        // ?????????????????????0??????????????????
        boolean flag = true;
        for (int i = len - 1; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }

        if (sb.toString().equals("")) {
            sb.append("0");
        }

        //?????????????????????????????????????????????????????????????????????.??????
        if(flg==1){
            sb.append(".1");
        }
        // ??????
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String decimalSub(String f, String s) {
        System.out.print("????????????:" + f + "-" + s + "=");
        // ?????????????????????????????????,?????????
        if(f=="" && s=="")
            return  "0";
        char[] a = new StringBuffer(f).toString().toCharArray();
        char[] b = new StringBuffer(s).toString().toCharArray();
        int lenA = a.length;
        int lenB = b.length;
        // ??????????????????
        int len = lenA > lenB ? lenA : lenB;
        int[] result = new int[len];
       /* // ?????????????????????
        char sign = '+';
        // ???????????????????????????
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
        }*/
        // ??????????????????????????????????????????????????????a-b???????????????b-a
        for (int i = len-1; i >=0; i--) {
            int aint = i < lenA ? (a[i] - '0') : 0;
            int bint = i < lenB ? (b[i] - '0') : 0;
            result[i]=aint-bint;
        }
        // ????????????????????????????????????????????????????????????????????????????????????????????????10????????????????????????????????????
        for (int i = len-1; i >0; i--) {
            if (result[i] < 0) {
                result[i - 1] -= 1;
                result[i] += 10;
            }
        }

        //??????????????????
        int flg=0;
        if(result[0]<0){
            flg=1;
            result[0]+=10;
        }
        StringBuffer sb = new StringBuffer();

//        // ??????????????????????????????????????????????????????????????????????????????
//        if (sign == '-') {
//            sb.append('-');
//        }


        // ?????????????????????0??????????????????
        boolean flag = true;
        for (int i = len - 1; i >= 0; i--) {
            if (result[i] == 0 && flag) {
                continue;
            } else {
                flag = false;
            }
            sb.append(result[i]);
        }

        // ??????????????????????????????????????????????????????????????????????????????0
        if (sb.toString().equals("")) {
            sb.append("0");
        }
        //????????????
        if(flg==1)
            sb.append(".1");

        // ?????????
        System.out.println(sb.toString());
        return sb.toString();
    }

    //???????????????????????????????????????????????????true
    public static int compareBigNum(String f,String s){
        char[] a = new StringBuffer(f).toString().toCharArray();
        char[] b = new StringBuffer(s).toString().toCharArray();
        int lenA=f.length();
        int lenB=s.length();
        // ???????????????????????????
        if(lenA>lenB)
            return  1;
        if (lenA < lenB) {
            return  -1;
        } else if (lenA == lenB) {
            int i = 0;
            while (i < lenA && a[i] == b[i]) {
                i++;
            }
            if(i==lenA)
                return  0;
            if (a[i] < b[i])
                return  -1;
            if(a[i]>b[i])
                return  1;
            if(i==lenA-1)
                return 0;
        }
        return 1;
    }

    public static int compareDecimalNum(String f,String s){
        char[] a = new StringBuffer(f).toString().toCharArray();
        char[] b = new StringBuffer(s).toString().toCharArray();
        int lenA=f.length();
        int lenB=s.length();
        //??????????????????
        int i=0;
        while(i<lenA && i<lenB){
            if(a[i]<b[i])
                return -1;
            if(a[i]>b[i])
                return 1;
            i++;
        }
        if(i==lenA && lenA<lenB)
            return  -1;
        if(i==lenA && lenA==lenB)
            return  0;
        return 1;
    }
    /**
     * ???????????????????????????????????????????????????????????????????????????????????? (????????????????????????????????????????????????????????????????????????"-"??????)
     */
    public static String bigNumberOperation(String f, String s){
        String ans = "";

        if(f == null || s == null){
            return ans;
        }

        String[] first=f.split("\\.");
        String[] second=s.split("\\.");
        int num_first=first.length;
        int num_second=second.length;
        //???????????????????????????
        String first_integer=first[0];
        String second_integer=second[0];
        //???????????????????????????
        String first_decimal="";
        String second_decimal="";
        if(num_first==2){
            first_decimal=first[1];
        }
        if(num_second==2){
            second_decimal=second[1];
        }
        if(first_integer.charAt(0) != '-' && second_integer.charAt(0) != '-'){
            String ans_decimal=decimalAdd(first_decimal,second_decimal);
            String[] arr=ans_decimal.split("\\.");
            String ans_integer=bigNumberAdd(first_integer,second_integer);
            if(arr.length==2){
                ans_integer=bigNumberAdd(ans_integer,"1");
                ans=ans_integer+"."+arr[0];
            }
            else{
                ans=ans_integer+"."+arr[0];
            }
        }else if(first_integer.charAt(0) == '-' && second_integer.charAt(0) != '-'){
            //??????????????????
            if(compareBigNum(second_integer,first_integer.substring(1))==1){
                String ans_decimal=decimalSub(second_decimal,first_decimal);
                String[] arr=ans_decimal.split("\\.");
                String ans_integer=bigNumberSub(second_integer,first_integer.substring(1));
                if(arr.length==2){
                    ans_integer=bigNumberSub(ans_integer,"1");
                    ans=ans_integer+"."+arr[0];
                }
                else{
                    ans=ans_integer+"."+arr[0];
                }
            }else if (compareBigNum(second_integer,first_integer.substring(1))==-1){
                String ans_decimal=decimalSub(first_decimal,second_decimal);
                String[] arr=ans_decimal.split("\\.");
                String ans_integer=bigNumberSub(first_integer.substring(1),second_integer);
                if(arr.length==2){
                    ans_integer=bigNumberSub(ans_integer,"1");
                    ans="-"+ans_integer+"."+arr[0];
                }else{
                    ans="-"+ans_integer+"."+arr[0];
                }
            }
            else{
                if(compareDecimalNum(second_decimal,first_decimal)==1){
                    String ans_decimal=decimalSub(second_decimal,first_decimal);
                    String[] arr=ans_decimal.split("\\.");
                    String ans_integer=bigNumberSub(second_integer,first_integer.substring(1));
                    if(arr.length==2){
                        ans_integer=bigNumberSub(ans_integer,"1");
                        ans=ans_integer+"."+arr[0];
                    }
                    else{
                        ans=ans_integer+"."+arr[0];
                    }
                }
                else if(compareDecimalNum(second_decimal,first_decimal)==-1){
                    String ans_decimal=decimalSub(first_decimal,second_decimal);
                    String[] arr=ans_decimal.split("\\.");
                    String ans_integer=bigNumberSub(first_integer.substring(1),second_integer);
                    if(arr.length==2){
                        ans_integer=bigNumberSub(ans_integer,"1");
                        ans="-"+ans_integer+"."+arr[0];
                    }else{
                        ans="-"+ans_integer+"."+arr[0];
                    }
                }
                else{
                    ans="0";
                }
            }
        }else if(first_integer.charAt(0) != '-' && second_integer.charAt(0) == '-'){
            //??????????????????
            if(compareBigNum(first_integer,second_integer.substring(1))==1){
                String ans_decimal=decimalSub(first_decimal,second_decimal);
                String[] arr=ans_decimal.split("\\.");
                String ans_integer=bigNumberSub(first_integer,second_integer.substring(1));
                if(arr.length==2){
                    ans_integer=bigNumberSub(ans_integer,"1");
                    ans=ans_integer+"."+arr[0];
                }
                else{
                    ans=ans_integer+"."+arr[0];
                }
            }else if (compareBigNum(first_integer,second_integer.substring(1))==-1){
                String ans_decimal=decimalSub(second_decimal,first_decimal);
                String[] arr=ans_decimal.split("\\.");
                String ans_integer=bigNumberSub(second_integer.substring(1),first_integer);
                if(arr.length==2){
                    ans_integer=bigNumberSub(ans_integer,"1");
                    ans="-"+ans_integer+"."+arr[0];
                }else{
                    ans="-"+ans_integer+"."+arr[0];
                }
            }
            else{
                if(compareDecimalNum(first_decimal,second_decimal)==0){
                    String ans_decimal=decimalSub(first_decimal,second_decimal);
                    String[] arr=ans_decimal.split("\\.");
                    String ans_integer=bigNumberSub(first_integer,second_integer.substring(1));
                    if(arr.length==2){
                        ans_integer=bigNumberSub(ans_integer,"1");
                        ans=ans_integer+"."+arr[0];
                    }
                    else{
                        ans=ans_integer+"."+arr[0];
                    }
                }
                if(compareDecimalNum(first_decimal,second_decimal)==-1){
                    String ans_decimal=decimalSub(second_decimal,first_decimal);
                    String[] arr=ans_decimal.split("\\.");
                    String ans_integer=bigNumberSub(second_integer.substring(1),first_integer);
                    if(arr.length==2){
                        ans_integer=bigNumberSub(ans_integer,"1");
                        ans="-"+ans_integer+"."+arr[0];
                    }else{
                        ans="-"+ans_integer+"."+arr[0];
                    }
                }
                else{
                    ans="0";
                }
            }
        }else if(first_integer.charAt(0) == '-' && second_integer.charAt(0) == '-'){
            String ans_decimal=decimalAdd(first_decimal,second_decimal);
            String[] arr=ans_decimal.split("\\.");
            String ans_integer=bigNumberAdd(first_integer.substring(1),second_integer.substring(1));
            if(arr.length==2){
                ans_integer=bigNumberAdd(ans_integer,"1");
                ans="-"+ans_integer+"."+arr[0];
            }
            else{
                ans="-"+ans_integer+"."+arr[0];
            }
        }
        return ans;
    }

    public  static  boolean isNUm(String s){
        char[] a=new StringBuffer(s).toString().toCharArray();
        int len=a.length;
        int num=0;
        for(int i=0;i<len;i++){
            if(a[i]=='.'){
                num++;
                if(num>1)
                    return  false;
            }
        }
        return  true;
    }

    public static Boolean isStringToNum(String s){
        char[] a=new StringBuffer(s).toString().toCharArray();
        int len=a.length;
        int num=0;
        for(int i=0;i<len;i++){
            if(a[i]=='.'){
                num++;
                if(num>1)
                    return  false;
            }
        }
        return  true;
    }

    /**
     * ??????????????????????????? dp ????????? ????????? px(??????)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * ??????????????????????????? px(??????) ????????? ????????? dp
     */
    public static int p2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * ??????????????????????????? px ????????? sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * ??????????????????????????? sp ????????? px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



}

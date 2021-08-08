package com.starstudio.projectdemo.journal.api;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.CaptureClient;
import com.huawei.hms.kit.awareness.capture.WeatherStatusResponse;
import com.huawei.hms.kit.awareness.status.weather.WeatherSituation;

import java.util.HashMap;
import java.util.Map;

/**
 * created by sgh
 * 2021-8-7
 * HMS获取天气与定位的简单封装
 */
public class HmsWeatherService {

    private WeatherSituation weatherSituation;   // hms提供的类，封装天气请求返回结果
    private CaptureClient client;         // hms内部类，发起天气请求
    private volatile static HmsWeatherService serviceImpl;

    private HmsWeatherService() {
    }

    public static synchronized void init(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            throw new RuntimeException("请为应用开启定位权限！！");

        if (serviceImpl == null) {
            synchronized (HmsWeatherService.class) {
                serviceImpl = new HmsWeatherService();
                serviceImpl.client = Awareness.getCaptureClient(context);
            }
        }

    }

    public static String getWeather() {
        if (serviceImpl == null)
            throw new RuntimeException("请先执行init(Context)方法");
        if (serviceImpl.weatherSituation != null)
            return "" + serviceImpl.weatherSituation.getSituation().getCnWeatherId();
        final String[] res = new String[1];
        serviceImpl.client.getWeatherByDevice()
                .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {
                    @Override
                    public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                        serviceImpl.weatherSituation = weatherStatusResponse.getWeatherStatus().getWeatherSituation();
                        res[0] = "" + serviceImpl.weatherSituation.getSituation().getCnWeatherId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        res[0] = e.getMessage();
                    }
                });
        return res[0];
    }

    public static String getLocation() {
        if (serviceImpl == null)
            throw new RuntimeException("请先执行init(Context)方法");
        if (serviceImpl.weatherSituation != null)
            return serviceImpl.weatherSituation.getCity().getName();
        final String[] res = new String[1];
        serviceImpl.client.getWeatherByDevice()
                .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {
                    @Override
                    public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                        serviceImpl.weatherSituation = weatherStatusResponse.getWeatherStatus().getWeatherSituation();
                        res[0] = serviceImpl.weatherSituation.getCity().getName();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        res[0] = e.getMessage();
                    }
                });
        return res[0];
    }
}

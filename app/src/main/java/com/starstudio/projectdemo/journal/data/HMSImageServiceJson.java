package com.starstudio.projectdemo.journal.data;

import android.graphics.Bitmap;

import com.huawei.hms.image.vision.A;

import org.json.JSONObject;

public class HMSImageServiceJson {

    public static class AuthJson extends JSONObject {
        private String projectId;
        private String appId;
        private String authApiKey;
        private String clientSecret;
        private String clientId;
        private volatile static AuthJson instance;

        private AuthJson() {
            this.projectId = "736430079245885371";
            appId = "104557639";
            authApiKey = "CgB6e3x9c2tIlXQZdvRg9VeCfngxvAwbW5FpKsYs/7eW39cdgYZ90pxu2gM85yEp+f2zCFSTXy4CebF3cdcULMzc";
            clientSecret = "4B72F0511A5A7C34C73EBF9540B7C712CF9102AF889F5AB5F1B3EF6F462DA386";
            clientId = "679108801520993472";
        }

        public synchronized static AuthJson getInstance() {
            if (instance == null)
                synchronized (AuthJson.class) {
                    instance = new AuthJson();
                }
            return instance;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAuthApiKey() {
            return authApiKey;
        }

        public void setAuthApiKey(String authApiKey) {
            this.authApiKey = authApiKey;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
    }

    public static class RequestJson extends JSONObject{
        private String requestId;
        private AuthJson authJson;
        private TaskJson taskJson;  // 具体请求业务参数

        public RequestJson(String requestId, TaskJson taskJson) {
            this.requestId = requestId;
            this.taskJson = taskJson;
            authJson = AuthJson.getInstance();
        }
    }

    public static class TaskJson extends JSONObject{
        private int filterType;   // 滤镜的类别
        private float intensity = 1;  // 滤镜强度，范围为(0,1]，默认为1
        private float compresssRate = 1;  // 压缩率，范围为(0,1]，默认为1
    }

    public static class VisionResultJson extends JSONObject {
        private int resultCode;
        private ResponseJson response;
        private Bitmap image;
    }

    public static class ResponseJson extends JSONObject {
        private String requestId;
        private String serviceId;
    }
}
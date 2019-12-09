package com.tsml.hkl.Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.enty.MyRequest;
import com.tsml.hkl.enty.UserOut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtils {


    public static void postFromParameters(RequestBody formBody) {
        String url = "http://localhost"; // 请求链接

        OkHttpClient okHttpClient = new OkHttpClient(); // OkHttpClient对象

        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求

        okHttpClient.newCall(request).enqueue(new Callback() {// 回调

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String s = KeyUtils.decryptBASE64(response.body().string());


                } catch (Exception e) {

                }
            }

            public void onFailure(Call call, IOException e) {

            }
        });
    }


    /**
     * Post提交字符串
     * 使用Post方法发送一串字符串，但不建议发送超过1M的文本信息
     */
    public static void postStringParameters(String s, Collback collback) {
        MediaType MEDIA_TYPE = MediaType.parse("text/text; charset=utf-8");

        OkHttpClient okHttpClient = new OkHttpClient(); // OkHttpClient对象
        Request request = new Request.Builder().url(AppData.URL).post(RequestBody.create(MEDIA_TYPE, s)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            public void onResponse(Call call, Response response) {
                try {
                    Gson gson = new Gson();
                    UserOut userOut = gson.fromJson(KeyUtils.jie(response.body().string()), UserOut.class);
                    collback.ok(userOut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                collback.ok(null);
            }
        });

    }

    public interface Collback {
        void ok(UserOut userOut);
    }


   /* public static void main(String[] args) {

        final int count = (int) ((Math.random() * 9 + 1) * 100000);
        String rokey = UUID.randomUUID().toString();
        String code = null;
        String counts = null;

        try {
            code = KeyUtils.encryptBASE64("555555555555555555555556");
            counts = KeyUtils.encryptBASE64(String.valueOf(count));
            MyRequest my = new MyRequest();
            my.setCum(counts);
            my.setUps(code);
            my.setImei("dsfsdfsdfsdfsd");
            Gson gs = new Gson();
            String s = gs.toJson(my);
            String request = KeyUtils.jia(s);
            postStringParameters(request, new Collback() {
                @Override
                public void ok(UserOut userOut) {
                    System.out.println("userOut = " + userOut);
                }

                @Override
                public UserOut errer(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

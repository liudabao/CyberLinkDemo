package com.example.lenovo.cyberlinkdemo.util;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2016/7/19.
 */
public class HttpUtil {

    private static final  OkHttpClient okHttpClient=new OkHttpClient();

    public static String get(String url){

        String result=null;
        Request request=new Request.Builder().url(url).build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                result=response.body().string();
            }
            else {
                Log.e("HTTP","fail");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

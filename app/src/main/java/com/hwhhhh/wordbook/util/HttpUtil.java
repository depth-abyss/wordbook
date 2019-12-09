package com.hwhhhh.wordbook.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void setHttpRequest(final String address, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;     //新建http访问
                try {
                    connection = (HttpURLConnection) new URL(address).openConnection();  //得到connection对象
                    connection.setRequestMethod("GET");     //get请求方式
                    connection.setReadTimeout(8000);        //读取超时
                    connection.setConnectTimeout(8000);     //连接超时
                    InputStream inputStream = connection.getInputStream();  //接收相应流
                    if (listener != null) {
                        listener.onFinish(inputStream);     //请求成功
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);                //请求失败
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();            //关闭请求
                    }
                }
            }
        }).start();
    }
}

package com.qsj.qsjMain.remote.service;

import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
@Service
public class EWXRobotRemoteService {

    private static final String KEY = "c32aa566-d71b-4a61-80b9-978b60f7e3d6";

    private final OkHttpClient robotClient = new OkHttpClient.Builder().addInterceptor(chain -> {
        Request original = chain.request();
        HttpUrl url = original.url();
        return chain.proceed(original.newBuilder()
                .url(url.newBuilder().
                        addQueryParameter("key", KEY)
                        .build())
                .build());
    }).build();

    /**
     * 使用accessToken授权调用api
     */


    public IEWXRobotApiRemoteService getService() {
        return new Retrofit.Builder().baseUrl("https://qyapi.weixin.qq.com/").client(robotClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IEWXRobotApiRemoteService.class);
    }

}

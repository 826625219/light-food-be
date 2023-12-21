package com.qsj.qsjMain.remote.service;

import com.google.gson.GsonBuilder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class GPTProxyRemoteService {

    private static final String TOKEN = "bc28c8a0f6b67e849eac8c6284cb6d82";

    /**
     * 使用accessToken授权调用api
     */
    private final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl url = original.url();
                return chain.proceed(original.newBuilder()
                        .url(url.newBuilder().addQueryParameter("access_token", TOKEN).build())
                        .build());

            })
            .writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
            .build();

    public IGPTProxyApiRemoteService getGptService() {
        return new Retrofit.Builder().baseUrl("http://43.133.35.38:5000/").client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IGPTProxyApiRemoteService.class);
    }


}

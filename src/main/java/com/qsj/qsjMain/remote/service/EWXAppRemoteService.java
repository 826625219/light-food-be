package com.qsj.qsjMain.remote.service;

import com.google.gson.GsonBuilder;
import com.qsj.qsjMain.remote.service.model.vo.EWXAccessTokenVO;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class EWXAppRemoteService {

    private static final String CROP_ID = "wwb0df74af9b2623f0";
    private static final String SECRET_KEY = "0EEPe-7p6_5B14X0ioZfb6r9RcuBqCpQb3VHUN1k3rc";
    private static final String BASE_URL = "https://qyapi.weixin.qq.com/";

    private String accessToken;
    private Long accessTokenExpireTime;

    private final OkHttpClient secretClient = new OkHttpClient.Builder().addInterceptor(chain -> {
        Request original = chain.request();
        HttpUrl url = original.url();
        return chain.proceed(original.newBuilder()
                .url(url.newBuilder().addQueryParameter("corpid", CROP_ID)
                        .addQueryParameter("corpsecret", SECRET_KEY)
                        .build())
                .build());
    }).build();

    /**
     * 使用accessToken授权调用api
     */

    private final OkHttpClient accessTokenClient = new OkHttpClient.Builder().addInterceptor(chain -> {
                if (accessToken == null || accessToken.isEmpty()) {
                    refreshAccessToken();
                }

                if (accessTokenExpireTime == null || accessTokenExpireTime < new Date().getTime() - 2 * 60 * 1000) {
                    refreshAccessToken();
                }

                Request original = chain.request();
                HttpUrl url = original.url();
                return chain.proceed(original.newBuilder()
                        .url(url.newBuilder().addQueryParameter("access_token", accessToken).build())
                        .build());

            })
            .writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS)
            .build();

    private void refreshAccessToken() throws IOException {
        // get access token from wx api
        EWXAccessTokenVO tokenVO = new Retrofit.Builder().baseUrl(BASE_URL).client(secretClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IEWXSecretApiRemoteService.class)
                .getAccessToken()
                .execute()
                .body();
        accessTokenExpireTime = new Date().getTime() + tokenVO.getExpiresIn() * 1000;
        accessToken = tokenVO.getAccessToken();
    }

    public IEWXTokenApiRemoteService getTokenApiRemoteService() {
        return new Retrofit.Builder().baseUrl(BASE_URL).client(accessTokenClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IEWXTokenApiRemoteService.class);
    }

}

package com.qsj.qsjMain.remote.service;

import com.google.gson.GsonBuilder;
import com.qsj.qsjMain.remote.service.model.vo.WXAccessTokenVO;
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
public class WXApiRemoteService {

    public static final String APP_ID = "wxfbfc7fd709d9253b";
    private static final String APP_SECRET = "bc28c8a0f6b67e849eac8c6284cb6d82";

    private String accessToken;
    private Long accessTokenExpireTime;

    /**
     * 使用appId, secret 等信息授权调用api
     */
    private final OkHttpClient secretClient = new OkHttpClient.Builder().addInterceptor(chain -> {
        Request original = chain.request();
        HttpUrl url = original.url();
        return chain.proceed(original.newBuilder()
                .url(url.newBuilder().addQueryParameter("appid", APP_ID)
                        .addQueryParameter("secret", APP_SECRET)
                        .addQueryParameter("grant_type", "client_credential")
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

    public IWXTokenApiRemoteService getTokenService() {
        return new Retrofit.Builder().baseUrl("https://api.weixin.qq.com/").client(accessTokenClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IWXTokenApiRemoteService.class);
    }

    public IWXSecretApiRemoteService getSecretService() {
        return new Retrofit.Builder().baseUrl("https://api.weixin.qq.com/").client(secretClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IWXSecretApiRemoteService.class);
    }

    private void refreshAccessToken() throws IOException {
        // get access token from wx api
        WXAccessTokenVO tokenVO = new Retrofit.Builder().baseUrl("https://api.weixin.qq.com/").client(secretClient)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IWXSecretApiRemoteService.class)
                .getAccessToken()
                .execute()
                .body();
        accessTokenExpireTime = new Date().getTime() + tokenVO.getExpiresIn() * 1000;
        accessToken = tokenVO.getAccessToken();
    }

}

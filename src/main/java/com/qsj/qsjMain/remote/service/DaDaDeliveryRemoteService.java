package com.qsj.qsjMain.remote.service;

import com.google.gson.GsonBuilder;
import com.qsj.qsjMain.config.ProfileVariableConfig;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Resource;

/**
 *
 */
@Slf4j
@Service
public class DaDaDeliveryRemoteService {

    @Resource
    private ProfileVariableConfig profileVariableConfig;
    private final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

    /**
     * 使用accessToken授权调用api
     */


    public IDaDaDeliverApiRemoteService getService() {

        return new Retrofit.Builder().baseUrl(profileVariableConfig.getDadaDeliveryPrefix()).client(client)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))
                .build()
                .create(IDaDaDeliverApiRemoteService.class);
    }

}

package com.aciv.parceros.Tools;

import com.aciv.parceros.Interfaces.RetrofitInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTools {
    public static RetrofitInterface getInterface(){
        return new Retrofit.Builder()
                .baseUrl("http://speuis.com/apps/parceros/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInterface.class);
    }
}

package com.platinumstudio.contacts.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//    To issue network requests to a REST API with Retrofit, we need to create an instance using the
//    Retrofit.Builder class and configure it with a base URL.
public class RetrofitClientInstance {

    private static Retrofit retrofit = null;

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static Retrofit getRetrofitInstance(){

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

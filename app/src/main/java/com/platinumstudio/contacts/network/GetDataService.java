package com.platinumstudio.contacts.network;

import com.platinumstudio.contacts.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface GetDataService {
//    @GET("/quote/random")
//    Call<Example> getRandomQuote(@HeaderMap Map<String, String> headerMap);

    @GET("/photos")
    Call<List<Photo>> getAllPhotos();
}
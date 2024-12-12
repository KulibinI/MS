package com.example.myapplication3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {
    @GET("/api/")
    Call<PixabayResponse> getImages(@Query("key") String apiKey, @Query("q") String query);
}
package com.example.myapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("posts")  // Измените на "todos" для получения данных
    Call<List<MyData>> getData();
}
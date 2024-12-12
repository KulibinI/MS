package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        loadButton = findViewById(R.id.loadButton);

        // Устанавливаем менеджер компоновки для RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Устанавливаем обработчик нажатий для кнопки
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJsonData();
            }
        });
    }

    private void loadJsonData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<MyData>> call = apiService.getData();

        call.enqueue(new Callback<List<MyData>>() {
            @Override
            public void onResponse(Call<List<MyData>> call, Response<List<MyData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Создаем адаптер и устанавливаем его в RecyclerView
                    adapter = new MyAdapter(response.body(), MainActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("API_ERROR", "Ошибка ответа: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<MyData>> call, Throwable t) {
                Log.e("API_ERROR", "Ошибка: " + t.getMessage());
            }
        });
    }
}
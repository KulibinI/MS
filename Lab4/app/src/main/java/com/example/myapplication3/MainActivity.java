package com.example.myapplication3;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "46271938-1548452966a0a363bfef5f589";
    private static final int STORAGE_PERMISSION_CODE = 101;
    private ImageView imageView;
    private EditText editMessage;
    private String imageUrl; // To store the URL of the fetched image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMessage = findViewById(R.id.edit_message);
        Button button = findViewById(R.id.button);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        ImageButton imageButton1 = findViewById(R.id.imageButton1);
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        imageView = findViewById(R.id.imageView);

        button.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            fetchImage(message);
        });

        button3.setOnClickListener(v -> downloadImage());

        button4.setOnClickListener(v -> openImageInBrowser());

        imageButton1.setOnClickListener(v -> {
            imageView.setImageResource(R.drawable.like_bill);
            Toast.makeText(MainActivity.this, "Like button clicked", Toast.LENGTH_SHORT).show();
        });

        imageButton2.setOnClickListener(v -> {
            imageView.setImageResource(R.drawable.dislike_bill);
            Toast.makeText(MainActivity.this, "Dislike button clicked", Toast.LENGTH_SHORT).show();
        });
    }



    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchImage(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pixabay.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PixabayApi api = retrofit.create(PixabayApi.class);
        Call<PixabayResponse> call = api.getImages(API_KEY, query);

        imageView.setImageResource(R.drawable.no_results);

        call.enqueue(new Callback<PixabayResponse>() {
            @Override
            public void onResponse(Call<PixabayResponse> call, Response<PixabayResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().hits.isEmpty()) {
                    imageUrl = response.body().hits.get(0).webformatURL; // Store the image URL
                    Picasso.get().load(imageUrl).into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.no_results);
                    Toast.makeText(MainActivity.this, "No images found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PixabayResponse> call, Throwable t) {
                imageView.setImageResource(R.drawable.no_results);
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadImage() {
        if (imageUrl != null) {
            requestStoragePermission(); // Request permission

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
            request.setTitle("Image Download");
            request.setDescription("Downloading image...");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_image.jpg");

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);

            Toast.makeText(this, "Downloading image...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No image to download", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageInBrowser() {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
            browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
            browserIntent.setData(Uri.parse(imageUrl));
            browserIntent.setPackage("com.android.chrome");
            try {
                startActivity(browserIntent);
            } catch (Exception e) {
                browserIntent.setPackage(null);
                startActivity(browserIntent);
            }
        } else {
            Toast.makeText(this, "No image", Toast.LENGTH_SHORT).show();
        }
    }
}
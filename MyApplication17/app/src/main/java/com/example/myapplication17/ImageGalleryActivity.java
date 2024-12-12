package com.example.myapplication17;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class ImageGalleryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        ImageView imageView = findViewById(R.id.imageView);
        String imageUrl = "https://example.com/sample_image.jpg";
        Picasso.get().load(imageUrl).into(imageView);
    }
}
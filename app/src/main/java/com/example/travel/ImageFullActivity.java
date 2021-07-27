package com.example.travel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.travel.Adapter.ImageFullAdapter;

import java.util.ArrayList;

public class ImageFullActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        Intent intent = getIntent();
        ArrayList<String> imageTitles = intent.getStringArrayListExtra("image");
        Integer position = intent.getIntExtra("position", 0);

        ViewPager viewPager = findViewById(R.id.viewPager);
        ImageFullAdapter imageFullAdapter = new ImageFullAdapter(ImageFullActivity.this, imageTitles);
        viewPager.setAdapter(imageFullAdapter);
        viewPager.setCurrentItem(position);
    }
}

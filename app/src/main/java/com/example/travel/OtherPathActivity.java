package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class OtherPathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_path);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.anim_slide_out_right_fast);

    }
}
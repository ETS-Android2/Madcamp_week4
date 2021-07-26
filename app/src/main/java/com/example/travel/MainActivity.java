package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.john.waveview.WaveView;

public class MainActivity extends AppCompatActivity {



    public static String username, useremail;
    private SeekBar seekBar;
    private WaveView waveView;
    private CardView mapcard, card;
    private FloatingActionButton toProf, toSearch;
    private EditText place;
    private Button placebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }



        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        useremail = intent.getStringExtra("email");

        toProf = findViewById(R.id.toProfile);
        toSearch = findViewById(R.id.toSearch);


//        seekBar = (SeekBar) findViewById(R.id.seek_bar);
//        waveView = (WaveView) findViewById(R.id.wave_view);
//
//        mapcard = findViewById(R.id.toMap);
//        place = findViewById(R.id.editPlace);
//        placebtn = findViewById(R.id.placeBtn);

//        card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
//                startActivity(intent);
//            }
//        });

        toProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserPathActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.anim_slide_in_right_fast, 0);
            }
        });

        toSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OtherPathActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.anim_slide_in_left_fast, 0);

            }
        });


//        placebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                intent.putExtra("place", place.getText().toString());
//                startActivity(intent);
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                waveView.setProgress(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }


}
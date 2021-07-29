package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class FrontActivity extends AppCompatActivity {

    EditText gettitle, getplace, getnumber;
    Button start;
    String email = MainActivity.useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        gettitle = findViewById(R.id.editTitle);
        getplace = findViewById(R.id.editPlace);
        getnumber = findViewById(R.id.editNumber);
        start = findViewById(R.id.placeBtn);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getnumber.getText().toString()=="1"){
                    ArrayList<String> blank = new ArrayList<>();
                    blank.add(email);
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("title", gettitle.getText().toString());
                    intent.putExtra("place", getplace.getText().toString());
                    intent.putExtra("friendlist", blank);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                    intent.putExtra("title", gettitle.getText().toString());
                    intent.putExtra("place", getplace.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0,0);

                }
            }
        });
    }
}
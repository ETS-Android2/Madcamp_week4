package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class JoinActivity extends AppCompatActivity {

    Button search, start;
    EditText editText;
    CardView friend;
    RecyclerView selected;
    TextView name;
    ArrayAdapter<String> adapter;

    String useremail = MainActivity.useremail;

    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        search = findViewById(R.id.button);
        start = findViewById(R.id.start);
        friend = findViewById(R.id.friend);
        selected = findViewById(R.id.selected);
        editText = findViewById(R.id.editText);
        name = findViewById(R.id.friendname);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , names);

        names = new ArrayList<>();

        //editText.getText().toString()으로 친구 찾기

        //찾은 친구 클릭하면 리스트에 추가
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.add(name.getText().toString());
                adapter.add(name.getText().toString());
                selected.setAdapter(adapter);

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DB에 저장? 인텐트로 넘김?

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("place", "대한민국");

                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });

    }
}
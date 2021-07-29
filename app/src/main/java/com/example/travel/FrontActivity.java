package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FrontActivity extends Fragment {

    EditText gettitle, getplace;
    Button start, btnPlus, btnMinus;
    TextView number;
    String email = MainActivity.useremail;

    Integer tmpNum = 1;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_front, container, false);



        gettitle = view.findViewById(R.id.editTitle);
        getplace = view.findViewById(R.id.editPlace);
        start = view.findViewById(R.id.placeBtn);
        btnMinus = view.findViewById(R.id.button_minus);
        btnPlus = view.findViewById(R.id.button_plus);
        number = view.findViewById(R.id.textView_num);

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmpNum == 1){
                    Toast.makeText(getContext(), "minimum value is 1", Toast.LENGTH_SHORT).show();
                } else{
                    tmpNum--;
                    number.setText(String.valueOf(tmpNum));
                }

            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpNum++;
                number.setText(String.valueOf(tmpNum));
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmpNum == 1){
                    ArrayList<String> blank = new ArrayList<>();
                    blank.add(email);
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("title", gettitle.getText().toString());
                    intent.putExtra("place", getplace.getText().toString());
                    intent.putExtra("friendlist", blank);
                    startActivity(intent);
//                    overridePendingTransition(0,0);
                }
                else{
                    Intent intent = new Intent(getActivity(), JoinActivity.class);
                    intent.putExtra("title", gettitle.getText().toString());
                    intent.putExtra("place", getplace.getText().toString());
                    startActivity(intent);
//                    overridePendingTransition(0,0);

                }
            }
        });


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
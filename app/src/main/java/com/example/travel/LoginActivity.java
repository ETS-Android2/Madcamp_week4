package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travel.items.Userinfo;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login, signup;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String BASE_URL = "http://192.249.18.176:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.emailEdit);
        password = findViewById(R.id.passwordEdit);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checklogin();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {

    }

    private void checklogin() {
        HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());

        Call<Userinfo> call = retrofitInterface.executeLogin(map);//로그인리절트 클래스 부르는데저 map넣어서 함

        call.enqueue(new Callback<Userinfo>() {
            @Override
            public void onResponse(Call<Userinfo> call, Response<Userinfo> response) {

                if (response.code() == 200) {
                    Log.d("look", "200");

                    Userinfo result = response.body();//응답의 내용. 이와같은 디비구조인게 UserInfo

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", result.getName());
                    intent.putExtra("email", result.getEmail());

                    startActivity(intent);

                    //finish();

                } else if (response.code() == 404) {
                    Log.d("look", "400");
                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Userinfo> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
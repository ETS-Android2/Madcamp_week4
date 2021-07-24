package com.example.travel;

import com.example.travel.items.Pathinfo;
import com.example.travel.items.Userinfo;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/user/login")
    Call<Userinfo> executeLogin(@Body HashMap<String, String> map);

    @POST("/course/courseList")
    Call<List<Pathinfo>> executeUserPath(@Body HashMap<String, String> map );
}

package com.example.travel;

import com.example.travel.items.Dateinfo;
import com.example.travel.items.Pathinfo;

import com.example.travel.items.SaveImageResponse;
import com.example.travel.items.SavePathInput;

import com.example.travel.items.Placeinfo;

import com.example.travel.items.Userinfo;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface RetrofitInterface {

    @POST("/user/login")
    Call<Userinfo> executeLogin(@Body HashMap<String, String> map);

    @POST("/user/userInfo")
    Call<Userinfo> getUserInfo(@Body HashMap<String, String> map);

    @POST("/user/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/course/courseList")
    Call<List<Pathinfo>> executeUserPath(@Body HashMap<String, String> map );

    @POST("/course/saveCourse")
    Call <Void> executeSavePath (@Body SavePathInput savePathInput);

    @POST("/course/singleSpot")
    Call <Placeinfo> executeSingleSpot (@Body HashMap<String, String> map );

    @Multipart
    @POST("/image/upload")
    Call<Void> uploadImage(@Part MultipartBody.Part[] image);

    @Multipart
    @POST("/image/uploadUserPic")
    Call<Void> uploadUserPic(@Part MultipartBody.Part[] image);

    @POST("/image/deleteUserPic")
    Call<Void> deleteUserPic(@Body HashMap<String, String> map);

    @Multipart
    @POST("/image/uploadThumbnail")
    Call<Void> uploadThumbnail(@Part MultipartBody.Part[] image);

    @POST("/image/deleteThumbnail")
    Call<Void> deleteThumbnail(@Body HashMap<String, String> map);

    @POST("/image/getImage")
    @Streaming
    Call<ResponseBody> getImage(@Body HashMap<String, String> map);

    @POST("/image/deleteImage")
    Call<Placeinfo> deleteImage(@Body HashMap<String, String> map);

    @GET("/search/getAll")
    Call<List<Pathinfo>> getAllPath();

    @POST("/user/sendDays")
    Call <Void> sendDays(@Body Dateinfo dateinfo);

    @POST("/user/getFriendDays")
    Call <Dateinfo> getFriendDays(@Body HashMap<String,String> map);
}

package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel.Adapter.JoinAdapter;
import com.example.travel.items.Userinfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity {

    Button search, start;
    EditText editText;
    CardView friend;
    RecyclerView selected;
    TextView name, email;
    ImageView prof;
    JoinAdapter adapter;
    Bitmap bmRotated;

    String useremail = MainActivity.useremail;
    private String BASE_URL = LoginActivity.BASE_URL;

    ArrayList<Bitmap> imgs = new ArrayList<>();
    ArrayList<String> emails = new ArrayList<>();

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private LinearLayoutManager linearLayoutManager;

    private String title, place;

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
        email = findViewById(R.id.friendemail);
        emails.add(useremail);
        prof= findViewById(R.id.profimg);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        place = intent.getStringExtra("place");

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected.setLayoutManager(linearLayoutManager);
        adapter = new JoinAdapter(getApplicationContext(), imgs);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //editText.getText().toString()으로 친구 찾기
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();

                map.put("email", editText.getText().toString());

                Call<Userinfo> call = retrofitInterface.executeSearch(map);

                call.enqueue(new Callback<Userinfo>() {
                    @Override
                    public void onResponse(Call<Userinfo> call, Response<Userinfo> response) {

                        if (response.code() == 200) {
                            Userinfo user = response.body();
                            name.setText(user.getName());
                            email.setText(user.getEmail());

                            // mImageTitles.get(position) 사진 db에서 불러와서 띄우기
                            HashMap<String, String> map = new HashMap<>();

                            map.put("name", user.getImage());
                            Call<ResponseBody> callImage = retrofitInterface.getImage(map);

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            callImage.enqueue(new Callback<ResponseBody>(){
                                @Override
                                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                    InputStream is = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                                    // filename에서 orientation 가져오기
                                    String[] tmpName = user.getImage().split("_");
//                    Log.d("kyung", String.valueOf(tmpName));
//                    Log.d("kyung", tmpName[4]);

                                    // 사진 회전 처리
                                    bmRotated = rotateBitmap(bitmap, Integer.parseInt(tmpName[1]));
//                    images.add(position, bitmap);
                                    prof.setImageBitmap(bmRotated);

//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bmRotated.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();

//                    imageByteArray.add(position, byteArray);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t){
                                    Log.d("bitmapfail", "String.valueOf(bitmap)");
                                    Toast.makeText(JoinActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
//                            Glide.with(getApplicationContext()).load(user. getImage()).into(prof);


                        } else if (response.code() == 400) {
                            Toast.makeText(JoinActivity.this,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Userinfo> call, Throwable t) {
                        Toast.makeText(JoinActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //찾은 친구 클릭하면 리스트에 추가
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ImageView item = null;
//                item.setImageBitmap(prof.getDrawingCache());
                imgs.add(bmRotated);
                emails.add(email.getText().toString());
                adapter = new JoinAdapter(getApplicationContext(), imgs);
                selected.setAdapter(adapter);

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("place", place);
                //인텐트로 친구리스트도 같이 보내기
                intent.putExtra("friendlist", emails);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });

    }

    // 사진 회전 처리 함수
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
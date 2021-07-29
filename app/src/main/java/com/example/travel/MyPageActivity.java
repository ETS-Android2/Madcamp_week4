package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel.Adapter.PathAdapter;
import com.example.travel.items.PathItem;
import com.example.travel.items.Pathinfo;
import com.example.travel.items.Placeinfo;
import com.example.travel.items.Userinfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageActivity extends Fragment {

    private static final int RESULT_OK = 1;
    private static final int REQUEST_CODE_CHOOSE = 23;

    private String username = MainActivity.username;
    private String useremail = MainActivity.useremail;

    private ArrayList<PathItem> pathlist=new ArrayList<>();
    private ArrayList<Placeinfo> plist=new ArrayList<>();
    private PathAdapter pathAdapter;

    TextView tvPosts, tvFriends , displayName;
    CircleImageView profileImg;
    RecyclerView recyclerView;
    FloatingActionButton pickDate;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = LoginActivity.BASE_URL;

    private String placetitle, placeregion;
    private Button imagePickBtn, imageDeleteBtn;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_page, container, false);
        tvPosts = view.findViewById(R.id.tvPosts);
        tvFriends = view.findViewById(R.id.tvFriends);
        displayName = view.findViewById(R.id.display_name);
        profileImg = view.findViewById(R.id.profile_image);
        recyclerView = view.findViewById(R.id.MyPageRecycler);

        imagePickBtn = view.findViewById(R.id.zhihu);
        imageDeleteBtn = view.findViewById(R.id.deletePic);
        pickDate = view.findViewById(R.id.PickDate);

        displayName.setText(username);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // pathList recyclerView 설정
        HashMap<String, String> map = new HashMap<>();
        map.put("email", useremail);

        Call<List<Pathinfo>> call = retrofitInterface.executeUserPath(map);

        call.enqueue(new Callback<List<Pathinfo>>() {
            @Override
            public void onResponse(Call<List<Pathinfo>> call, Response<List<Pathinfo>> response) {
                if(response.code() == 200) {
                    List<Pathinfo> resultList = response.body();
                    PathItem item = new PathItem();

                    for (Pathinfo result : resultList) {
                        Log.d("path", "" + result.getImage());
                        item = new PathItem(result.getTitle() , result.getRegion(),result.getTotalSize().toString(), result.getImage());
                        pathlist.add(item);
                    }

                    tvPosts.setText(String.valueOf(pathlist.size()));

                    pathAdapter = new PathAdapter(getContext(), pathlist);
                    recyclerView.setAdapter(pathAdapter);

                    pathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            placetitle =pathlist.get(position).getPathtitle();
                            placeregion = resultList.get(position).getRegion();
                            plist = resultList.get(position).getLocations();
                            Intent intent = new Intent(getContext(), UserPlaceActivity.class);
                            intent.putExtra("title", placetitle);
                            intent.putExtra("region", placeregion);
                            intent.putExtra("list", plist);
                            startActivity(intent);
//                            overridePendingTransition( R.anim.anim_slide_in_right_fast, 0);

                        }
                    });
                }
                else if (response.code() == 400) {
                    Log.d("look", "400");
                    Toast.makeText(getContext(), "Wrong Credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pathinfo>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });

        // user info 불러오기
        HashMap<String, String> mapInfo = new HashMap<>();
        mapInfo.put("email", MainActivity.useremail);
        Call<Userinfo> callInfo = retrofitInterface.getUserInfo(mapInfo);

        callInfo.enqueue(new Callback<Userinfo>() {
            @Override
            public void onResponse(Call<Userinfo> call, Response<Userinfo> response) {
                if (response.code() == 200) {
                    Userinfo result = response.body();

                    tvFriends.setText(String.valueOf(result.getFriends().size()));

                    if(!result.getImage().equals("")){
                        // 프로필 사진 불러오기
                        HashMap<String, String> mapPic = new HashMap<>();

                        mapPic.put("name", result.getImage());
                        Call<ResponseBody> callImage = retrofitInterface.getImage(mapPic);

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        callImage.enqueue(new Callback<ResponseBody>(){
                            @Override
                            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                if(response.code()==200){
                                    InputStream is = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                                    // filename에서 orientation 가져오기
                                    String[] tmpName = result.getImage().split("_");

//                                    Log.d("kyung2", tmpName[1]);

                                    // 사진 회전 처리
                                    Bitmap bmRotated = rotateBitmap(bitmap, Integer.parseInt(tmpName[1]));
                                    profileImg.setImageBitmap(bmRotated);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t){
                                Log.d("bitmapfail", "String.valueOf(bitmap)");
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } else if (response.code() == 400) {
                }

            }

            @Override
            public void onFailure(Call<Userinfo> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        // 프로필 사진 삭제하기
        imageDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> mapDel = new HashMap<>();

                mapDel.put("email",MainActivity.useremail);
                Call<Void> callImage = retrofitInterface.deleteUserPic(mapDel);

                callImage.enqueue(new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if(response.code()==200){
                            profileImg.setImageResource(R.drawable.custom_person_icon);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t){
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        // 프로필 사진 변경하기
        imagePickBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v){
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                startAction(v);
                            } else {
                                Toast.makeText(getContext(), R.string.permission_request_denied, Toast.LENGTH_LONG)
                                        .show();
                            }
                        }, Throwable::printStackTrace);
            }
        });

        //버튼 누르면 캘린더 이동 , 날짜 선택
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        tvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Not yet", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

//    public void onFriendsClick(View view){
//        // 친구들 리싸이클러뷰에 띄우기
//        Toast.makeText(getContext(), "Not yet", Toast.LENGTH_SHORT).show();
//    }

    private void startAction(View v) {
        Matisse.from(this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .gridExpectedSize(
                        getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .setOnSelectedListener((uriList, pathList) -> {
                    Log.e("onSelected", "onSelected: pathList=" + pathList);
                })
                .showSingleMediaType(true)
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(isChecked -> {
                    Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                })
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("kyung", String.valueOf(resultCode));
        if (requestCode == REQUEST_CODE_CHOOSE) {
            try {
                if(Matisse.obtainResult(data) != null){
                    saveImage(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
                }
//                saveImage(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(data)));
        }
    }

    // uri를 입력받아 byte array로 변환
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // 사용자의 프로필 사진을 db에 저장하기
    public void saveImage(List<Uri> uris, List<String> paths) throws IOException {
        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[uris.size()];
        for(int i=0;i<uris.size(); i++){
            InputStream iStream = requireActivity().getContentResolver().openInputStream(uris.get(i));
            byte[] imageBytes = getBytes(iStream);

            //사진 orientation 저장하기
            ExifInterface exif = null;
//            Log.d("kyung0", String.valueOf(uris.size()));
//            Log.d("kyung1", paths.get(i));
            try {
                exif = new ExifInterface(paths.get(i));
//                Log.d("kyung", paths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

//            Log.d("kyung0", String.valueOf(orientation));

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            String filename = MainActivity.useremail+"_"+orientation+"_.jpg";
            surveyImagesParts[i] = MultipartBody.Part.createFormData("image", filename, requestFile);

            Glide.with(MyPageActivity.this).load(uris.get(i)).into(profileImg);
        }

        Call<Void> call = retrofitInterface.uploadUserPic(surveyImagesParts);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT)
                        .show();
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
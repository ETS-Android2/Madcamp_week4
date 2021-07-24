package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;





import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,RoutingListener,GoogleApiClient.OnConnectionFailedListener {

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button , bt_searchpath;
    private EditText editText;

    Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    private ArrayList<UserLocation> clickedPath = new ArrayList<>();

    //
    private final static int LOCATION_REQUEST_CODE = 23 ;
    boolean locationPermission = false;
    private List<Polyline> polylines = null;

    private String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        editText = (EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        bt_searchpath = (Button)findViewById(R.id.bt_pathsearch);

        Intent intent = getIntent();
        place = intent.getStringExtra("place");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //경로 다각형 그려주는 버튼 . 비행기 타고 가야함
        bt_searchpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(polyline != null) polyline.remove();
//                PolylineOptions polylineOptions = new PolylineOptions()
//                        .addAll(latLngList).clickable(true);
//                polyline = mMap.addPolyline(polylineOptions);
                if(latLngList.size() >= 3) {
                    for (int i = 0; i < latLngList.size() - 2; i++) {
                        Findroutes(latLngList.get(i), latLngList.get(i + 1));
                    }
                }else if(latLngList.size() == 2){
                    Findroutes(latLngList.get(0) , latLngList.get(1));
                }else if(latLngList.size() == 1){
                    Toast.makeText(MapActivity.this, "장소를 두개 이상 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(MapActivity.this ,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyDCsK1Y92o6guzI4h0jmFHPc6Yz43EUENE")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("선택한 위치");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });
        ////////////////////

        // 장소를 입력하고 버튼을 누르면 그 위치에 마커가 표시됨
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = editText.getText().toString();
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title(str);
                mOptions2.snippet(address);
                mOptions2.position(point);

                mMap.addMarker(mOptions2);
                //검색한 장소로 카메라 이동
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,15));

                //검색한 장소 clickedPath에 추가.(장소명, 위도, 경도) 아마 db에 보낼 리스트
                //Location 은 임의로 만든 클래스임
                UserLocation location = new UserLocation(str , latitude , longitude);
                clickedPath.add(location);

                //폴리곤(못생긴경로) 그리기 위해 (위도,경도) 저장하는 리스트. LatLng은 원래 정의된 클래스임
                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                latLngList.add(latLng);

            }
        });

        // Add a marker in Sydney and move the camera
//        LatLng Seoul = new LatLng(36.5680281276506, 127.68838295507976);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Seoul, 7));


        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(
                    place, // 주소
                    10); // 최대 검색 결과 개수
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // 콤마를 기준으로 split
        String []splitStr = addressList.get(0).toString().split(",");
        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1);
        LatLng startCity = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startCity,13));

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MapActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
//        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(this, "onRoutingCancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();

    }
}
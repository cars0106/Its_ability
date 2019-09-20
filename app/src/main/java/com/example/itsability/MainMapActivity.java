package com.example.itsability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainMapActivity extends AppCompatActivity implements AutoPermissionsListener {
    View mapCard;
    TextView textView;

    private TMapView tMapView;

    /*
    https://stackoverflow.com/questions/33696488/getting-bitmap-from-vector-drawable
    Vector Drawable에서 Bitmap 객체 받는 방법

    https://github.com/mike-jung/DoItAndroid/blob/master/part2/chapter14/SampleLocation/app/src/main/java/org/techtown/location/MainActivity.java
    GPS 설정 및 Auto permission 참고 자료

    http://tmapapi.sktelecom.com/main.html#android/docs/androidDoc.TMapView_OnClickListenerCallback
    T-map api 가이드
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);


        try {


            // Card 객체 생성 및 visibility 속성 변경
            mapCard = (View) findViewById(R.id.map_card);
            mapCard.setVisibility(View.INVISIBLE);
            textView = findViewById(R.id.textView);

            //현재 위치 설정
            startLocationService();
            AutoPermissions.Companion.loadAllPermissions(this, 101);

            // TMap 생성
            LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);

            tMapView = new TMapView(this);
            tMapView.setSKTMapApiKey("5594960f-bb8d-46ea-94db-e4a68576b536");
            linearLayoutTmap.addView(tMapView);
            tMapView.setMarkerRotate(true);

            /*
            https://community.openapi.sk.com/t/marker/7010/4
            다중 마커 생성
            */
            final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘

            final ArrayList<TMapPoint> alTMapPoint = new ArrayList<>();
            alTMapPoint.add(new TMapPoint(37.496263, 126.959167)); // 숭실대학교 창신관
            alTMapPoint.add(new TMapPoint(37.576016, 126.976867)); // 광화문
            alTMapPoint.add(new TMapPoint(37.570432, 126.992169)); // 종로3가
            alTMapPoint.add(new TMapPoint(37.570194, 126.983045)); // 종로5가

            for (int i = 0; i < alTMapPoint.size(); i++) {
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(bitmap);
                markerItem1.setTMapPoint(alTMapPoint.get(i));
                tMapView.addMarkerItem("markerItem" + i, markerItem1);
            }
            tMapView.setCenterPoint(126.959167, 37.496263);

            // 마커 클릭 이벤트 설정
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                    mapCard.setVisibility(View.INVISIBLE);
                    ImageView cardImage = (ImageView)findViewById(R.id.map_cardImage);
                    Glide.with(cardImage)
                            .load("https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359.jpg?raw=true")
                            .thumbnail(0.3f)
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(25)))
                            .into(cardImage);

                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    for (TMapMarkerItem item : arrayList) {
                        mapCard.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), String.valueOf(item.getTMapPoint()), Toast.LENGTH_LONG).show();
                    }
                    Log.d("EndTest", " EndTest");
                    return false;
                }
            });

        }
        catch(Exception e) {
            e.printStackTrace();
        }


        //BottomNavigation 구현
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainIntent);
                        return true;
                    case R.id.action_ar:
                        return true;
                    case R.id.action_search:
                        return true;
                    case R.id.action_map:
                        return true;
                }
                return false;
            }
        });
    }

    //벡터이미지에서 비트맵 변환
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        drawable = (DrawableCompat.wrap(drawable)).mutate();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;

                textView.setText(message);


            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 1000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
            textView.setText(message);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }
}

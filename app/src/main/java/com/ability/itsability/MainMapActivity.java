package com.ability.itsability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ability.itsability.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.List;

public class MainMapActivity extends AppCompatActivity implements AutoPermissionsListener {
    View mapCard;
    ImageButton centerMapButton;

    private TMapView tMapView;
    private static final String TAG = "DESCRIPTION";


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
            centerMapButton = findViewById(R.id.map_makeCenterLocationButton);

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

            //마커아이콘 생성 후, Map Activity에 표시할 TMapPoint List를 가져옵니다.
            final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘
            List<TMapPoint> allTMapPoint = DataFromServer.getTMapPointForMapActivity();

            //가져온 TMapPoint List를 이용하여 마커들을 생성해줍니다.
            for (int i = 0; i < allTMapPoint.size(); i++) {
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(bitmap);
                markerItem1.setTMapPoint(allTMapPoint.get(i));
                tMapView.addMarkerItem("markerItem" + i, markerItem1);
            }

            // 마커 클릭 이벤트 설정
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                    mapCard.setVisibility(View.INVISIBLE);
                    centerMapButton.setVisibility(View.VISIBLE);

                    return false;
                }

                @Override
                //마커를 클릭했을 경우
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    for (TMapMarkerItem item : arrayList) {

                        //mapCard를 VISIBLE로, 지도 중심 이동 버튼을 INVISIBLE로 변경한다.
                        mapCard.setVisibility(View.VISIBLE);
                        centerMapButton.setVisibility(View.INVISIBLE);

                        //Marker의 Index를 이용하여 장소를 찾은 후, CardView에 해당 장소의 이름, 주소, 위치 설명 등을 보여준다.
                        int markerIndex = Integer.parseInt(item.getID().replace("markerItem",""));

                        ImageView cardImage = (ImageView)findViewById(R.id.map_cardImage);
                        TextView cardPlaceName = (TextView)findViewById(R.id.place_cardPlaceName);
                        TextView cardPlaceAddr = (TextView)findViewById(R.id.place_cardPlaceAddress);
                        TextView cardPlaceLocationDescription = (TextView)findViewById(R.id.place_cardPlaceDescription);
                        ImageView arSupport = (ImageView)findViewById(R.id.map_arSupport);

                        cardPlaceName.setText(DataFromServer.getLocationName(markerIndex));
                        cardPlaceAddr.setText(DataFromServer.getAddress(markerIndex));
                        cardPlaceLocationDescription.setText(DataFromServer.getPlaceLocationDescription(markerIndex));
                        if(!DataFromServer.getAR(markerIndex)) {arSupport.setVisibility(View.INVISIBLE);}
                        else {arSupport.setVisibility(View.VISIBLE);}

                        Glide.with(cardImage)
                                .load(DataFromServer.getImageUrl(markerIndex))
                                .thumbnail(0.3f)
                                .apply(new RequestOptions().transform(new CenterCrop()))
                                .into(cardImage);
                    }
                    return false;
                }
            });

        }
        catch(Exception e) {
            e.printStackTrace();
        }


        //BottomNavigation 구현
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(mainIntent,100);
                        return true;
                    case R.id.action_ar:
                        Intent arIntent = new Intent(getApplicationContext(),MainArActivity.class);
                        startActivityForResult(arIntent,100);
                        return true;
                    case R.id.action_search:
                        Intent searchIntent = new Intent(getApplicationContext(),MainSearchActivity.class);
                        startActivityForResult(searchIntent,100);
                        return true;
                    case R.id.action_map:
                        return true;
                }
                return false;
            }
        });

        //Card 누르면 설명 화면으로 넘어가기
        mapCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TextView placeName = (TextView)findViewById(R.id.place_cardPlaceName);

                Intent placeDescription = new Intent(getApplicationContext(),PlaceDescriptionActivity.class);
                placeDescription.putExtra("placeName",placeName.getText().toString());
                startActivity(placeDescription);
                return true;
            }
        });

        centerMapButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocationService();
            }
        });
    }

    //resultCode가 RESULT_OK일 경우, 하단바 선택을 원래대로 바꾸는 코드입니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
            bottomNavigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    //BackButton을 눌렀을 때, resultCode로 RESULT_OK를 반환하고 자신은 종료됩니다.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
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

        private boolean didMapCenter = false;

        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            //지도 중심점 현재 위치로 설정
            if(didMapCenter == false) {
                tMapView.setCenterPoint(longitude,latitude);
                didMapCenter = true;
            }
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
        //Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        //Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }
}

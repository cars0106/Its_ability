package com.example.itsability;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapOverlay;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;


public class PlaceDescriptionActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_place_description);

        //*
        try {

            TMapPoint currentPlacePoint = new TMapPoint(37.570194, 126.983045);


            // TMap 생성
            tMapView = (TMapView)findViewById(R.id.place_placeMap);
            tMapView.setSKTMapApiKey("5594960f-bb8d-46ea-94db-e4a68576b536");
            tMapView.setCenterPoint(currentPlacePoint.getLongitude(),currentPlacePoint.getLatitude());
            tMapView.setMarkerRotate(true);

            final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘

             // 종로5가
            TMapMarkerItem currentPlaceMarker = new TMapMarkerItem();
            currentPlaceMarker.setIcon(bitmap);
            currentPlaceMarker.setTMapPoint(currentPlacePoint);
            tMapView.addMarkerItem("currentMarker", currentPlaceMarker);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //*/

        /*
        TMapOverlay tMapOverlay = new TMapOverlay();
        tMapOverlay.draw(new Canvas().drawRoundRect(new RectF(100,100,100,100),10,10,),tMapView,false);

         */



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
}
package com.example.itsability;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapOverlay;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;


public class PlaceDescriptionActivity extends AppCompatActivity {

    private TMapView tMapView;
    private CardView cardView;
    private ImageView placeMainImageView;

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

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("placeName")) {
            TextView placeName = (TextView)findViewById(R.id.place_placeName);
            TextView placeAddr = (TextView)findViewById(R.id.place_placeAddress);
            placeName.setText(bundle.getString("placeName"));
            placeAddr.setText(bundle.getString("placeAddr"));
        }



        //PlaceMainImage 설정
        placeMainImageView = (ImageView)findViewById(R.id.place_PlaceMainImage);
        Glide.with(placeMainImageView)
                .load("https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359.jpg?raw=true")
                .thumbnail(0.3f)
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(placeMainImageView);



        // TMap 생성 후 CardView에 추가
        tMapView = new TMapView(this) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec,heightMeasureSpec);
                int width = getMeasuredWidth();
                int height = (int)(width * 0.4);
                setMeasuredDimension(width,height);
            }
        };
        cardView = (CardView)findViewById(R.id.place_placeMapCard);
        cardView.addView(tMapView);

        TMapPoint currentPlacePoint = new TMapPoint(37.570194, 126.983045);
        tMapView.setSKTMapApiKey("5594960f-bb8d-46ea-94db-e4a68576b536");
        tMapView.setCenterPoint(currentPlacePoint.getLongitude(),currentPlacePoint.getLatitude());
        tMapView.setMarkerRotate(true);

        //TMap에 Marker 추가
        final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘
        TMapMarkerItem currentPlaceMarker = new TMapMarkerItem();
        currentPlaceMarker.setIcon(bitmap);
        currentPlaceMarker.setTMapPoint(currentPlacePoint);
        tMapView.addMarkerItem("currentMarker", currentPlaceMarker);

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
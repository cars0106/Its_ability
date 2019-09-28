package com.example.itsability;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class PlaceDescriptionActivity extends AppCompatActivity {

    private TMapView tMapView;
    private CardView cardView;
    private ImageView placeMainImageView;

    private RequestQueue requestLocationQueue;
    private TextView textView;

    private String locationName;
    private static final String TAG = "DESCRIPTION";

    private RequestQueue requestInfoQueue;

    /*
    https://stackoverflow.com/questions/33696488/getting-bitmap-from-vector-drawable
    Vector Drawable에서 Bitmap 객체 받는 방법

    https://github.com/mike-jung/DoItAndroid/blob/master/part2/chapter14/SampleLocation/app/src/main/java/org/techtown/location/MainActivity.java
    GPS 설정 및 Auto permission 참고 자료

    http://tmapapi.sktelecom.com/main.html#android/docs/androidDoc.TMapView_OnClickListenerCallback
    T-map api 가이드

    https://developer.android.com/training/volley/simple#java
    Volley 가이드
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

        Bundle bundle = getIntent().getExtras();
        if(getIntent().hasExtra("placeName")) {
            TextView placeName = (TextView)findViewById(R.id.place_placeName);
            TextView placeAddr = (TextView)findViewById(R.id.place_placeAddress);
            locationName = bundle.getString("placeName");
            placeName.setText(locationName);
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

        tMapView.setSKTMapApiKey("5594960f-bb8d-46ea-94db-e4a68576b536");
        tMapView.setMarkerRotate(true);

        final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘

        //Volley 로 해당 장소 위치 받아와서 Marker 추가
        DataFromServer dataFromServer = new DataFromServer();
        List<String> w3wData = dataFromServer.returnW3WAddr(locationName);

        for (String i : w3wData) {

            requestLocationQueue = Volley.newRequestQueue(this);
            String url = "https://api.what3words.com/v3/convert-to-coordinates?words="+ i.trim() +"&key=F4VVBXGP";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject location = response.getJSONObject("coordinates");

                        String latitude = location.getString("lat");
                        String longitude = location.getString("lng");
                        TMapPoint currentPlacePoint = new TMapPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        tMapView.setCenterPoint(currentPlacePoint.getLongitude(),currentPlacePoint.getLatitude());

                        TMapMarkerItem currentPlaceMarker = new TMapMarkerItem();
                        currentPlaceMarker.setIcon(bitmap);
                        currentPlaceMarker.setTMapPoint(currentPlacePoint);
                        tMapView.addMarkerItem("currentMarker", currentPlaceMarker);

                        Log.d("TAG","MARKER ADDED");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Fail to Get Location JSONRequest", Toast.LENGTH_LONG).show();
                }
            });

            jsonObjectRequest.setTag(TAG);
            requestLocationQueue.add(jsonObjectRequest);

        }

        // Volley 로 해당 장소 정보 추가
        textView = (TextView) findViewById(R.id.place_description_text);
        requestInfoQueue = Volley.newRequestQueue(this);
        String url2 = dataFromServer.returnTourAPIUrl(locationName);

        JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject res = response.getJSONObject("response");
                    JSONObject body = res.getJSONObject("body");
                    JSONObject items = body.getJSONObject("items");
                    JSONObject item = items.getJSONObject("item");
                    String usefee = item.getString("usefee");
                    String usetimeculture = item.getString("usetimeculture");
                    String infocenterculture = item.getString("infocenterculture");
                    String restdateculture = item.getString("restdateculture");
                    String parkingculture = item.getString("parkingculture");
                    String parkingfee = item.getString("parkingfee");

                    String sum = "입장료 : " + usefee + "\n이용시간 : " + usetimeculture + "\n문의 및 안내 : " + infocenterculture +"\n쉬는날 : " + restdateculture + "\n주차 가능 여부 : " + parkingculture + "\n주차료 : " + parkingfee;
                    sum = sum.replaceAll("<br \\/>", "");
                    textView.setText(sum);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setVisibility(View.INVISIBLE);
            }
        });

        jsonObjectRequestInfo.setTag(TAG);
        requestInfoQueue.add(jsonObjectRequestInfo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestLocationQueue != null) {
            requestLocationQueue.cancelAll(TAG);
        }
        if (requestInfoQueue != null) {
            requestInfoQueue.cancelAll(TAG);
        }
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
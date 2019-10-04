package com.example.itsability;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;


public class PlaceDescriptionActivity extends AppCompatActivity {

    private TMapView tMapView;
    private CardView cardView;

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

            //TextView 초기화
            TextView placeName = (TextView)findViewById(R.id.place_placeName);
            TextView placeAddr = (TextView)findViewById(R.id.place_placeAddress);

            TextView recommandTime = (TextView)findViewById(R.id.place_recommandTime);
            TextView recommandTimeDetail = (TextView)findViewById(R.id.place_recommandTimeDetail);
            TextView notRecommandTime = (TextView)findViewById(R.id.place_notRecommandTime);
            TextView notRecommandTimeDetail = (TextView)findViewById(R.id.place_notRecommandTimeDetail);
            TextView recommandClothes = (TextView)findViewById(R.id.place_recommandClothes);
            TextView recommandClothesDetail = (TextView)findViewById(R.id.place_recommandClothesDetail);
            TextView otherTips = (TextView)findViewById(R.id.place_otherTips);
            TextView otherTipsDetail = (TextView)findViewById(R.id.place_otherTipsDetail);

            ImageView placeMainImageView = (ImageView)findViewById(R.id.place_PlaceMainImage);
            ImageView placeTimeImageView = (ImageView)findViewById(R.id.place_icRecommandTime);

            locationName = bundle.getString("placeName");
            Map<String, List<String>> descriptionData = DataFromServer.getPlaceDescriptionData(locationName);
            String url = DataFromServer.getImageUrl(locationName);

            placeName.setText(locationName);
            placeAddr.setText(DataFromServer.getAddress(locationName));

            recommandTime.setText(descriptionData.get("PhotoTime").get(1));
            recommandTimeDetail.setText(descriptionData.get("PhotoTime").get(2));
            notRecommandTime.setText(descriptionData.get("NotRecommandTime").get(0));
            notRecommandTimeDetail.setText(descriptionData.get("NotRecommandTime").get(1));
            recommandClothes.setText(descriptionData.get("RecommandClothes").get(0));
            recommandClothesDetail.setText(descriptionData.get("RecommandClothes").get(1));
            otherTips.setText(descriptionData.get("OtherTips").get(0));
            otherTipsDetail.setText(descriptionData.get("OtherTips").get(1));

            //PlaceMainImage 설정
            Glide.with(placeMainImageView)
                    .load(url)
                    .thumbnail(0.3f)
                    .apply(new RequestOptions().transform(new CenterCrop()))
                    .into(placeMainImageView);

            if(DataFromServer.getAR(locationName) == false) {
                ImageView arsupportBar = (ImageView)findViewById(R.id.place_arsupport_bar);
                ImageButton arguideButton = (ImageButton)findViewById(R.id.place_show_ar);

                arsupportBar.setVisibility(View.GONE);
                arguideButton.setVisibility(View.GONE);
            }

        }


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

        //마커아이콘 생성 후, Map Activity에 표시할 TMapPoint List를 가져옵니다.
        final Bitmap bitmap = getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.ic_map_pin); // 마커 아이콘
        List<TMapPoint> allTMapPoint = DataFromServer.getTMapPointForPlaceDescriptionActivity(locationName);

        tMapView.setCenterPoint(allTMapPoint.get(0).getLongitude(), allTMapPoint.get(0).getLatitude());

        //가져온 TMapPoint List를 이용하여 마커들을 생성해줍니다.
        for (int i = 0; i < allTMapPoint.size(); i++) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            markerItem1.setIcon(bitmap);
            markerItem1.setTMapPoint(allTMapPoint.get(i));
            tMapView.addMarkerItem("markerItem" + i, markerItem1);
        }


        // Volley 로 해당 장소 정보 추가
        requestInfoQueue = Volley.newRequestQueue(this);
        String url2 = DataFromServer.getTourAPIUrl(locationName);

        Log.d("TAG",url2);

        if(url2.isEmpty()) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.place_otherInformationLinear);
            linearLayout.setVisibility(View.GONE);
        }
        else {
            JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject res = response.getJSONObject("response");
                        JSONObject body = res.getJSONObject("body");
                        JSONObject items = body.getJSONObject("items");
                        JSONObject item = items.getJSONObject("item");

                        String extendString = "";

                        TextView usefeeText = (TextView)findViewById(R.id.place_usefeeDetail);
                        TextView usetimeText = (TextView)findViewById(R.id.place_usetimeDetail);
                        TextView restdateText = (TextView)findViewById(R.id.place_restdateDetail);
                        TextView parkingText = (TextView)findViewById(R.id.place_parkingDetail);
                        TextView parkingfeeText = (TextView)findViewById(R.id.place_parkingfeeDetail);


                        if(item.has("usetimeculture")) {extendString = "culture";}

                        if(item.has("usetime" + extendString)) {usetimeText.setText(item.getString("usetime" + extendString).replaceAll("<br \\/>", "")); }
                        else {
                            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.place_usetimeLinear);
                            linearLayout.setVisibility(View.GONE);
                        }

                        if(item.has("restdate" + extendString)) {restdateText.setText(item.getString("restdate" + extendString).replaceAll("<br \\/>", "")); }
                        else {
                            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.place_restdateLinear);
                            linearLayout.setVisibility(View.GONE);
                        }

                        if(item.has("parking" + extendString)) {parkingText.setText(item.getString("parking" + extendString).replaceAll("<br \\/>", "")); }
                        else{
                            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.place_parkingLinear);
                            linearLayout.setVisibility(View.GONE);
                        }

                        if(item.has("usefee")) { usefeeText.setText(item.getString("usefee").replaceAll("<br \\/>", ""));}
                        else {usefeeText.setText("없음");}

                        if(item.has("parkingfee")) {parkingfeeText.setText(item.getString("parkingfee").replaceAll("<br \\/>", ""));}
                        else {
                            LinearLayout parkingfeeLinear = (LinearLayout)findViewById(R.id.place_parkingfeeLinear);
                            parkingfeeLinear.setVisibility(View.GONE);
                        }

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

        final Button openPhotoDescription = (Button)findViewById(R.id.place_showPlaceDescription);
        openPhotoDescription.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoDescription = new Intent(getApplicationContext(), PhotoDescriptionActivity.class);
                photoDescription.putExtra("placeName",locationName);
                startActivity(photoDescription);
            }
        });
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

    public void onButtonShowARClicked(View view) {
        Intent intent = new Intent(getApplicationContext(),CloudAnchorActivity.class);
        intent.putExtra("locationName",locationName);
        startActivity(intent);
    }
}
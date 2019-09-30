package com.example.itsability;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.skt.Tmap.TMapPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();
    private static List<String> placeNameforSearchIndex = new ArrayList<>();
    private static List<List<String>> placeW3W = new ArrayList<>();
    private static List<TMapPoint> placeW3WtoCoordinateForMapActivity = new ArrayList<>();
    private static List<Map<String,List<String>>> placeDescriptionData = new ArrayList<>();

    private static final String tourAPIKey = "c5DXs4GAE2qWO%2BmeVvcCmSQIIXtCL9izfIzCA2%2BGJFkxuA4%2BapH9EXOR4fvRS0s3RrYuzL3ug8ducJchXZn9AQ%3D%3D";
    private static final String TAG = "DESCRIPTION";

    public static void addData(Map<String,Object> data, List<String> w3w, Map<String,List<String>> placeDescription, final Context context) {
        placeData.add(data);
        placeNameforSearchIndex.add(data.get("Location").toString());
        placeW3W.add(w3w);
        placeDescriptionData.add(placeDescription);

        addTMapPointForMapActivity(w3w.get(0),context);
    }

    public static String returnLocationName(int index) {
        return placeData.get(index).get("Location").toString();
    }

    public static String returnAddress(int index) {
        return placeData.get(index).get("Address").toString();
    }

    public static int returnDataSize() {
        return placeData.size();
    }

    public static String returnTourAPIUrl(String LocationName) {
        int index = placeNameforSearchIndex.indexOf(LocationName);
        String url = placeData.get(index).get("tourAPI").toString();

        if(url.isEmpty() == false) {
            url = url.replace("인증키",tourAPIKey);
            url = url.replace("MobileOS=ETC","MobileOS=AND");
            url = url.replace("MobileApp=TourAPI3.0_Guide","MobileApp=itsability");
            url = url+"&_type=json";
        }

        return url;
    }

    //한 장소에서 촬영된 여러개의 스팟의 마커를 PlaceDescription Activity에 추가할 때 사용합니다.
    public List<String> returnW3WAddr(String LocationName) {
        int index = placeNameforSearchIndex.indexOf(LocationName);
        return placeW3W.get(index);
    }

    //받아온 w3w값을 위,경도 값으로 변경한 후, placeW3WtoCoordinateForMapActivity에 추가합니다.
    public static void addTMapPointForMapActivity(String w3w, final Context context) {

        Log.d("TAG","Adding Data");

        RequestQueue requestLocationQueue = Volley.newRequestQueue(context);
        String url = "https://api.what3words.com/v3/convert-to-coordinates?words="+ w3w +"&key=F4VVBXGP";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //받아온lat, lng 값을 Double로 바꾼 후, TMapPoint의 형태로 저장
                    JSONObject location = response.getJSONObject("coordinates");

                    String latitude = location.getString("lat");
                    String longitude = location.getString("lng");
                    Log.d("TAG","Saving lat : "+latitude + " lng : "+longitude);
                    TMapPoint placePoint = new TMapPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    placeW3WtoCoordinateForMapActivity.add(placePoint);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Fail to Get Location JSONRequest", Toast.LENGTH_LONG).show();
            }
        });

        jsonObjectRequest.setTag(TAG);
        requestLocationQueue.add(jsonObjectRequest);
    }

    public static List<TMapPoint> returnTMapPointForMapActivity() {
        return placeW3WtoCoordinateForMapActivity;
    }



}

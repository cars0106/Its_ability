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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();
    private static List<String> placeName = new ArrayList<>();

    /*
    W3W값의 위/경도 변환과 관련된 Map입니다.
    Volley로 Parsing한 파일들이 Parsing을 요구한 순서대로 저장되지 않는 현상이 있어, Map을 이용하여 저장합니다.

    placeW3WtoCoordinate는 모든 W3W값들을 TMapPoint의 형태로 저장합니다.
    모든 Key들은 장소 이름 + index의 형태로 구성됩니다.
    (ex. 63빌딩0 : TMapPoint1, 63빌딩1 : TMapPoint2, 롯데월드0 : TMapPoint3)

    placeW3WNum은 해당 장소에 몇개의 W3W값이 있는지를 저장합니다.
    모든 Key들은 장소의 이름입니다.
    (ex. 63빌딩 : 2, 롯데월드 : 1)
     */
    private static Map<String,TMapPoint> placeW3WtoCoordinate = new HashMap<>();
    private static Map<String,Integer> placeW3WNum = new HashMap<>();

    private static List<Map<String,List<String>>> placeDescriptionData = new ArrayList<>();
    private static List<List<Map<String,String>>> photoDescriptionData = new ArrayList<>();

    private static final String tourAPIKey = "c5DXs4GAE2qWO%2BmeVvcCmSQIIXtCL9izfIzCA2%2BGJFkxuA4%2BapH9EXOR4fvRS0s3RrYuzL3ug8ducJchXZn9AQ%3D%3D";
    private static final String TAG = "DESCRIPTION";

    public static void addData(Map<String,Object> data, List<String> w3w, Map<String,List<String>> placeDescription, List<Map<String,String>> photoDescription, final Context context) {

        String locationName = data.get("Location").toString();

        placeData.add(data);
        placeName.add(locationName);
        placeDescriptionData.add(placeDescription);
        photoDescriptionData.add(photoDescription);

        try {Log.d("TAG",photoDescription.toString()); }
        catch(Exception e) { Log.d("TAG","PhotoDescription is Null"); }

        placeW3WNum.put(locationName,0);
        for(String i : w3w) { addTMapPoint(locationName,i,context); }
    }

    public static String getLocationName(int index) {
        return placeData.get(index).get("Location").toString();
    }

    public static String getAddress(int index) {
        return placeData.get(index).get("Address").toString();
    }

    public static String getAddress(String locationName) {
        int index = placeName.indexOf(locationName);
        return placeData.get(index).get("Address").toString();
    }

    public static String getPlaceLocationDescription(int index) {
        return placeData.get(index).get("PlaceLocationDescription").toString();
    }

    public static int getDataSize() {
        return placeData.size();
    }

    public static String getTourAPIUrl(String LocationName) {
        int index = placeName.indexOf(LocationName);
        String url = placeData.get(index).get("tourAPI").toString();

        if(url.isEmpty() == false) {
            url = url.replace("인증키",tourAPIKey);
            url = url.replace("MobileOS=ETC","MobileOS=AND");
            url = url.replace("MobileApp=TourAPI3.0_Guide","MobileApp=itsability");
            url = url+"&_type=json";
        }

        return url;
    }

    public static Map<String,List<String>> getPlaceDescriptionData(String locationName) {
        int index = placeName.indexOf(locationName);
        return placeDescriptionData.get(index);
    }

    //받아온 w3w값을 위,경도 값으로 변경한 후, placeW3WtoCoordinateForMapActivity에 추가합니다.
    public static void addTMapPoint(final String locationName, String w3w, final Context context) {

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

                    //아래의 코드를 통해 {63빌딩0 : TMapPoint1} 과 같은 형태로 좌표를 저장합니다.
                    int num = placeW3WNum.get(locationName);
                    String key = locationName + String.valueOf(num);
                    placeW3WtoCoordinate.put(key, placePoint);

                    //좌표를 저장한 후, 해당 위치에 1개의 좌표값이 추가되었다는 것을 알려주기 위해 placeW3WNum에 1을 추가합니다.
                    placeW3WNum.put(locationName,num+1);

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

    public static List<TMapPoint> getTMapPointForMapActivity() {
        List<TMapPoint> placeCoordinateForMapActivity = new ArrayList<>();

        for(String i : placeName) {
            placeCoordinateForMapActivity.add(placeW3WtoCoordinate.get(i+"0"));
        }

        return placeCoordinateForMapActivity;
    }

    public static List<TMapPoint> getTMapPointForPlaceDescriptionActivity(String locationName) {

        List<TMapPoint> placeCoordinateForPlaceDescription = new ArrayList<>();

        for(int i = 0; i<placeW3WNum.get(locationName); i++) {
            placeCoordinateForPlaceDescription.add(placeW3WtoCoordinate.get(locationName+String.valueOf(i)));
        }

        return placeCoordinateForPlaceDescription;
    }




}

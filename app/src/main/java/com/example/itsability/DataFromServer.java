package com.example.itsability;


import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();
    private static List<String> placeNameforSearchIndex = new ArrayList<>();
    private static List<List<String>> placeW3W = new ArrayList<>();
    private static List<Map<String,List<String>>> placeDescriptionData = new ArrayList<>();
    private static final String tourAPIKey = "c5DXs4GAE2qWO%2BmeVvcCmSQIIXtCL9izfIzCA2%2BGJFkxuA4%2BapH9EXOR4fvRS0s3RrYuzL3ug8ducJchXZn9AQ%3D%3D";


    public void addData(Map<String,Object> data, List<String> W3W, Map<String,List<String>> placeDescription) {
        placeData.add(data);
        placeNameforSearchIndex.add(data.get("Location").toString());
        placeW3W.add(W3W);
        placeDescriptionData.add(placeDescription);
    }

    public Map<String,Object> getDataFromLocationName(String name) {
        int index = placeNameforSearchIndex.indexOf(name);
        return placeData.get(index);
    }

    public List<Map<String,Object>> returnData() {
        return placeData;
    }

    public String returnLocationName(int index) {
        return placeData.get(index).get("Location").toString();
    }

    public String returnAddress(int index) {
        return placeData.get(index).get("Address").toString();
    }

    public int returnDataSize() {
        return placeData.size();
    }

    public String returnTourAPIUrl(String LocationName) {
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

    public List<String> returnW3WAddr(String LocationName) {
        /*
        Map<String,Object> locationData = getDataFromLocationName(LocationName);

        //Object를 String으로 바꾼 후, 이를 Substring, split으로 이용하여 배열의 형태로 변환
        String origin = locationData.get("W3W").toString();
        origin = origin.substring(1, origin.length() - 1);
        List<String> list = Arrays.asList(origin.split(","));

        return list;

         */

        int index = placeNameforSearchIndex.indexOf(LocationName);
        return placeW3W.get(index);

    }

    public void addW3WValue(List<String> value) {
        placeW3W.add(value);
    }

    //Map Activity에 마커를 찍을때(모든 장소들의 W3W값을 가져와야 할 때) 사용합니다.
    public List<String> returnAllW3WAddr() {

        List<String> returnW3W = new ArrayList<>();
        for(List<String> i : placeW3W) { returnW3W.add(i.get(0)); }
        return returnW3W;
    }


}

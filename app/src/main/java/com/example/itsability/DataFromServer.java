package com.example.itsability;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();
    private static List<String> placeNameforSearchIndex = new ArrayList<>();
    private static final String tourAPIKey = "c5DXs4GAE2qWO%2BmeVvcCmSQIIXtCL9izfIzCA2%2BGJFkxuA4%2BapH9EXOR4fvRS0s3RrYuzL3ug8ducJchXZn9AQ%3D%3D"


    public void addData(Map<String,Object> data) {
        placeData.add(data);
        placeNameforSearchIndex.add(data.get("Location").toString());
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
}

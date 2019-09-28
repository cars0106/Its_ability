package com.example.itsability;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();
    private static List<String> placeNameforSearchIndex = new ArrayList<>();


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


}

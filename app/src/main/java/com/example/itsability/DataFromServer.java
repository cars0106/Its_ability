package com.example.itsability;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFromServer {

    private static List<Map<String,Object>> placeData = new ArrayList<>();

    public void addData(Map<String,Object> data) {
        placeData.add(data);
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

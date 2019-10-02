package com.example.itsability;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.skt.Tmap.TMapPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//스플래쉬 구현은 https://yongtech.tistory.com/100, https://lx5475.github.io/2017/07/15/android-splash/ 여기를 참고했습니다.
//Splash에서 데이터를 받아온 후, MainActivity에서 레이아웃을 구성할 수 있도록 여유시간을 설정하였습니다.

public class SplashActivity extends Activity {

    private Context context;
    private static final String TAG = "DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        try {

            //서버에서 데이터를 받아서 저장하는 Thread입니다.
            Thread dataReadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    //FirebaseFirestore Instance 생성
                    FirebaseFirestore FirebaseDB = FirebaseFirestore.getInstance();

                    //Firestore에서 "data" collection들의 값을 로드합니다.
                    FirebaseDB.collection("data")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    //성공적으로 Firestore에서 값을 불러왔을 경우, 작업을 수행합니다.
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            //"Data" Collection 안에 있는 Document들을 하나씩 가져온 후, DataFromServer에 저장합니다.
                                            Map<String, Object> data = document.getData();
                                            data.remove("PhotoDescription");
                                            data.remove("PlaceDescription");
                                            data.remove("W3W");

                                            List<String> w3wData = (List<String>) document.get("W3W");
                                            Map<String, List<String>> placeDescriptionData = (Map<String, List<String>>) document.get("PlaceDescription");
                                            List<Map<String,String>> photoDescriptionData = (List<Map<String,String>>) document.get("PhotoDescription");

                                            DataFromServer.addData(data, w3wData, placeDescriptionData, photoDescriptionData, context);
                                        }
                                    }
                                }
                            });
                }
            });

            dataReadThread.start();
            dataReadThread.join();

            //MainActivity에서 RecyclerView를 만들때까지의 여유시간을 줍니다.
            Thread.sleep(3000);
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

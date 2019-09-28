package com.example.itsability;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

//스플래쉬 구현은 https://yongtech.tistory.com/100, https://lx5475.github.io/2017/07/15/android-splash/ 여기를 참고했습니다.
//로딩시간은 1초로 임의로 설정했습니다.

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            //서버에서 데이터를 받아서 저장하는 Thread입니다.
            Thread dataReadThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    //서버에서 가져온 데이터를 저장할 DataFromServer Instance 생성
                    final DataFromServer dataGetInstance = new DataFromServer();

                    //FirebaseFirestore Instance 생성
                    FirebaseFirestore FirebaseDB = FirebaseFirestore.getInstance();

                    //Firestore에서 "data" collection들의 값을 로드합니다.
                    FirebaseDB.collection("data")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    //성공적으로 Firestore에서 값을 불러왔을 경우, 작업을 수행합니다.
                                    if(task.isSuccessful()) {
                                        for(QueryDocumentSnapshot document : task.getResult()) {

                                            //"Data" Collection 안에 있는 Document들을 하나씩 가져온 후, DataFromServer에 저장합니다.
                                            Map<String,Object> data = document.getData();
                                            dataGetInstance.addData(data);
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

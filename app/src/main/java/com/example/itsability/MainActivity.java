package com.example.itsability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //RecyclerView 구현을 위한 Adapter
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        

        openRecyclerView();

        //BottomNavigation 구현
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;
                    case R.id.action_ar:
                        return true;
                    case R.id.action_search:
                        return true;
                    case R.id.action_map:
                        Intent mapIntent = new Intent(getApplicationContext(), MainMapActivity.class);
                        startActivity(mapIntent);
                        return true;
                }
                return false;
            }
        });
    }

    //RecyclerView 구현과 관련된 메소드
    private void openRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            //*
            //RecyclerView의 Scroll을 막기 위함
            @Override
            public boolean canScrollVertically() {
                return false;
            }
             //*/
        };
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        addAdapterItem();
        recyclerView.setAdapter(adapter);


    }

    //RecyclerView에서 사용할 데이터를 불러오는 함수입니다.
    //Back-End 작업이 끝나면 서버에서 호출하여 가져온 값을 저장하도록 코드를 수정해야 합니다.
    private void addAdapterItem() {

        List<String> testName = Arrays.asList(
                "숭실대학교",
                "청계천",
                "반포한강공원",
                "호텔캐슬",
                "미디어스튜디오",
                "남산타워",
                "숭실대학교",
                "청계천",
                "반포한강공원",
                "호텔캐슬",
                "미디어스튜디오",
                "남산타워"
        );

        List<String> testAddr = Arrays.asList(
                "서울특별시 동작구 상도로 369",
                "서울특별시 종로구 창신동",
                "서울특별시 서초구 반포동",
                "서울특별시 송파구 올림픽로 240",
                "서울특별시 동작구 상도로 369",
                "서울특별시 용산구 남산공원길 103",
                "서울특별시 동작구 상도로 369",
                "서울특별시 종로구 창신동",
                "서울특별시 서초구 반포동",
                "서울특별시 송파구 올림픽로 240",
                "서울특별시 동작구 상도로 369",
                "서울특별시 용산구 남산공원길 103"
        );

        for(int i = 0; i<testName.size();i++) {
            RecyclerData t = new RecyclerData();
            t.setLocationName(testName.get(i));
            t.setLocationAddr(testAddr.get(i));

            adapter.addItem(t);


        }
    }
}

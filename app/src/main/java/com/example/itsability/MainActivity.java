package com.example.itsability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;
                    case R.id.action_ar:
                        Intent arIntent = new Intent(getApplicationContext(),MainArActivity.class);
                        startActivity(arIntent);
                        return true;
                    case R.id.action_search:
                        Intent searchIntent = new Intent(getApplicationContext(),MainSearchActivity.class);
                        startActivity(searchIntent);
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

        /*
        https://stackoverflow.com/questions/40003238/recyclerview-2-columns-with-cardview
        Recycler View에서 두개의 줄을 만들기 위해서 LinearLayoutManager대신 GridLayoutManager를 사용합니다.
        이 부분은 위의 StackOverFlow 링크를 참조했습니다.

        https://pupli.net/2017/07/31/android-recyclerview-gridlayoutmanager-column-spacing-by-itemdecoration/
        Recycler View에서 줄 사이의 공백을 만드는데는 recyclerView.additemDecoration()을 사용합니다.
        이 부분은 위의 링크를 참조했습니다.
        */

        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2) {
            //RecyclerView의 Scroll을 막기 위함
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new RecyclerDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_spacing)));

        //RecyclerView에 ItemTouchListener를 추가합니다.
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        RecyclerData data = adapter.returnItem(position);

                        Intent placeDescriptionIntent = new Intent(getApplicationContext(), PlaceDescriptionActivity.class);
                        placeDescriptionIntent.putExtra("placeName",data.getLocationName());
                        placeDescriptionIntent.putExtra("placeAddr",data.getLocationAddr());
                        startActivity(placeDescriptionIntent);
                    }
                })
        );

        adapter = new RecyclerAdapter();
        addAdapterItem();
        recyclerView.setAdapter(adapter);
    }

    //RecyclerView에서 사용할 데이터를 불러오는 함수입니다.
    //Back-End 작업이 끝나면 서버에서 호출하여 가져온 값을 저장하도록 코드를 수정해야 합니다.
    private void addAdapterItem() {

        //GetDataFromServer의 인스턴스를 생성한 후, returnData로 저장한 값들을 가져옵니다.
        //List<Map<String,Object>> data = dataInstance.returnData();

        List<String> testImageUrl = Arrays.asList(
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_01.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_02.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_03.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_04.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_05.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_06.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_07.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_08.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_09.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_10.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_11.jpg?raw=true",
                "https://github.com/SebinLee/itsability_photo/blob/master/KakaoTalk_20190912_143049359_12.jpg?raw=true"
            );


        DataFromServer dataInstance = new DataFromServer();

        for(int i = 0; i<dataInstance.getDataSize(); i++) {
            RecyclerData t = new RecyclerData();
            t.setLocationName(dataInstance.getLocationName(i));
            t.setLocationAddr(dataInstance.getAddress(i));
            t.setImageUrl(testImageUrl.get(i));
            adapter.addItem(t);
        }
    }

}

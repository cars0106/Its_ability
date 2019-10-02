package com.example.itsability;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
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

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigation = (BottomNavigationView)MainActivity.this.findViewById(R.id.bottom_navigation);
        int selectedItemId = bottomNavigation.getSelectedItemId();

        Log.d("TAG","SelectedItemId : " + String.valueOf(selectedItemId));
        Log.d("TAG","SelectedItemId : " + String.valueOf(R.id.action_home));

        if(R.id.action_home != selectedItemId) { bottomNavigation.setSelectedItemId(R.id.action_home); }
        else {super.onBackPressed();}
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

        for(int i = 0; i<DataFromServer.getDataSize(); i++) {
            RecyclerData t = new RecyclerData();
            t.setLocationName(DataFromServer.getLocationName(i));
            t.setLocationAddr(DataFromServer.getAddress(i));
            t.setImageUrl(DataFromServer.getImageUrl(i));
            t.setAR(DataFromServer.getAR(i));
            adapter.addItem(t);
        }
    }

}

package com.ability.itsability;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;


public class MainSearchActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private List<TextView> textViewPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);


        //BottomNavigation 구현
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivityForResult(mainIntent,100);
                        return true;
                    case R.id.action_ar:
                        Intent arIntent = new Intent(getApplicationContext(),MainArActivity.class);
                        startActivityForResult(arIntent,100);
                        return true;
                    case R.id.action_search:
                        return true;
                    case R.id.action_map:
                        Intent mapIntent = new Intent(getApplicationContext(), MainMapActivity.class);
                        startActivityForResult(mapIntent,100);
                        return true;
                }
                return false;
            }
        });

        //TextView에 터치이벤트 할당
        addTextViewTouchEvent();
    }

    //resultCode가 RESULT_OK일 경우, 하단바 선택을 원래대로 바꾸는 코드입니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }
    }

    //BackButton을 눌렀을 때, resultCode로 RESULT_OK를 반환하고 자신은 종료됩니다.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
    private void addTextViewTouchEvent() {

        //OnTouchListener는 TextView를 터치했을 때, 해당 Text의 ID를 putExtra()를 통해 다음 액티비티로 넘겨주며 액티비티를 실행합니다.
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {

            /*
            https://stackoverflow.com/questions/10137692/how-to-get-resource-name-from-resource-id
            ID를 문자열로 가져오는 것은 위의 방법을 참조하였습니다.
             */

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String touchId = v.getResources().getResourceEntryName(v.getId());
                touchId = touchId.substring(4);

                Intent placeSearchResult = new Intent(getApplicationContext(),PlaceSearchActivity.class);
                placeSearchResult.putExtra("location",touchId);
                startActivity(placeSearchResult);

                return true;

            }
        };


        //장소이름이 적힌 모든 TextView를 리스트의 요소로 추가합니다.
        textViewPlaceName = Arrays.asList(
                (TextView)findViewById(R.id.txt_seoul_dongjak),
                (TextView)findViewById(R.id.txt_seoul_dobong),
                (TextView)findViewById(R.id.txt_seoul_dongdaemun),
                (TextView)findViewById(R.id.txt_seoul_eunpyeong),
                (TextView)findViewById(R.id.txt_seoul_gangbuk),
                (TextView)findViewById(R.id.txt_seoul_gangdong),
                (TextView)findViewById(R.id.txt_seoul_gangnam),
                (TextView)findViewById(R.id.txt_seoul_gangseo),
                (TextView)findViewById(R.id.txt_seoul_geumcheon),
                (TextView)findViewById(R.id.txt_seoul_guro),
                (TextView)findViewById(R.id.txt_seoul_gwanak),
                (TextView)findViewById(R.id.txt_seoul_gwangjin),
                (TextView)findViewById(R.id.txt_seoul_jongno),
                (TextView)findViewById(R.id.txt_seoul_jung),
                (TextView)findViewById(R.id.txt_seoul_jungnang),
                (TextView)findViewById(R.id.txt_seoul_mapo),
                (TextView)findViewById(R.id.txt_seoul_nowon),
                (TextView)findViewById(R.id.txt_seoul_seocho),
                (TextView)findViewById(R.id.txt_seoul_seodaemun),
                (TextView)findViewById(R.id.txt_seoul_seongbuk),
                (TextView)findViewById(R.id.txt_seoul_seongdong),
                (TextView)findViewById(R.id.txt_seoul_songpa),
                (TextView)findViewById(R.id.txt_seoul_yangcheon),
                (TextView)findViewById(R.id.txt_seoul_yeongdengpo),
                (TextView)findViewById(R.id.txt_seoul_yongsan)
        );

        //textViewPlaceName에 존재하는 모든 요소에 onTouchListener를 연결합니다.
        for(TextView i : textViewPlaceName) {
            i.setOnTouchListener(onTouchListener);
        }
    }
}

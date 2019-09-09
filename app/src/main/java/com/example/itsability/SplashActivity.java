package com.example.itsability;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//스플래쉬 구현은 https://yongtech.tistory.com/100 여기를 참고했습니다.
//로딩시간으로 초기화면 시간 설정한게 아니라 임의로 4초로 설정했는데, 나중에 수정할 예정

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

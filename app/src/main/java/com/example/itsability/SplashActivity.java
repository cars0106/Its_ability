package com.example.itsability;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//스플래쉬 구현은 https://yongtech.tistory.com/100, https://lx5475.github.io/2017/07/15/android-splash/ 여기를 참고했습니다.
//로딩시간은 1초로 임의로 설정했습니다.

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.ability.itsability;

import android.app.Activity;
import android.view.View;

public class FullScreenHelper {
    public static void setFullScreenOnWindowFocusChanged(Activity activity, boolean hasFocus) {
        if (hasFocus) {
            // https://developer.android.com/training/system-ui/immersive.html#sticky
            activity
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

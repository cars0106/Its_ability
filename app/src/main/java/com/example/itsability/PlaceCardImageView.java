package com.example.itsability;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

//RecyclerView에 가로세로가 동일한 ImageView를 만들기 위해서 사용하는 Custom View

public class PlaceCardImageView extends AppCompatImageView {

    public PlaceCardImageView(Context context) {
        super(context);
    }

    public PlaceCardImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceCardImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width= getMeasuredWidth();
        int height = (int)(width * 0.5);
        setMeasuredDimension(width, height);
    }
}
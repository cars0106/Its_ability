package com.example.itsability;

import android.content.Context;
import android.util.AttributeSet;

import androidx.cardview.widget.CardView;

public class PlaceCardView extends CardView {

    public PlaceCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (int)(width * 0.61);
        setMeasuredDimension(width,height);
    }
}

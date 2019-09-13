package com.example.itsability;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/*
https://pupli.net/2017/07/31/android-recyclerview-gridlayoutmanager-column-spacing-by-itemdecoration/
이 클래스는 RecyclerView에서 열간의 공백을 만드는데 사용되는 클래스입니다.
이 클래스는 위의 링크를 바탕으로 작성되었습니다.
 */

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int layoutPos = parent.getChildLayoutPosition(view);

        outRect.bottom = space;

        //맨 위에 두개의 사진에만 Top Margin을 적용하기 위한 코드이다.
        if (layoutPos <= 1) {outRect.top = space; }
        else { outRect.top = 0; }

        //왼쪽열에는 왼쪽,오른쪽 Margin을 모두 적용하고 오른쪽 열에는 Left Margin을 적용하지 않는다.
        if(layoutPos%2==0) { outRect.left=space; outRect.right=space;}
        else {outRect.left = 0; outRect.right=space;}
    }
}

package com.ability.itsability;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/*
https://onlyformylittlefox.tistory.com/9?category=556987
https://liveonthekeyboard.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-RecyclerView-2-OnItemClick-%EB%A6%AC%EC%8A%A4%EB%84%88-%EA%B5%AC%ED%98%84
이 클래스는 RecyclerView에서 아이템별로 Touch Event를 발생시키기 위해 제작한 Class입니다.
이 클래스의 구현을 위해 위의 링크들을 참조했습니다.
 */


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    //Item을 클릭하였을 때, 어떤 행동을 할지 저장할 Interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //이 RecyclerItemClickListener의 생성자
    //이 Listener를 사용하는 코드에서 Override한 OnItemClickListener를 가져와서 저장하고, mGestureDetector를 새롭게 생성한다.
    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;

        //mGestureDetector의onSIngleTapUp()을 return true Override하는 것은 Item을 누르고 뗄 때 한번만 인식하도록 하기 위함이다.
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {return true;}
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        //MotionEvent의 getX(), getY()를 이용하여 현재 터치한 부분의 x,y좌표값을 가져옴
        //view.findChildViewUnder(x,y)를 이용하여 현재 터치한 부분의 아래에 있는 View를 가져옴
        View clickedChildView = view.findChildViewUnder(e.getX(),e.getY());


        //만약 눌린 부분에 View가 있을 경우(clickedChildView가 null이 아닌 경우)
        //OnItemClickListener가 생성되어 있을 경우(mListener가 null이 아닌 경우)
        //mGestureDetector에서 TouchEvent가 감지 되었을 경우 실행
        if(clickedChildView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {

            //OnItemClickListener에 있는 onItemClick를 실행
            //이때, view.getChildAdapterPosition()을 이용하여 현재 클릭한 뷰가 RecyclerView에서 몇번째에 있는 아이템인지 알려준다.
            mListener.onItemClick(clickedChildView, view.getChildAdapterPosition(clickedChildView));
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}

package com.example.itsability;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/*
https://dev-imaec.tistory.com/27
이 Java 코드는 RecyclerView에 데이터를 연결시키기 위한 RecyclerAdapter입니다.
이 Java 코드는 위의 링크를 참조하여 작성되었습니다.
*/

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    //Adapter에 들어갈 list 입니다.
    //Generic에 들어갈 Class는 RecyclerView에 적용할 데이터들을 저장한 클래스입니다.
    private ArrayList<RecyclerData> recyclerDataList = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate(xml에 있는 View의 정의를 실제 View로 만드는 것) 시킵니다.
        //LayoutInflater를 이용하여 만든 ViewHolder를 return 시킵니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_itemview, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(recyclerDataList.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return recyclerDataList.size();
    }

    void addItem(RecyclerData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        recyclerDataList.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_PlaceName;
        private TextView textView_PlaceAddr;
        //private ImageView imageView;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView_PlaceName = itemView.findViewById(R.id.recycler_placeName);
            textView_PlaceAddr = itemView.findViewById(R.id.recycler_placeAddress);
            //imageView = itemView.findViewById(R.id.imageView);
        }

        //View에 값을 Bind(할당, 연결) 시키는 메서드입니다.
        void onBind(RecyclerData data) {
            textView_PlaceName.setText(data.getLocationName());
            textView_PlaceAddr.setText(data.getLocationAddr());
            //imageView.setImageResource(data.getImageUrl());
        }
    }
}
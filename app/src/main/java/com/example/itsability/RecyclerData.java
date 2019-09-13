package com.example.itsability;

//이 class는 RecyclerView에 들어갈 데이터들을 저장하기 위해 만들어진 클래스입니다.
public class RecyclerData {
    private String locationName;
    private String locationAddr;
    private String imageUrl;

    public String getLocationName() {return locationName;}
    public String getLocationAddr() {return locationAddr;}
    public String getImageUrl() {return imageUrl;}

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationAddr(String locationAddr) {
        this.locationAddr = locationAddr;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

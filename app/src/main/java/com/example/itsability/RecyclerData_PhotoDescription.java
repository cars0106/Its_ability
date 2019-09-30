package com.example.itsability;

//이 class는 RecyclerView에 들어갈 데이터들을 저장하기 위해 만들어진 클래스입니다.
public class RecyclerData_PhotoDescription {
    private String photoTitle;
    private String cameraDate;
    private String imageUrl;
    private String hashTag;
    private String paragraph;

    public String getPhotoTitle() {return photoTitle;}
    public String getCameraDate() {return cameraDate;}
    public String getImageUrl() {return imageUrl;}
    public String getHashTag() {return hashTag;}
    public String getParagraph() {return paragraph;}

    public void setPhotoTitle(String photoTitle) {this.photoTitle = photoTitle;}
    public void setCameraDate(String cameraDate) {this.cameraDate = cameraDate;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
    public void setHashTag(String hashTag) {this.hashTag = hashTag;}
    public void setParagraph(String paragraph) {this.paragraph = paragraph;}
}

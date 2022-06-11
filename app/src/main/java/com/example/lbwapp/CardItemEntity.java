package com.example.lbwapp;

import java.io.Serializable;

public class CardItemEntity implements Serializable {//卡片中的对象

    // ***********************v1.0 For test**************************
//    private boolean isUserPhoto=false;//是test还是用户自己加的照片
//    private int image_card=R.drawable.family1;//照片ID
//
//    public int getImage_card() {
//        return image_card;
//    }
//
//    public void setImage_card(int image_card) {
//        this.image_card = image_card;
//    }
//
//    public void setUserPhoto(boolean userPhoto) {
//        isUserPhoto = userPhoto;
//    }
//
//    public boolean isUserPhoto() {
//        return isUserPhoto;
//    }

    // ***********************v2.0 For test**************************
//    private int dayNumber_card=0;//照片距离现在天数
//    private int isdayNumberAlreadyCalculated=0;//当天是否已经计算/更新了这张卡片应该显示的日期
//    public int getDayNumber_card() {
//        return dayNumber_card;
//    }
//
//    public void setDayNumber_card(int dayNumber_card) {
//        this.dayNumber_card = dayNumber_card;
//    }
//
//    public int getIsdayNumberAlreadyCalculated() {
//        return isdayNumberAlreadyCalculated;
//    }
//
//    public void setIsdayNumberAlreadyCalculated(int isdayNumberAlreadyCalculated) {
//        this.isdayNumberAlreadyCalculated = isdayNumberAlreadyCalculated;
//    }

    private String imagePath_card="";//照片绝对路径
    private String title_card="";//照片标题
    private String dateTaken_card="";//照片拍摄时间“yy:mm:dd”
//    private int isDateEditable_card=0;//用户是否可编辑照片创建日期
    private String text_card="";//照片记录
    private String label_card="empty";//照片标签empty
    private String place_card="无";//照片地点【默认：“无”】



    public String getDateTaken_card() {
        return dateTaken_card;
    }

    public void setDateTaken_card(String dateTaken_card) {
        this.dateTaken_card = dateTaken_card;
    }

    public String getImagePath_card() {
        return imagePath_card;
    }

    public void setImagePath_card(String imagePath_card) {
        this.imagePath_card = imagePath_card;
    }

    public String getTitle_card() {
        return title_card;
    }

    public void setTitle_card(String title_card) {
        this.title_card = title_card;
    }

    public String getText_card() {
        return text_card;
    }

    public void setText_card(String text_card) {
        this.text_card = text_card;
    }

    public String getLabel_card() {
        return label_card;
    }

    public void setLabel_card(String label_card) {
        this.label_card = label_card;
    }

    public String getPlace_card() {
        return place_card;
    }

    public void setPlace_card(String label_card) {
        this.place_card = place_card;
    }



    // ***********************For test**************************
//    public CardItemEntity(int image_card, String title_card, int dayNumber_card, String text_card, String label_card, String place_card){
//        this.image_card = image_card;
//        this.title_card = title_card;
//        this.dayNumber_card = dayNumber_card;
//        this.text_card = text_card;
//        this.label_card = label_card;
//        this.place_card = place_card;
//    }

    public CardItemEntity(String imagePath, String title_card, String dateTaken_card, String text_card, String label_card, String place_card){
        this.imagePath_card = imagePath;
        this.title_card = title_card;
        this.dateTaken_card = dateTaken_card;
//        this.isDateEditable_card=isDateEditable_card;
        this.text_card = text_card;
        this.label_card = label_card;
        this.place_card = place_card;
// ***********************For test**************************
//        this.isUserPhoto = true;
//        this.dayNumber_card = dayNumber_card;
    }

    public CardItemEntity(String imagePath, String title_card, String label_card, String dateTaken_card) {
        this.imagePath_card = imagePath;
        this.title_card = title_card;
        this.dateTaken_card = dateTaken_card;
        this.label_card = label_card;
    }

    public CardItemEntity(String imagePath) {
        this.imagePath_card = imagePath;
// ***********************For test**************************
//        this.isUserPhoto = true;
    }

    public CardItemEntity(String imagePath, String title_card, String label_card) {
        this.imagePath_card = imagePath;
        this.title_card = title_card;
        this.label_card = label_card;
    }

    public CardItemEntity(String imagePath, String title_card) {
        this.imagePath_card = imagePath;
        this.title_card = title_card;
    }

    public CardItemEntity() {

    }
}
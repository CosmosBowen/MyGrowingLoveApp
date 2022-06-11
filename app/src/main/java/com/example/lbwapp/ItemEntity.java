package com.example.lbwapp;


import android.media.Image;
import android.widget.ImageView;

import java.io.Serializable;


public class ItemEntity implements Serializable {
    public String content;
    public int img;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public ItemEntity(String content, int img) {
        this.content = content;
        this.img = img;
    }

    public ItemEntity() {

    }
}

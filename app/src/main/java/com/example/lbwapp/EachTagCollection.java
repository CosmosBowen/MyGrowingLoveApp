package com.example.lbwapp;

import java.util.List;

public class EachTagCollection {


    //Map<String,List<CardItemEntity>> tagGroup=new HashMap<>();
    private String eachTagName;
    private List<CardItemEntity> eachTagList;

    public EachTagCollection(String eachTagName,List<CardItemEntity> eachTagList){
        this.eachTagName=eachTagName;
        this.eachTagList=eachTagList;
    }

    public String getEachTagName() {
        return eachTagName;
    }

    public void setEachTagName(String eachTagName) {
        this.eachTagName = eachTagName;
    }

    public List<CardItemEntity> getEachTagList() {
        return eachTagList;
    }

    public void setEachTagList(List<CardItemEntity> eachTagList) {
        this.eachTagList = eachTagList;
    }

}

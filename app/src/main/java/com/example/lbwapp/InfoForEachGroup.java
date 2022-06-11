package com.example.lbwapp;

import java.util.List;

public class InfoForEachGroup {

    private String eachTagName;
    private String eachTagFirstImagePath;
    private int eachTagCount;

    public InfoForEachGroup(String eachTagName, String eachTagFirstImagePath, int eachTagCount){
        this.eachTagName=eachTagName;
        this.eachTagFirstImagePath=eachTagFirstImagePath;
        this.eachTagCount=eachTagCount;
    }

    public String getEachTagName() {
        return eachTagName;
    }

    public void setEachTagName(String eachTagName) {
        this.eachTagName = eachTagName;
    }

    public String getEachTagFirstImagePath() {
        return eachTagFirstImagePath;
    }

    public void setEachTagFirstImagePath(String eachTagFirstImagePath) {
        this.eachTagFirstImagePath = eachTagFirstImagePath;
    }

    public int getEachTagCount() {
        return eachTagCount;
    }

    public void setEachTagCount(int eachTagCount) {
        this.eachTagCount = eachTagCount;
    }



}

package com.example.lbwapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Countries {
    private List<String[]> AllInfo_CodeLocationName =new ArrayList<>();
    private HashMap<String,String> Name2Code=new HashMap<>();

    /*
    public boolean isCorrectCountry(String countryName){
//        boolean checkCorrectCountry=Name2Code.containsKey(countryName);
        boolean checkCorrectCountry=false;
        for(String[] eachInfo:AllInfo_CodeLocationName){
            for(int i=2;i<eachInfo.length;i++){
                if(eachInfo[i].equals(countryName)){
                    return true;
                }
            }
        }
        return checkCorrectCountry;
    }

     */
    public Countries(){
        generateBasicInfo();
    }

    private void generateBasicInfo(){
        //0:code
        //1:latitude&longitide
        //2（英文版本）,3（中文版本）,4...:possible country name
        //支持国家名称拼写错误，并始终只显示中文的文本，包括国家名、省名、具体地区名。

        AllInfo_CodeLocationName.add(new String[]{"CH", "35.86166,104.195397", "China", "中国","CHINA","ChiNa"});
        AllInfo_CodeLocationName.add(new String[]{"US", "37.09024,-95.712891", "United States", "美国","UNITED STATES","UNITEDSTATES","united states","unitedStates","unitedstates"});
        AllInfo_CodeLocationName.add(new String[]{"AU", "-25.274398,133.775136", "Australia", "澳大利亚"});
        AllInfo_CodeLocationName.add(new String[]{"BG", "42.733883,25.48583", "Bulgaria", "保加利亚"});
        AllInfo_CodeLocationName.add(new String[]{"BR", "-14.235004,-51.92528", "Brazil", "巴西"});
        AllInfo_CodeLocationName.add(new String[]{"CA", "56.130366,-106.346771", "Canada", "CA"});
        AllInfo_CodeLocationName.add(new String[]{"PH", "12.879721,121.774017", "Philippines", "菲律宾"});
        AllInfo_CodeLocationName.add(new String[]{"DE", "51.165691,10.451526", "Germany", "德国"});
        AllInfo_CodeLocationName.add(new String[]{"CU", "21.521757,-77.781167", "Cuba", "古巴"});
        AllInfo_CodeLocationName.add(new String[]{"EG", "26.820553,30.802498", "Egypt", "埃及"});
        AllInfo_CodeLocationName.add(new String[]{"ES", "40.463667,-3.74922", "Spain", "西班牙"});
        AllInfo_CodeLocationName.add(new String[]{"FI", "61.92411,25.748151", "Finland", "芬兰"});
        AllInfo_CodeLocationName.add(new String[]{"GB", "55.378051,-3.435973", "United Kingdom", "英国"});
        AllInfo_CodeLocationName.add(new String[]{"GR", "39.074208,21.824312", "Greece", "希腊"});
        AllInfo_CodeLocationName.add(new String[]{"IN", "20.593684,78.96288", "India", "印度"});
    }

    public String getCorrectCountryOrNull(String countryName){
//        boolean checkCorrectCountry=Name2Code.containsKey(countryName);
        String correctCountry=" ";
        for(String[] eachInfo:AllInfo_CodeLocationName){
            for(int i=2;i<eachInfo.length;i++){
                if(eachInfo[i].equals(countryName)){
                    correctCountry=eachInfo[3];//中文版本
//                    correctCountry=eachInfo[2];//英文版本
                    return correctCountry;
                }
            }
        }
        return correctCountry;
    }

    public String getCode(String countryName){
        //中文版本
        String code=" ";
        for(String[] eachInfo:AllInfo_CodeLocationName){
            for(int i=2;i<eachInfo.length;i++){
                if(eachInfo[i].equals(countryName)){
                    code=eachInfo[0];//code
                    return code;
                }
            }
        }
        return null;
    }

    public String getlatitudeAndLongitudeFromCountryName(String countryName){
        String latitudeAndLongitude=" ";
        for(String[] eachInfo:AllInfo_CodeLocationName){
            for(int i=2;i<eachInfo.length;i++){
                if(eachInfo[i].equals(countryName)){
                    latitudeAndLongitude=eachInfo[1];//latitude and longitude in String format
                    return latitudeAndLongitude;
                }
            }
        }
        return null;
    }
}

package com.example.lbwapp;

public class MyDeltaDate {
//    private int deltaDays;
//    private int deltaMonths;
//    private int deltaYears;
//
//    private MyDeltaDate(int deltaDays,int deltaMonths,int deltaYears){
//        this.deltaDays=deltaDays;
//        this.deltaMonths=deltaMonths;
//        this.deltaYears=deltaYears;
//    }

    public static String getGoodFormForMonthOrDay(int form){
        if(form<10){
            return "0"+form;
        }
        return String.valueOf(form);
    }
    public static String[] getTimePoints(String curTime){
        int[] date=getIntDateFromString(curTime);
        int year=date[0];
        int month=date[1];
        int day=date[2];

        //oneYearAgo
        String oneYearAgo=(year-1)+":"+(month<10?"0"+month:month)+":"+(day<10?"0"+day:day);
        //oneMonthAgo
        String oneMonthAgo="";
        if(month==1){
            oneMonthAgo=(year-1)+":12";
        }else {
            String formedMonth="";
            if(month-1<10){
                formedMonth="0"+(month-1);
            }else {
                formedMonth=String.valueOf(month-1);
            }
            oneMonthAgo=year+":"+formedMonth;
        }
        oneMonthAgo+=":"+(day<10?"0"+day:day);
        //oneWeekAgo
        String oneWeekAgo="";
        if(day<7){//8-2,7-1
            if(month==1){
                int daysForLastMonth=getDayNumberFromMonthAndYear(12,year-1);
                oneWeekAgo=(year-1)+":12"+":"+(day-6+daysForLastMonth);//day保证两位数
            }else {
                int daysForLastMonth=getDayNumberFromMonthAndYear(month-1,year);
                String formedMonth="";
                if(month-1<10){
                    formedMonth="0"+(month-1);
                }else {
                    formedMonth=String.valueOf(month-1);
                }
                oneWeekAgo=year+":"+formedMonth+":"+(day-6+daysForLastMonth);//day保证两位数
            }
        }else {
            String formedDay="";
            if(day-6<10){
                formedDay="0"+(day-6);
            }else {
                formedDay=String.valueOf(day-6);
            }
            oneWeekAgo=year+":"+(month<10?"0"+month:month)+":"+formedDay;
        }

        return new String[]{oneYearAgo, oneMonthAgo, oneWeekAgo};

    }
    public static boolean isFarerThanOrEqual(String firstDateString, String secondDateString){
        //year month day
        int[] firstDate=getIntDateFromString(firstDateString);
        int[] secondDate=getIntDateFromString(secondDateString);
        //-->year
        if(firstDate[0]<secondDate[0]){
            return true;
        }else if(firstDate[0]>secondDate[0]){
            return false;
        }
        //the same year, -->month
        if(firstDate[1]<secondDate[1]){
            return true;
        }else if(firstDate[1]>secondDate[1]){
            return false;
        }
        //the same year and month, -->day
        if(firstDate[2]<secondDate[2]){
            return true;
        }else if(firstDate[2]>secondDate[2]){
            return false;
        }
        //the same year and month and day
        return true;
    }
    public static String getDaysTextFromDates(String curTime,String takenTime){
        int[] myDeltaDate = calculateMyDeltaDateFromDates(curTime,takenTime);//MyDeltaDate myDeltaDate
        return getDaysTextFromMyDeltaDate(myDeltaDate);
    }

    public static int[] getIntDateFromString(String stringDate){
        int year=Integer.parseInt(stringDate.substring(0,4));
        int month=Integer.parseInt(stringDate.substring(5,7));
        int day=Integer.parseInt(stringDate.substring(8,10));
        int[] date={year,month,day};
        return date;
    }

    public static int[] calculateMyDeltaDateFromDates(String curTime,String takenTime){//return myDeltaDate;
        // String<yy:MM:dd> -> int<yy><MM><dd>
        int[] curTimeDate=getIntDateFromString(curTime);
        int curYear=curTimeDate[0];//Integer.parseInt(curTime.substring(0,4));
        int curMonth=curTimeDate[1];//Integer.parseInt(curTime.substring(5,7));
        int curDay=curTimeDate[2];//Integer.parseInt(curTime.substring(8,10));

        int[] takenTimeDate=getIntDateFromString(takenTime);
        int takenYear=takenTimeDate[0];//Integer.parseInt(takenTime.substring(0,4));
        int takenMonth=takenTimeDate[1];//Integer.parseInt(takenTime.substring(5,7));
        int takenDay=takenTimeDate[2];//Integer.parseInt(takenTime.substring(8,10));

        //calculate deltaY/M/D
        int deltaYears=curYear-takenYear;
        int deltaMonths=curMonth-takenMonth;
        if(deltaMonths<0){
            deltaYears=deltaYears-1;
            deltaMonths=deltaMonths+12;
        }
        int deltaDays=curDay-takenDay;
        if(deltaDays<0){
            deltaMonths=deltaMonths-1;
            if(deltaMonths<0){
                deltaYears=deltaYears-1;
                deltaMonths=deltaMonths+12;
            }
            deltaDays=deltaDays+getDayNumberFromMonthAndYear(takenMonth,takenYear);
        }
        int[] myDeltaDate={deltaYears,deltaMonths,deltaDays};
//        MyDeltaDate myDeltaDate=new MyDeltaDate(deltaDays,deltaMonths,deltaYears);
        return myDeltaDate;
    }

    private static int getDayNumberFromMonthAndYear(int month, int year){
        int dayNumber=0;
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:
                dayNumber=30;
                break;
            case 2:
                if(year%4==0){
                    dayNumber=29;
                }else {
                    dayNumber=28;
                }
                break;
            default:
                dayNumber=31;//1,3,5,7,8,10,12
                break;
        }
        return dayNumber;
    }

    private static String getDaysTextFromMyDeltaDate(int[] myDeltaDate){
        int deltaYears=myDeltaDate[0];
        int deltaMonths=myDeltaDate[1];
        int deltaDays=myDeltaDate[2];

        String showText="";
        // Chinese version
        if(deltaYears>0){
            showText=deltaYears+"年前";
        }else if (deltaMonths>0){
            showText=deltaMonths+"个月前";
        }else if(deltaDays>0){
            showText=deltaDays+"天前";
        }else {
            showText="今天";
        }
        return showText;
//        return showText+"\n间隔:"+"\n(年) "+deltaYears+"\n(月) "+deltaMonths+"\n(日) "+deltaDays;

        // English version,以前是需要myDeltaDate.deltaYears，myDeltaDate.deltaMonths，myDeltaDate.deltaDays
//        if(deltaYears>0){
//            if(deltaYears>1){
//                showText=deltaYears+" years ago";
//            }else {
//                showText=deltaYears+" year ago";
//            }
//        }else if (deltaMonths>0){
//            if(deltaMonths>1){
//                showText=deltaMonths+" months ago";
//            }else {
//                showText=deltaMonths+" month ago";
//            }
//        }else if(deltaDays>0){
//            if(deltaDays>1){
//                showText=deltaDays+" days ago";
//            }else {
//                showText=deltaDays+" day ago";
//            }
//        }else {
//            showText="Today";
//        }
//        return showText+"\nin between:"+"\n(years)  "+deltaYears+"\n(months) "+deltaMonths+"\n(days)   "+deltaDays;

    }
}

package com.example.lbwapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class calendarTest extends Activity {
    private Activity mActivityContext=this;
    private CalendarView calendarView;
    private LinearLayout linearLayout;
    MyTextView dayNumber_card;
    int[] curDate;
//    private com.haibin.calendarview.CalendarLayout calendarLayout;
//    private com.haibin.calendarview.CalendarView calendarView;
    private Button btn_expand;
    private Button btn_shrink;
    private TextView tv_getDate;
    private CheckBox checkBox;
    private String curTime="";//2022:03:28
    long selectedDate=0;//(new CalendarView(mActivityContext)).getDate();
    String dateTaken="0000:00:00";//照片创建时间
    private TextView textShow;
    int mYear=0;
    int mMonth=0;
    int mDayOfMonth=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_test);

        curTime = new SimpleDateFormat("yyyy:MM:dd").format(Calendar.getInstance().getTime());
        textShow=findViewById(R.id.testShow);
        textShow.setText("选择的日期间隔：\n"+selectedDate);

        if(curDate==null){
            curDate=MyDeltaDate.getIntDateFromString(curTime);
//            Toast.makeText(calendarTest.this,"新的curTime",Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(calendarTest.this,"已经计算了curTime",Toast.LENGTH_SHORT).show();
        }

        tv_getDate=findViewById(R.id.tv_getDate);
        tv_getDate.setText("今天的日期："+curTime);
        checkBox=findViewById(R.id.checkBox);
        dayNumber_card=findViewById(R.id.dayNumber_card);
        linearLayout=findViewById(R.id.calendarLayout);
        btn_expand=findViewById(R.id.btn_expand);
        btn_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    Toast.makeText(calendarTest.this,"已勾选 ✔ ",Toast.LENGTH_SHORT).show();
                    if(linearLayout.indexOfChild(calendarView)==-1){
                        calendarView=new CalendarView(mActivityContext);
//                        selectedDate=calendarView.getDate();
                        CalendarView.LayoutParams layoutParams=new CalendarView.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        //year(-1900) month(0-11) date(1-31)
//                    Date maxDate=new Date(curDate[0]-1900,curDate[1],curDate[2]);
//                    calendarView.setMaxDate(maxDate.getTime());
                        calendarView.setMaxDate(calendarView.getDate());
//                        int[] date=MyDeltaDate.getIntDateFromString(dateTaken);
//                        int selectedYear=date[0];
//                        int selectedMonth=date[1];
//                        int selectedDay=date[2];
//                        Calendar selectedCalendar=Calendar.getInstance();
//                        selectedCalendar.set(Calendar.YEAR,selectedYear);
//                        selectedCalendar.set(Calendar.MONTH,selectedMonth);
//                        selectedCalendar.set(Calendar.DAY_OF_MONTH,selectedDay);
//                        selectedDate=selectedCalendar.getTimeInMillis();

                        calendarView.setDate(selectedDate);

                        calendarView.setLayoutParams(layoutParams);
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                            String msg="选择了："+year+"年/"+(month+1)+"月/"+dayOfMonth+"日";
//                            Toast.makeText(calendarTest.this,msg,Toast.LENGTH_SHORT).show();
                                month=month+1;
                                String monthString=String.valueOf(month);
                                String dayString=String.valueOf(dayOfMonth);
                                if(month<10){
                                    monthString="0"+month;
                                }
                                if(dayOfMonth<10){
                                    dayString="0"+dayOfMonth;
                                }
                                dateTaken=year+":"+monthString+":"+dayString;
                                String daysText="";
                                daysText=MyDeltaDate.getDaysTextFromDates(curTime,dateTaken);

                                mYear=year;
                                mMonth=month;
                                mDayOfMonth=dayOfMonth;
//                                int[] date=MyDeltaDate.getIntDateFromString(dateTaken);
//                                Calendar selectedCalendar=Calendar.getInstance();
//                                selectedCalendar.set(Calendar.YEAR,year);
//                                selectedCalendar.set(Calendar.MONTH,month+1);
//                                selectedCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                                selectedDate=selectedCalendar.getTimeInMillis();
//
//                                textShow.setText("选择的日期间隔：\n"+selectedDate);
                                dayNumber_card.setText("📅 "+daysText);//daysText/takenTime
                            }
                        });
                        linearLayout.addView(calendarView);
                    }
                }else {
                    Toast.makeText(calendarTest.this,"未勾选 ❌",Toast.LENGTH_SHORT).show();
                }


            }
        });
//
        btn_shrink=findViewById(R.id.btn_shink);
        btn_shrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar selectedCalendar=Calendar.getInstance();
                selectedCalendar.set(Calendar.YEAR,mYear);
                selectedCalendar.set(Calendar.MONTH,mMonth+1);
                selectedCalendar.set(Calendar.DAY_OF_MONTH,mDayOfMonth);
                selectedDate=selectedCalendar.getTimeInMillis();

                textShow.setText("选择的日期间隔：\n"+selectedDate);
                linearLayout.removeAllViews();
                checkBox.setChecked(false);
            }
        });

//        calendarView=findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String msg="选择了："+year+"年/"+month+"月/"+dayOfMonth+"日";
//                Toast.makeText(calendarTest.this,msg,Toast.LENGTH_SHORT).show();
//            }
//        });

//        calendarLayout=findViewById(R.id.calendarLayout);
//        calendarView=findViewById(R.id.calendarView);
//        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Calendar calendar, boolean isClick) {
//                String msg="选择了："+calendar.getYear()+"年/"+calendar.getMonth()+"月/"+calendar.getDay()+"日";
//                Toast.makeText(calendarTest.this,msg,Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
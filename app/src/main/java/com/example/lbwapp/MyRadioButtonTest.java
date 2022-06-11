package com.example.lbwapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyRadioButtonTest extends AppCompatActivity {

//    Map<Integer, Integer> objects=new HashMap<Integer, Integer>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_radio_button_test);
        Log.d("MyRadioButtonTest","onCreate");

        LinearLayout ll_container=(LinearLayout)findViewById(R.id.ll_container);
        final MyRadioGroup rg=new MyRadioGroup(this);
//        int selectedLabelResId=R.string.travel;
//        String selectedLabelText=getString(selectedLabelResId);
        String selectedLabelText="探亲";
//        rg.setHighlightButton(selectedLabelText);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyRadioButtonTest","onClickListener");
            }
        });

        MyRadioGroup.LayoutParams rg_layoutParams=new MyRadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ll_container.addView(rg,rg_layoutParams);


//        objects.put(R.string.travel,R.color.travel);
//        objects.put(R.string.home,R.color.home);
//        objects.put(R.string.visit,R.color.visit);
//        objects.put(R.string.aboard,R.color.aboard);


//        for(Map.Entry<Integer,Integer> entry: objects.entrySet()){
//            int textId=entry.getKey();
//            int colorId=entry.getValue();
//            MyRadioButton button=new MyRadioButton(this);
//            MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            rb_LayoutParams.setMargins(20,20,20,20);
//            button.setLayoutParams(rb_LayoutParams);
//            button.setMyTextView(getString(textId));
//            button.setImageView(getColor(colorId));
//            rg.addView(button);
//        }



//        MyRadioButton btn1=new MyRadioButton(this);
//        MyRadioButton btn2=new MyRadioButton(this);
//        MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        rb_LayoutParams.setMargins(20,20,20,20);
//        btn1.setLayoutParams(rb_LayoutParams);
//        btn2.setLayoutParams(rb_LayoutParams);
//
//        btn1.setMyTextView(getString(R.string.travel));
//        btn1.setImageView(getColor(R.color.travel));
//
//        rg.addView(btn1);
//        rg.addView(btn2);



//        rg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("MyRadioGroupTest","changeChildrenSelectedState");
//                if(rg.btnList.size()!=0){
//                    for(MyRadioButton button: rg.btnList){
//                        if(button!=rg.selectedBtn){
//                            Log.d("MyRadioGroupTest",button.getMyTextView());
//                            button.setMyDefaultAlhpa();
//                        }
//                    }
//                }
////                rg.changeChildrenSelectedState();
//            }
//        });

//        CalendarView.LayoutParams layoutParams=new CalendarView.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        layoutParams.setMargins(200,200,0,0);
//        RadioGroup radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
//        View myRadioButton1=getLayoutInflater().inflate(R.layout.my_radio_button,null);
//        View myRadioButton2=getLayoutInflater().inflate(R.layout.my_radio_button,null);
//        myRadioButton1.setLayoutParams(layoutParams);
//        myRadioButton2.setLayoutParams(layoutParams);
//        radioGroup.addView(myRadioButton1);
//        radioGroup.addView(myRadioButton2);

    }
//    public void onRadioButtonClicked(View view){
//        String showButtonName="null";
//        boolean checked=((MyRadioButton) view).isChecked();
//        switch (view.getId()){
//            case R.id.radioButton_home:
//                if(checked){
//                    showButtonName="home";
//                    RadioButton rb=(RadioButton) findViewById(view.getId());
//                    rb.setAlpha(1);
//                }
//                break;
//            case R.id.radioButton_travel:
//                if(checked){
//                    showButtonName="travel";
//                }
//                break;
//            case R.id.radioButton_visit:
//                if(checked){
//                    showButtonName="visit";
//                }
//                break;
//            case R.id.radioButton_aboard:
//                if(checked){
//                    showButtonName="abroad";
//                }
//                break;
//        }
//        Toast.makeText(MyRadioButtonTest.this,"click: "+showButtonName,Toast.LENGTH_SHORT).show();
//    }
}
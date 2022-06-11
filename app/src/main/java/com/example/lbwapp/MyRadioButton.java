package com.example.lbwapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

public class MyRadioButton extends LinearLayout{

    private boolean isRealLabel = true;
    private boolean isFunctionalLabel = false;
//    boolean isSelected=false;
//    @StyleableRes
//    int index0=0;//imageView
//    @StyleableRes
//    int index1=1;//myTextView
//    @StyleableRes
//    int index2=2;//cardView
    ImageView imageView;
    MyTextView myTextView;
    CardView cardView;
    TextView border;
//    RadioButton radioButton;
    ObjectAnimator objectAnimator;
    ObjectAnimator objectAnimatorBorder;
    //set dynamically
    public MyRadioButton(Context context){//isRealLabel默认为true, isFunctionalLabel默认为false
        super(context);
        init(context);
    }
    public MyRadioButton(Context context, boolean isRealLabel, boolean isFunctionalLabel){
        super(context);
        this.isRealLabel = isRealLabel;
        this.isFunctionalLabel=isFunctionalLabel;
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.my_radio_button,this);
        initComponents();
    }

    private void initComponents(){
        imageView=(ImageView) findViewById(R.id.imageView);
        myTextView=(MyTextView) findViewById(R.id.myTextView);
        cardView=(CardView)findViewById(R.id.cardView);
        border=(TextView)findViewById(R.id.border);
//        radioButton=(RadioButton)findViewById(R.id.radioButton);

        if(isRealLabel){
            setMyDefaultRealBtnAlhpa();
            objectAnimator=ObjectAnimator.ofFloat(cardView,"Alpha",1);
            objectAnimator.setDuration(400);
            objectAnimatorBorder=ObjectAnimator.ofFloat(border,"Alpha",1);
            objectAnimatorBorder.setDuration(400);
        }else {
            setMyDefaultNonRealBtnAlhpa();
        }
    }

    public void startMyAlphaAnimation(){
        //只有RealLabel有黑边框border“渐渐出现”的动画，和“背景渐渐出现”的动画，“+”两个功能都没有的！

        //********************cardView渐变*******************
//        objectAnimator.start();
        objectAnimatorBorder.start();
//        if(isRealLabel){
//            objectAnimator.start();
//            objectAnimatorBorder.start();
//        }
    }

    public float getMyAlpha(){
        return cardView.getAlpha();
    }
    public void setMyAlpha(float alpha){
        cardView.setAlpha(alpha);
    }

    public void setMyDefaultRealBtnAlhpa(){
        //********************cardView渐变*******************
//        cardView.setAlpha(0.3f);
        border.setAlpha(0f);
    }
    public void setMyDefaultNonRealBtnAlhpa(){
        cardView.setAlpha(1);
        border.setAlpha(0f);
    }



    public int getImageView(){
        ColorDrawable colorDrawable=(ColorDrawable) imageView.getBackground();
        return colorDrawable.getColor();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setImageView(int value){
        imageView.setBackgroundColor(value);
//        GradientDrawable borderColor=(GradientDrawable) border.getBackground().mutate();
//        borderColor.setStroke(3,Color.BLUE);
//        borderColor.setStroke(3,Color.parseColor(String.valueOf(value)));
//        imageView.setBackground(new ColorDrawable(value));
    }

    public String getMyTextView(){
        return myTextView.getText().toString();
    }
    public void setMyTextView(String value){
        myTextView.setText(value);
    }

    public boolean isRealLabel(){
        return isRealLabel;
    }

    public boolean isFunctionalLabel() {
        return isFunctionalLabel;
    }


    //set from xml
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public MyRadioButton(Context context, AttributeSet attrs){
//        super(context, attrs);
//        init(context, attrs);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void init(Context context, AttributeSet attrs){
//        Toast.makeText(context,"button long",Toast.LENGTH_SHORT).show();
//        inflate(context, R.layout.my_radio_button,this);
////        inflate(context, R.layout.my_radio_button,new LinearLayout(getContext()));
//
////        inflate(context, R.layout.my_radio_button,null,false);
////        LayoutInflater.from(context).inflate(R.layout.my_radio_button,null,false);
//        //for set attributes in <page layout>.xml
//        int[] sets={R.attr.imageView, R.attr.myTextView};
//        TypedArray typedArray=context.obtainStyledAttributes(attrs, sets);
//        int color=typedArray.getColor(index0,Color.BLACK);
//        CharSequence myTextViewValue=typedArray.getText(index1);
//        typedArray.recycle();
//
//        initComponents();
//
//        setImageView(color);
//        setMyTextView(myTextViewValue);
//
//    }


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public int getImageView(){
//        ColorDrawable colorDrawable=(ColorDrawable) imageView.getBackground();
//        return colorDrawable.getColor();
//    }
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void setImageView(int value){
//        imageView.setBackground(new ColorDrawable(value));
//    }
//
//    public CharSequence getMyTextView(){
//        return myTextView.getText();
//    }
//    public void setMyTextView(CharSequence value){
//        myTextView.setText(value);
//    }


//    public boolean isChecked() {
//        return isChecked;
//    }
//
//    public void setIsChecked(){
//        isChecked=true;
//    }
}


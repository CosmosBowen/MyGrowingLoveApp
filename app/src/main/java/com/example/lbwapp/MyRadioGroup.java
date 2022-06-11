package com.example.lbwapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyRadioGroup extends LinearLayout {


    //原来的btnMap变成<String, String>,因为颜色需要存储成“String”的类型，而不再是“resId”的int类型啦！
//    Map<String, Integer> btnMap=new LinkedHashMap<>();
//    public Map<String,String> storedLabels=new LinkedHashMap<>();//相当于从数据库里面拿数据
    public List<MyRadioButton> btnList=new ArrayList<>();

//    public String selectedText="";

//    MyRadioButton selectedBtn=null;

//    //learned by CalendarView
//
//    /**
//     * Sets the listener to be notified upon selected button change.
//     *
//     * @param listener The listener to be notified.
//     */
//    public void setOnButtonChangeListener(MyRadioGroup.OnButtonChangeListener listener) {
//    }
//
//    /**
//     * The callback used to indicate the user changes the button.
//     */
//    public interface OnButtonChangeListener {
//
//        /**
//         * Called upon change of the selected button.
//         *
//         * @param view The view associated with this listener.
//         * @param selectedText The button that was set.
//         */
//        void onSelectedButtonChange(MyRadioGroup view, String selectedText);
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyRadioGroup(Context context) {
        super(context);
        setOrientation(VERTICAL);
        //放真正的数据
//        getStoredLabels();
//        init();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void init(){
//
////        fillBtnList();
//        //放真正数据的标签按钮
//        putRealBtns();
//        //放功能按钮
//        putFunctionalBtns();
//        //设置孩子的点击事件
//        setChildOnClickListener();
//    }
//    public MyRadioGroup(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setOrientation(VERTICAL);
////        Toast.makeText(context,"group long",Toast.LENGTH_SHORT).show();
//    }

//    public void setHighlightButton(String labelText){
//        selectedText=labelText;
//        for(MyRadioButton button: btnList){
//            if(button.getMyTextView()==labelText){
//                Log.d("labelTest","setHighlightButton: "+labelText);
//                button.startMyAlphaAnimation();
////                button.setMyAlpha(1);
////                selectedBtn=button;
//            }
//        }
//    }

//    private void getStoredLabels(){
//        //从数据库里拿数据<text:String, color:String>
//        //其实只用存“真正的数据”，不需要“无标记”和“+”
//        storedLabels.put("travel","#F892B5");
//        storedLabels.put("home","#7BD1F8");
//        storedLabels.put("visit","#B3E47A");
//        storedLabels.put("abroad","#FFEB3F");
////        storedLabels.put("旅行","#F892B5");
////        storedLabels.put("家庭日记","#7BD1F8");
////        storedLabels.put("探亲","#B3E47A");
////        storedLabels.put("出国","#FFEB3F");
//
////        storedLabels.put("无标记","#ABACAC");
////        storedLabels.put("+","#FB8C00");
//
//        //以前放数据，是这样：getString()把resId转换成String(资源里真正的值)
////        btnMap.put(getContext().getString(R.string.travel),R.color.travel);
//
//
//        //测试hashMap放进去后各个东西的顺序，放进去后，就不变啦，就是显示的按钮（从上到下）的顺序
//        //put->get key/value的顺序->组成btnList的顺序->btn显示的顺序
////        for(Map.Entry<String,Integer> entry: btnMap.entrySet()){
////            String text=entry.getKey();
////            Log.d("MyRadioGroupTest","fillBtnList_1 "+text);
////        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void myGroupAddBtn(String text, String color, boolean isRealLabel, boolean isFunctionalLabel){
        final MyRadioButton button=new MyRadioButton(getContext(),isRealLabel,isFunctionalLabel);
        MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rb_LayoutParams.setMargins(20,20,20,20);
        button.setLayoutParams(rb_LayoutParams);
        button.setMyTextView(text);
        button.setImageView(Color.parseColor(color));
        addView(button);
    }
    //overloading, set "isRealLabel=true" by default
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void myGroupAddBtn(String text, String color){
        myGroupAddBtn(text, color,true,false);
    }


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void fillBtnList(){
//        for(Map.Entry<String,String> entry: storedLabels.entrySet()){
//            String text=entry.getKey();
//            String color=entry.getValue();
//            MyRadioButton button=new MyRadioButton(getContext());
//            MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            rb_LayoutParams.setMargins(20,20,20,20);
//            button.setLayoutParams(rb_LayoutParams);
//            button.setMyTextView(text);
//            button.setImageView(Color.parseColor(color));
//            addView(button);
////            Log.d("MyRadioGroupTest","fillBtnList_2 "+text);
//        }
////        for(Map.Entry<String,Integer> entry: btnMap.entrySet()){
////            String text=entry.getKey();
////            int colorId=entry.getValue();
////            MyRadioButton button=new MyRadioButton(getContext());
////            MyRadioButton.LayoutParams rb_LayoutParams=new MyRadioButton.LayoutParams(
////                    ViewGroup.LayoutParams.WRAP_CONTENT,
////                    ViewGroup.LayoutParams.WRAP_CONTENT
////            );
////            rb_LayoutParams.setMargins(20,20,20,20);
////            button.setLayoutParams(rb_LayoutParams);
////            button.setMyTextView(text);
////            button.setImageView(getContext().getColor(colorId));
////            addView(button);
//////            Log.d("MyRadioGroupTest","fillBtnList_2 "+text);
////        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void putRealBtns() {
//        for (Map.Entry<String, String> entry : storedLabels.entrySet()) {
//            String text = entry.getKey();
//            String color = entry.getValue();
////            myGroupAddBtn(text, color);
//            myGroupAddBtn(text, color,true,false);
//
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putFunctionalBtns(){
        myGroupAddBtn("empty","#ABACAC",true,true);
        myGroupAddBtn("+","#FB8C00",false,true);
    }

    @Override
    public void addView(final View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof MyRadioButton) {
            final MyRadioButton button=(MyRadioButton)child;
            btnList.add(button);
        }
        super.addView(child, index, params);
    }

//    public OnChildClickListener onChildClickListener;
//
//    public interface OnChildClickListener{
//        public void setChildClicklistener(final View child);
//    }
//
//    public void setOnChildClickListener(OnChildClickListener onChildClickListener){
//        this.onChildClickListener=onChildClickListener;
//    }

    @Override
    public void removeAllViews(){
        btnList.clear();
        super.removeAllViews();
    }


    //    setChildOnClickListener
//    public void setChildOnClickListener(){
//        for(final MyRadioButton button:btnList){
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.O)
//                @Override
//                public void onClick(View v) {
//
//                    if(button.isRealLabel()){
//                        button.startMyAlphaAnimation();
//                        selectedText=button.getMyTextView();
//                        Toast.makeText(getContext(),"click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
//                        //其他隐身(好好琢磨)
//                        for(MyRadioButton iterateBtn: btnList){
//                            if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
//                                iterateBtn.setMyDefaultRealBtnAlhpa();
//                            }
//                        }
//                    }else {
//                        //+新的标签
//                        Toast.makeText(getContext(),"+ 新的标签",Toast.LENGTH_SHORT).show();
//                        //设想：加成功
//                        //增加标签
//                        String newText="动物";
//                        String newColor="#6200EE";
//                        storedLabels.put(newText,newColor);
//                        removeAllViews();
//                        //myGroupAddBtn(text, color,true,false);
////                        putRealBtns();
////                        putFunctionalBtns();
//                        init();
//                        setHighlightButton(newText);
//                        //selectedText="动物";
//                    }
//                    Log.d("labelTest","short click 选择："+selectedText);
//                }
//            });
//            button.setOnLongClickListener(new View.OnLongClickListener(){
//                @Override
//                public boolean onLongClick(View v) {
//                    //Toast.makeText(getContext(),"长click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
//                    if(button.isFunctionalLabel()){
//                        //do nothing
//                        Toast.makeText(getContext(),"do nothing",Toast.LENGTH_SHORT).show();
//                    }else {
//                        //是否删除、删除
//                        final String deletedText=button.getMyTextView();
//                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getContext());
//                        alertBuilder.setMessage("是否要删除： \""+deletedText+"\" ?");
//                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @RequiresApi(api = Build.VERSION_CODES.O)
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //删除标签
//                                storedLabels.remove(deletedText);
//                                //btnMap.remove(deletedText);
//                                removeAllViews();
//                                //fillBtnList();
////                                putRealBtns();
////                                putFunctionalBtns();
//                                init();
//                                Log.d("MyRadioGroupTest","刚刚<删掉>，重新恢复putRealBtns+putFunctionalBtns");
//                                if(deletedText==selectedText){
//                                    setHighlightButton("empty");
//                                }else {
//                                    setHighlightButton(selectedText);
//                                }
//
//                            }
//                        }).setNegativeButton("No", null);
//                        final AlertDialog alertDialog=alertBuilder.create();
//                        alertDialog.show();
//                    }
//                    Log.d("labelTest","long click 选择："+selectedText);
//                    return true;
//
//
//                }
//            });
//        }
//    }


//    public void changeChildrenSelectedState(){
//        Log.d("MyRadioGroupTest","changeChildrenSelectedState");
//        if(btnList.size()!=0){
//            for(MyRadioButton button: btnList){
//                if(button!=selectedBtn){
//                    Log.d("MyRadioGroupTest",button.getMyTextView());
//                    button.setMyDefaultAlhpa();
//                }
//            }
//        }
//
//    }

//    @Override
//    public void addView(View child, int index, ViewGroup.LayoutParams params) {
////        if (child instanceof MyRadioButton) {
////            child.setOnClickListener(view -> {
////                MyRadioButton selectedButton = (MyRadioButton) child;
////
////                //setAllButtonsToUnselectedState();
////                //setSelectedButtonToSelectedState(selectedButton);
////                initOnClickListener(selectedButton);
////            });
////        }
//
//        super.addView(child, index, params);
//    }

//    @Override
//    private void setCheckedStateForView(int viewId, boolean checked) {
//        View checkedView = findViewById(viewId);
//        if (checkedView != null && checkedView instanceof MyRadioButton) {
//            ((MyRadioButton) checkedView).setChecked(checked);
//        }
//    }

//    private void initOnClickListener(MyRadioButton selectedButton){
//
//        selectedButton.setIsChecked();
//
//    }
}














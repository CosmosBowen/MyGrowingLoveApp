package com.example.lbwapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class labelTest extends Activity {

    private String[] myColorCollection =
            {"#F892B5","#7BD1F8","#B3E47A","#FFEB3F","#7581BF","#C4A0CA","#26A69A","#E66F7C","#D4E157","#FAC374"};

    private String getNewColorFromCollection(){
        Collection<String> usedColors=storedLabels.values();
        for(String colorInCollection:myColorCollection){
            if(usedColors.contains(colorInCollection)==false){
                return colorInCollection;
            }
        }
        //否则超过规定的10个标签数目
        Toast.makeText(mActivityContext,"标签不允许超过10个",Toast.LENGTH_SHORT).show();
        return "";
    }

    private Button btn_back;

    //数据库
    private MyDatabaseHelper dbHelper;
    public Map<String,String> storedLabels=new LinkedHashMap<>();//相当于从数据库里面拿数据
    private boolean isGetLabelLibrary=false;

    private Activity mActivityContext=this;
    private MyRadioGroup rg;

    //标签：
    private CardView label;
    private ImageView label_color_card;
    private MyTextView label_text_card;

    // 动态生成动态生成CalendarView(子组件)->LinearLayout/CardView(父组件)
    private CardView labelCardViewContainer;
//    private CalendarView calendarView2;//private LinearLayout labelOptions;
//    private RadioGroup radioGroup;

    boolean isLabelEdited=false;
    boolean isStoredLabelsEdited=false;
    boolean isLabelOpen=false;//用来表示“标签”是否已经打开或关上
    String labelChosen="empty";//照片标签
    private String selectedText="empty";//选择的标签
    //
//    RadioGroup radioGroup;
//    RadioButton rb_visit;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(){
        if(rg!=null){
            //放真正数据的标签按钮
            putRealBtns();
            //放功能按钮
            rg.putFunctionalBtns();
            //设置孩子的点击事件
            setChildOnClickListener();
        }else {
            Log.d("labelTest","ERROR: null rg");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void putRealBtns() {
        for (Map.Entry<String, String> entry : storedLabels.entrySet()) {
            String text = entry.getKey();
            String color = entry.getValue();
//            myGroupAddBtn(text, color);
            rg.myGroupAddBtn(text, color,true,false);

        }
    }



    private void getStoredLabels(){

//        storedLabels.put("travel","#F892B5");
//        storedLabels.put("home","#7BD1F8");
//        storedLabels.put("visit","#B3E47A");
//        storedLabels.put("abroad","#FFEB3F");
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(
                FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.moveToFirst()){
            do{
                String text = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT));
                String color = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR));
                storedLabels.put(text,color);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setHighlightButton(String labelText){
        selectedText=labelText;
        setLabel(selectedText);
        for(MyRadioButton button: rg.btnList){
            String buttonText=button.getMyTextView();
            if(buttonText.equals(labelText)){//buttonText==labelText 行不通，神奇噢【可以好好想想】
                Log.d("labelTest","setHighlightButton: "+labelText);
                button.startMyAlphaAnimation();
                return;
//                button.setMyAlpha(1);
//                selectedBtn=button;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setLabel(String label){
        Log.d("labelTest","setLabel!!!!!!!!!! "+label);
        label_text_card.setText(label);
        if(label.equals("empty")){//getString(R.string.empty)
            label_color_card.setBackgroundColor(this.getColor(R.color.empty));
        }else {
            String color=storedLabels.get(label);
            label_color_card.setBackgroundColor(Color.parseColor(color));
        }

        //理想的做法
        //label_card.setText("标签："+item.getLabel_card());
        //照片标签 item.getLabel_card()
//        switch (label){
//            case "travel":
//                label_text_card.setText("travel");//用CardItem类的label属性设置标签的文字和背景色
//                label_color_card.setBackgroundColor(this.getColor(R.color.travel));
//                break;
//            case "abroad":
//                label_text_card.setText("abroad");
//                label_color_card.setBackgroundColor(this.getColor(R.color.aboard));
//                break;
//            case "visit":
//                label_text_card.setText("visit");
//                label_color_card.setBackgroundColor(this.getColor(R.color.visit));
//                break;
//            case "home":
//                label_text_card.setText("home");
//                label_color_card.setBackgroundColor(this.getColor(R.color.home));
//                break;
//            case "pets":
//                label_text_card.setText("pets");
//                label_color_card.setBackgroundColor(this.getColor(R.color.pets));
//                break;
//            case "empty":
//                label_text_card.setText("empty");
//                label_color_card.setBackgroundColor(this.getColor(R.color.empty));
//                break;
//            default:
//                break;
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChildOnClickListener(){
        for(final MyRadioButton button:rg.btnList){

            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(button.isRealLabel()){
                        button.startMyAlphaAnimation();
                        selectedText=button.getMyTextView();
                        setLabel(selectedText);
//                        label_text_card.setText(selectedText);
//                        label_color_card.setBackgroundColor(Color.parseColor(storedLabels.get(selectedText)));

                        Toast.makeText(mActivityContext,"click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
                        //其他隐身(好好琢磨)
                        for(MyRadioButton iterateBtn: rg.btnList){
                            if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
                                iterateBtn.setMyDefaultRealBtnAlhpa();
                            }
                        }
                    }else {
                        //添加+新的标签
//                        Toast.makeText(mActivityContext,"+ 新的标签",Toast.LENGTH_SHORT).show();
                        Toast.makeText(mActivityContext,"+ add new label",Toast.LENGTH_SHORT).show();
                        //设想：加成功
                        //增加标签
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(mActivityContext);
                        LayoutInflater inflater=mActivityContext.getLayoutInflater();
                        View dialogView=inflater.inflate(R.layout.create_custom_label,null);
                        alertBuilder.setView(dialogView);
                        final MyEditText customLabelName=(MyEditText)dialogView.findViewById(R.id.customLabelName);
                        alertBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //添加标签
//                                String newText="pets";
//                                String newColor="#6200EE";
                                String newText=customLabelName.getText().toString();
                                if(newText.isEmpty()){
                                    //没填"标签名"，就不能创建新的标签
                                    Toast.makeText(labelTest.this,"name cannot be empty, please enter again",Toast.LENGTH_SHORT);
                                    Log.d("labelTest","name cannot be empty, please enter again");
//                                    return;
                                }else if(storedLabels.containsKey(newText)){
                                    Log.d("labelTest","already have one");
//                                    selectedText=newText;
                                    setHighlightButton(newText);
                                    for(MyRadioButton iterateBtn: rg.btnList){
                                        if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
                                            iterateBtn.setMyDefaultRealBtnAlhpa();
                                        }
                                    }
                                } else {
                                    //成功创建新的标签名
                                    String newColor=getNewColorFromCollection();
                                    if(newColor!=""){
                                        storedLabels.put(newText,newColor);
                                        isStoredLabelsEdited=true;
                                        rg.removeAllViews();
                                        //myGroupAddBtn(text, color,true,false);
                                        // putRealBtns();
                                        // putFunctionalBtns();
                                        init();
                                        setHighlightButton(newText);
//                                        selectedText=newText;
                                        //selectedText=button.getMyTextView();

                                        setLabel(selectedText);
                                        //label_text_card.setText(newText);
                                        // label_color_card.setBackgroundColor(Color.parseColor(newColor));
                                        //selectedText="动物";
                                    }
                                }



                            }
                        }).setNegativeButton("Cancel", null);
                        AlertDialog alertDialog=alertBuilder.create();
                        alertDialog.show();

                    }
                    Log.d("labelTest","short click："+selectedText);
                }
            });

            button.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if(button.isFunctionalLabel()){
                        //do nothing
                        Toast.makeText(mActivityContext,"do nothing",Toast.LENGTH_SHORT).show();
                    }else {
                        //是否删除、删除
                        final String deletedText=button.getMyTextView();
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(mActivityContext);
//                        alertBuilder.setMessage("是否要删除： \""+deletedText+"\" ?");
                        alertBuilder.setMessage("You sure to delete \""+deletedText+"\" ?");
                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除标签
                                //看相片数据库里有没有用这个label作为标签的，有的话，就不能删掉；没有的话，才能删

                                List<String> labelsBeingUsed = new ArrayList<>();
//                                labelsBeingUsed.add("travel");
                                SQLiteDatabase db=dbHelper.getReadableDatabase();
                                Cursor cursor=db.query(
                                        FeedReaderContract.FeedEntry.TABLE_NAME,
                                        new String[]{FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL},
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                );
                                if(cursor.moveToFirst()){
                                    do{
                                        String label = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LABEL));
                                        labelsBeingUsed.add(label);
                                    }while (cursor.moveToNext());
                                }
                                cursor.close();
                                if(labelsBeingUsed.contains(deletedText)){
                                    //不可删
                                    Toast.makeText(mActivityContext,"该标签被使用了，不能删",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    //可删
                                    isStoredLabelsEdited=true;
                                    storedLabels.remove(deletedText);
                                    //btnMap.remove(deletedText);
                                    rg.removeAllViews();
                                    //fillBtnList();
//                                putRealBtns();
//                                putFunctionalBtns();
                                    init();
                                    Log.d("MyRadioGroupTest","刚刚<删掉>，重新恢复putRealBtns+putFunctionalBtns");
                                    if(deletedText.equals(selectedText)){
                                        setHighlightButton("empty");
                                    }else {
                                        setHighlightButton(selectedText);
                                    }
                                }


                            }
                        }).setNegativeButton("No", null);
                        final AlertDialog alertDialog=alertBuilder.create();
                        alertDialog.show();
                    }
                    Log.d("labelTest","long click："+selectedText);
                    return true;
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_test);
        //
//        radioGroup=findViewById(R.id.label);
//        rb_visit=findViewById(R.id.radioButton_visit);
//        rb_visit.setTypeface(MyApplication.getInstance().getTypeface());

        //必须先初始化
//        rg=new MyRadioGroup(mActivityContext);

        //数据库
        dbHelper=new MyDatabaseHelper(this,MyDatabaseHelper.DATABASE_NAME,null, MyDatabaseHelper.DATABASE_VERSION);
        dbHelper.getWritableDatabase();
//        addInitialLabels();

        isGetLabelLibrary=false;
//        Log.d("labelTest",">>>>>database created");

        btn_back=findViewById(R.id.back);

        //标签
        label=findViewById(R.id.label);
        label_color_card=findViewById(R.id.label_color_card);
        label_text_card=findViewById(R.id.label_text_card);

        //展开的标签选择容器
        labelCardViewContainer =findViewById(R.id.labelCardViewContainer);

        setLabel(selectedText);


        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLabelOpen==false){
//                    Toast.makeText(labelTest.this,"可编辑🍭 ✔ ",Toast.LENGTH_SHORT).show();
                    isLabelOpen=true;
                    if(labelCardViewContainer.indexOfChild(rg)==-1){

                        rg=new MyRadioGroup(mActivityContext);
                        if(isGetLabelLibrary==false){
                            getStoredLabels();
//                            dbHelper=new MyDatabaseHelper(mActivityContext,MyDatabaseHelper.DATABASE_NAME,null,MyDatabaseHelper.DATABASE_VERSION);
//                            dbHelper.getWritableDatabase();
//                            Cursor cursor=db.query("PhotoList",null,null,null,null,null,null);

//                            addInitialLabels();
//                            Log.d("labelTest",">>>>>addInitialLabels");
                            Log.d("labelTest",">>>>>getStoredLabels");
                            isGetLabelLibrary=true;
                        }
                        init();
                        setHighlightButton(selectedText);
//                        for(final MyRadioButton button:rg.btnList){
//                            button.setOnClickListener(new View.OnClickListener() {
//                                @RequiresApi(api = Build.VERSION_CODES.O)
//                                @Override
//                                public void onClick(View v) {
//
//                                    if(button.isRealLabel()){
//                                        button.startMyAlphaAnimation();
//                                        selectedText=button.getMyTextView();
//                                        Toast.makeText(labelTest.this,"click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
//                                        //其他隐身(好好琢磨)
//                                        for(MyRadioButton iterateBtn: rg.btnList){
//                                            if(iterateBtn.getMyTextView()!=selectedText&&iterateBtn.isRealLabel()==true){
//                                                iterateBtn.setMyDefaultRealBtnAlhpa();
//                                            }
//                                        }
//                                    }else {
//                                        //+新的标签
//                                        Toast.makeText(labelTest.this,"+ 新的标签",Toast.LENGTH_SHORT).show();
//                                        //设想：加成功
//                                        //增加标签
//                                        String newText="动物";
//                                        String newColor="#6200EE";
//                                        rg.storedLabels.put(newText,newColor);
//                                        rg.removeAllViews();
//                                        //myGroupAddBtn(text, color,true,false);
//                                        rg.putRealBtns();
//                                        rg.putFunctionalBtns();
//                                        rg.setSelectedLabelText(newText);
//                                        selectedText=newText;
////                                        selectedText="动物";
//                                    }
//                                    Log.d("labelTest","short click 选择："+selectedText);
//                                }
//                            });
//                            button.setOnLongClickListener(new View.OnLongClickListener(){
//                                @Override
//                                public boolean onLongClick(View v) {
//                                    //Toast.makeText(getContext(),"长click: "+button.getMyTextView(),Toast.LENGTH_SHORT).show();
//                                    if(button.isFunctionalLabel()){
//                                        //do nothing
//                                        Toast.makeText(labelTest.this,"do nothing",Toast.LENGTH_SHORT).show();
//                                    }else {
//                                        //是否删除、删除
//                                        final String deletedText=button.getMyTextView();
//                                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(labelTest.this);
//                                        alertBuilder.setMessage("是否要删除： \""+deletedText+"\" ?");
//                                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                            @RequiresApi(api = Build.VERSION_CODES.O)
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                //删除标签
//                                                rg.storedLabels.remove(deletedText);
//                                                //btnMap.remove(deletedText);
//                                                rg.removeAllViews();
//                                                //fillBtnList();
//                                                rg.putRealBtns();
//                                                rg.putFunctionalBtns();
//                                                Log.d("MyRadioGroupTest","刚刚<删掉>，重新恢复putRealBtns+putFunctionalBtns");
//                                                if(deletedText==selectedText){
//                                                    selectedText=rg.setSelectedLabelText("empty");
//                                                }else {
//                                                    selectedText=rg.setSelectedLabelText(selectedText);
//                                                }
//
//                                            }
//                                        }).setNegativeButton("No", null);
//                                        final AlertDialog alertDialog=alertBuilder.create();
//                                        alertDialog.show();
//                                    }
//                                    Log.d("labelTest","long click 选择："+selectedText);
//                                    return true;
//
//
//                                }
//                            });
//                        }


//                        ViewTreeObserver observer=rg.getViewTreeObserver();
//                        observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//                            @Override
//                            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                                Toast.makeText(labelTest.this,"observer",Toast.LENGTH_SHORT).show();
//                            }
//                        });


//                        rg.setOnAddView(new MyRadioGroup.OnAddView() {
//
//                            @Override
//                            public void addView(View child) {
//                                if (child instanceof MyRadioButton) {
//                                    final MyRadioButton button=(MyRadioButton)child;
//                                    rg.btnList.add(button);
//
//                                }
//                            }
//                        });

//                        rg.setOnButtonChangeListener(new MyRadioGroup.OnButtonChangeListener() {
//                            @Override
//                            public void onSelectedButtonChange(@NonNull MyRadioGroup view, String selectedText) {
//                                Log.d("MyRadioGroupTest","get selectedText: "+selectedText);
//                            }
//                        });
//                        rg.OnChildClickedListener=()=>{
//                            labelChosen=rg.selectedText;
//                        }
//                        rg.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Log.d("labelTest","call my onClick");
//                            }
//                        });

                        MyRadioGroup.LayoutParams rg_layoutParams=new MyRadioGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        labelCardViewContainer.addView(rg,rg_layoutParams);
                    }
                }else {
                    labelCardViewContainer.removeAllViews();
                    isLabelOpen=false;
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStoredLabelsEdited){
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
//                    int deletedRows = db.delete(
//                        FeedReaderContract.FeedEntry.TABLE_NAME,
//                        null,
//                        null);
                    String SQL_ClearLabelLibrary="DELETE FROM "+ FeedReaderContract.FeedEntry.LABEL_TABLE_NAME;
                    db.execSQL(SQL_ClearLabelLibrary);
                    Toast.makeText(labelTest.this,"deleted",Toast.LENGTH_SHORT).show();


                    ContentValues values=new ContentValues();
                    for (Map.Entry<String, String> entry : storedLabels.entrySet()) {
                        String text = entry.getKey();
                        String color = entry.getValue();
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELTEXT, text);
                        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LABELCOLOR, color);
                        db.insert(FeedReaderContract.FeedEntry.LABEL_TABLE_NAME,null,values);
                        values.clear();
                    }
                    Toast.makeText(labelTest.this,"added end..",Toast.LENGTH_SHORT).show();


                }
            }
        });
    }


//    public void onRadioButtonClicked(View view){
//        String showButtonName="null";
//        boolean checked=((RadioButton) view).isChecked();
//        switch (view.getId()){
//            case R.id.radioButton_home:
//                if(checked){
//                    showButtonName="home";
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
//        Toast.makeText(labelTest.this,"click: "+showButtonName,Toast.LENGTH_SHORT).show();
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void setLabel(String label){
//        //理想的做法
//        //label_card.setText("标签："+item.getLabel_card());
//        //照片标签 item.getLabel_card()
//        switch (label){
//            case "travel":
//                label_text_card.setText(this.getString(R.string.travel));//用CardItem类的label属性设置标签的文字和背景色
//                label_color_card.setBackgroundColor(this.getColor(R.color.travel));
//                break;
//            case "aboard":
//                label_text_card.setText(this.getString(R.string.aboard));
//                label_color_card.setBackgroundColor(this.getColor(R.color.aboard));
//                break;
//            case "visit":
//                label_text_card.setText(this.getString(R.string.visit));
//                label_color_card.setBackgroundColor(this.getColor(R.color.visit));
//                break;
//            case "home":
//                label_text_card.setText(this.getString(R.string.home));
//                label_color_card.setBackgroundColor(this.getColor(R.color.home));
//                break;
//            case "empty":
//                label_text_card.setText(this.getString(R.string.empty));
//                label_color_card.setBackgroundColor(this.getColor(R.color.empty));
//                break;
//            default:
//                break;
//        }
//    }
}
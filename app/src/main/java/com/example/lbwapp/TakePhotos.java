package com.example.lbwapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TakePhotos extends Activity {
    public static final int RC_TAKE_PHOTO = 1;//相机
    public static final int RC_CHOOSE_PHOTO = 2;//相册

    public static int triggerToSettingNumber_library = 0;
    public static int triggerToSettingNumber_camera = 0;

//    private int a=0;//测试
//    private String mTempImagePath;
    private String imagePath;
    private File fileDir;
    private Uri fileDirUri;
    private File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photos);

//        if (ContextCompat.checkSelfPermission(TakePhotos.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
//            ActivityCompat.requestPermissions(TakePhotos.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, RC_CHOOSE_PHOTO);
//            Log.d("TakePhotos", "permission");
//        }

        Log.d("TakePhotos","onCreate,"+"triggerToSettingNumber_camera:"+TakePhotos.triggerToSettingNumber_camera);
        Log.d("TakePhotos","onCreate,"+"triggerToSettingNumber_library:"+TakePhotos.triggerToSettingNumber_library);
        ImageView imageView_bg=findViewById(R.id.bg);
        //点击背景图片回到主界面
        imageView_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ah","back to Home");
                Intent intent=new Intent(TakePhotos.this,PhotoList.class);
                startActivity(intent);
            }
        });
        CardView cardViewLibrary=findViewById(R.id.library);
        CardView cardViewCamera=findViewById(R.id.camera);

//        //图库：模拟器：通过shouldShowRequestPermissionRationale跳转“设置界面”
//        //sdk > 23,模拟器可用shouldShowRequestPermissionRationale
//        cardViewLibrary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(TakePhotos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    choosePhoto();
//                } else {
//                    Log.d("TakePhotos","library: no permission");
//                    boolean shouldRational=ActivityCompat.shouldShowRequestPermissionRationale(TakePhotos.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//                    Log.d("TakePhotos","library: shouldRational,"+shouldRational);
//                    if(shouldRational){
//                        //Show an explanation to the user
//                        Log.d("TakePhotos","shouldShowRequestPermissionRationale");
//                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(TakePhotos.this);
//                        alertBuilder.setMessage("To pick one photo from phone library");
//                        alertBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent=new Intent();
//                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                intent.setData(Uri.fromParts("package", getPackageName(), null));
//                                startActivity(intent);
////                                finish();
//                            }
//                        });
//                        alertBuilder.setNegativeButton("Disagree", null);
//                        final AlertDialog alertDialog=alertBuilder.create();
//                        alertDialog.show();
//                    }else {
//                        Log.d("TakePhotos","library: request");
//                        //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
//                        ActivityCompat.requestPermissions(TakePhotos.this,
//                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_PHOTO);
//                    }
//                }
//            }
//        });

        //图库：手机：通过自己定义int变量跳转“设置界面”,我的手机，华为荣耀20，不可用shouldShowRequestPermissionRationale
        cardViewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TakePhotos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
//                    a=a+1;
                    TakePhotos.triggerToSettingNumber_library = TakePhotos.triggerToSettingNumber_library +1;
                    Log.d("TakePhotos","library: no permission");
                    Log.d("TakePhotos","library: triggerToSettingNumber,"+TakePhotos.triggerToSettingNumber_library);
                    if(TakePhotos.triggerToSettingNumber_library >1){
                        //Show an explanation to the user
                        Log.d("TakePhotos","library: in triggerToSettingNumber");
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(TakePhotos.this);
                        alertBuilder.setMessage("To pick one photo from phone library");
                        alertBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            }
                        }).setNegativeButton("Disagree", null);
                        final AlertDialog alertDialog=alertBuilder.create();
                        alertDialog.show();
                    }else {
                        Log.d("TakePhotos","library: request");
                        //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                        ActivityCompat.requestPermissions(
                                TakePhotos.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                RC_CHOOSE_PHOTO);
                    }
                }
            }
        });

//        //相机：模拟器：通过shouldShowRequestPermissionRationale跳转“设置界面”
//        //sdk > 23,模拟器可用shouldShowRequestPermissionRationale
//        cardViewCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(TakePhotos.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(TakePhotos.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
//                    takePhoto();
//                } else {
//                    Log.d("TakePhotos","camera: no permission");
//                    boolean shouldRational=ActivityCompat.shouldShowRequestPermissionRationale(TakePhotos.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                            && ActivityCompat.shouldShowRequestPermissionRationale(TakePhotos.this, Manifest.permission.CAMERA);
//                    Log.d("TakePhotos","camera: shouldRational,"+shouldRational);
//                    if(shouldRational){
//                        //Show an explanation to the user
//                        Log.d("TakePhotos","shouldShowRequestPermissionRationale");
//                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(TakePhotos.this);
//                        alertBuilder.setMessage("To take a picture from camera");
//                        alertBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent=new Intent();
//                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                                intent.setData(Uri.fromParts("package", getPackageName(), null));
//                                startActivity(intent);
////                                finish();
//                            }
//                        });
//                        alertBuilder.setNegativeButton("Disagree", null);
//                        final AlertDialog alertDialog=alertBuilder.create();
//                        alertDialog.show();
//                    }else {
//                        Log.d("TakePhotos","camera: request");
//                        //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
//                        ActivityCompat.requestPermissions(TakePhotos.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, RC_TAKE_PHOTO);
//                    }
//                }
//            }
//        });

        //相机：手机：通过自己定义int变量跳转“设置界面”,我的手机，华为荣耀20，不可用shouldShowRequestPermissionRationale
        cardViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(TakePhotos.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(TakePhotos.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    TakePhotos.triggerToSettingNumber_camera =TakePhotos.triggerToSettingNumber_camera +1;
                    Log.d("TakePhotos","camera: no permission");
                    Log.d("TakePhotos","triggerToSettingNumber,"+TakePhotos.triggerToSettingNumber_camera);
                    if(TakePhotos.triggerToSettingNumber_camera >1){
                        //Show an explanation to the user
                        Log.d("TakePhotos","camera: in triggerToSettingNumber");
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(TakePhotos.this);
                        alertBuilder.setMessage("To take a picture from camera");
                        alertBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);
                            }
                        }).setNegativeButton("Disagree", null);
                        final AlertDialog alertDialog=alertBuilder.create();
                        alertDialog.show();
                    }else {
                        Log.d("TakePhotos","camera: request");
                        //未授权，申请授权(使用相机和读取存储卡的权限)
                        ActivityCompat.requestPermissions(TakePhotos.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, RC_TAKE_PHOTO);
                    }
                }
            }
        });

    }

    private void choosePhoto() {//拍照
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    private void takePhoto() {
        fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        //每张照片都是同一个名字“photo.jpeg”
//        File photoFile = new File(fileDir, "photo.jpeg");
        //为每张新拍的照片命名，日期+时间
        String curTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String photoName="IMG_"+curTime+".jpeg";//jpeg还是jpg
        photoFile=new File(fileDir,photoName);
//        mTempImagePath = photoFile.getAbsolutePath();
        imagePath=photoFile.getAbsolutePath();
        Uri imageUri = FileProvider7.getUriForFile(this, photoFile);

        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("resultCode:",String.valueOf(resultCode));
        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                if(data==null){//或者resultCode==RESULT_CANCELED
                    Toast.makeText(TakePhotos.this,"didn't choose any photos from library",Toast.LENGTH_LONG).show();
                }else {
                    String imagePath="";//局部变量，覆盖全局变量
                    Uri uri = data.getData();
                    String filePath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        imagePath=filePath;
                    }
                    Intent intent=new Intent(TakePhotos.this,ChoosePhotoPortrait.class);
                    intent.putExtra("imagePath",imagePath);
                    startActivity(intent);
                }
                break;
            case RC_TAKE_PHOTO:
                if(resultCode==RESULT_CANCELED){
                    Toast.makeText(TakePhotos.this,"didn't take any photos from camera",Toast.LENGTH_LONG).show();
                }else {
//                    imagePath=mTempImagePath;
                    //广播更新图库
                    Intent updateImageLibraryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    fileDirUri = Uri.fromFile(fileDir);//photoTest文件夹
                    updateImageLibraryIntent.setData(fileDirUri);
                    getApplicationContext().sendBroadcast(updateImageLibraryIntent);
                    Intent intent=new Intent(TakePhotos.this,ChoosePhotoPortrait.class);
                    intent.putExtra("imagePath",imagePath);//全局变量
                    startActivity(intent);
                }
                break;
        }



        //Intent intent=new Intent(TakePhotos.this,LibraryPhoto.class);
        //切换到相应显示的ChoosePhoto的页面，首先判断图片的横竖，再切换到<竖：ChoosePhotoPortrait>或<横：ChoosePhotoLandscape>
    }


    /**
     权限申请结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_TAKE_PHOTO:   //拍照权限申请返回
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }else {
                    Log.d("TakePhotos","onRequestPermissionsResult, no the camera permission");
                }
                break;
            case RC_CHOOSE_PHOTO:   //相册选择照片权限申请返回
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                }else {
                    Log.d("TakePhotos","onRequestPermissionsResult, no library permission");
                }
                break;
        }
    }

    //判断图片是竖的还是横的
//    private boolean isVertical(String imagePath){
//        boolean checkResult=false;
//        BufferedImage bufferImage;
//        try{
//            bufferImage=ImageIO.read(new File(imagePath));
//            int height=bufferImage.getHeight();
//            int width=bufferImage.getWidth();
//            if (height>width){
//                checkResult=true;
//            }else {
//                checkResult=false;
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return checkResult;
//    }
}




package com.qin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import com.qin.lafapplication.R;
import com.qin.utils.OkHttpCallback;
import com.qin.utils.OkHttpUtils;

import java.io.*;

public class CameraActivity extends AppCompatActivity {

    ImageView imageViewCamera;
    Button buttonCamera;

    public Uri cameraUri;
    public File file;
    public static final int REQUEST_CODE_CAMERA=1;
    public static final int REQUEST_PHOTO_CUT =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageViewCamera=findViewById(R.id.camera_imageView);
        buttonCamera=findViewById(R.id.camera_button);

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
    }

    //将剪切后的照片展示到ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                crop(cameraUri);
            }
        }
        else if(requestCode== REQUEST_PHOTO_CUT){
            //指定uri后data返回null,通过uri获取文件
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cameraUri));
                imageViewCamera.setImageBitmap(bitmap);

                file=bitmap2File(bitmap);
                OkHttpUtils.upload("http://192.168.137.1:8089/uploadImage",file.getAbsolutePath(),new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        super.onFinish(status, msg);

                        Log.e("com.qin",msg);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    //剪切照片
    public void crop(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");

        //开放uri读写权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
        //剪切框比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //输出图片尺寸
        intent.putExtra("outputX",500);
        intent.putExtra("outputY",500);
        //输出图片格式
        intent.putExtra("outputFormat","JPEG");
        //取消面部识别(避免破坏剪裁比例)
        intent.putExtra("onFaceDetection",true);

        intent.putExtra("return-data",true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraUri);

        //启动带有返回值,请求码为PHOTO_REQUEST_CUT的Activity
        startActivityForResult(intent,REQUEST_PHOTO_CUT);
    }

    public void startCamera(){
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,getCameraUri(getFile("temp_photo.png")));
        startActivityForResult(intent,REQUEST_CODE_CAMERA);
    }

    public Uri getCameraUri(File file){
        if(Build.VERSION.SDK_INT>24)
            cameraUri= FileProvider.getUriForFile(this,"com.qin.fileProvider",file);
        else
            cameraUri=Uri.fromFile(file);

        return cameraUri;
    }

    public File getFile(String fileName){
        file=new File(getExternalCacheDir(),fileName);
        if(file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    //将Bitmap转成File,便于上传服务器
    public File bitmap2File(Bitmap bitmap){
        BufferedOutputStream bufferedOutputStream=null;
        try {
            bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}

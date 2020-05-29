package com.qin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.qin.lafapplication.R;
import com.qin.utils.OkHttpCallback;
import com.qin.utils.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;

    public Uri cameraUri;
    public File file;
    private static final int REQUEST_CODE_CAMERA=1;
    private static final int REQUEST_CODE_PHOTO =2;
    private static final int REQUEST_CODE_CROP =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView=findViewById(R.id.addPhotoTest);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //调用相机
                        ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,getCameraUri(getFile("temp_photo.png")));
                        startActivityForResult(cameraIntent,REQUEST_CODE_CAMERA);
                        break;
                    case 1:
                        //调用相册
                        Intent photoIntent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(photoIntent, REQUEST_CODE_PHOTO);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                crop(cameraUri);
                break;
            case REQUEST_CODE_CROP:
                //指定uri后data返回null,通过uri获取文件
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(cameraUri));
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_PHOTO:
                Uri selectPhoto=data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(selectPhoto));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    //剪裁照片
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
        startActivityForResult(intent,REQUEST_CODE_CROP);
    }
}

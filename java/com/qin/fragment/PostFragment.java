package com.qin.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.PhotoActivity;
import com.qin.lafapplication.R;
import com.qin.utils.OkHttpCallback;
import com.qin.utils.OkHttpUtils;
import com.qin.vo.SpinnerItem;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class PostFragment extends Fragment implements View.OnClickListener {

    TextView cancelText;
    EditText titleEdit;
    RadioGroup radioGroupLoF;
    RadioButton radioButtonLost;
    RadioButton radioButtonFound;
    RadioGroup radioGroupAddress;
    RadioButton radioButtonSouth;
    RadioButton radioButtonNorth;
    Spinner spinnerCategory;
    Button buttonDate;
    TextView textViewDate;
    EditText detailEdit;
    ImageView imageViewPhoto;
    Button buttonPost;

    int year, month, day;
    public Uri cameraUri;
    public File file;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_CROP = 2;
    public static final int REQUEST_CODE_PHOTO = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        init(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelText:
                Intent intent = new Intent(getContext(), getActivity().getClass());
                startActivity(intent);
                break;
            case R.id.dateButton:
                getDate();
                chooseDate();
                break;
            case R.id.photoImageView:
                getPhoto();
                break;
            case R.id.postButton:
                post();
                break;
        }
    }

    public void getDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void getPhoto() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //调用相机
                        startCamera();
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

    public void chooseDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //month从0开始
                textViewDate.setText(year + "-" + (++month) + "-" + dayOfMonth);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), DatePickerDialog.THEME_HOLO_LIGHT, listener, year, month, day);
        datePickerDialog.show();
    }

    //发布 将数据上传服务器
    public void post() {
    // String name, Integer categoryId, String time, String address, String detail, String pictureUrl, @RequestParam("profile") MultipartFile file, Integer LoF, Integer userId

        String title=titleEdit.getText().toString().trim();
        if(title==null||title==""){
            Toast.makeText(getContext(),"标题不能为空",Toast.LENGTH_LONG).show();
            return;
        }

        Integer categoryId=Integer.parseInt(((SpinnerItem)spinnerCategory.getSelectedItem()).GetID());

        OkHttpUtils.get("http://47.105.143.99:8089/portal/post/add?",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
            }
        });


    }


    //将照片展示到ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode){
            case REQUEST_CODE_CAMERA:
                crop(cameraUri);
                break;
            case REQUEST_CODE_CROP:
                //指定uri后data返回null,通过uri获取文件
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(cameraUri));
                    imageViewPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_PHOTO:
                Uri selectPhoto=data.getData();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selectPhoto));
                    imageViewPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //剪裁照片
    public void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        //开放uri读写权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //剪切框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片尺寸
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        //输出图片格式
        intent.putExtra("outputFormat", "JPEG");
        //取消面部识别(避免破坏剪裁比例)
        intent.putExtra("onFaceDetection", true);

        intent.putExtra("return-data", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

        //启动带有返回值,请求码为PHOTO_REQUEST_CUT的Activity
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    public void startCamera() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,getCameraUri(getFile("temp_photo.png")));
        startActivityForResult(cameraIntent,REQUEST_CODE_CAMERA);
    }

    public Uri getCameraUri(File file) {
        if (Build.VERSION.SDK_INT > 24)
            cameraUri = FileProvider.getUriForFile(getContext(), "com.qin.fileProvider", file);
        else
            cameraUri = Uri.fromFile(file);

        return cameraUri;
    }

    public File getFile(String fileName) {
        file = new File(getContext().getExternalCacheDir(), fileName);
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    //将Bitmap转成File,便于上传服务器
    public File bitmap2File(Bitmap bitmap) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void init(View view){
        cancelText = view.findViewById(R.id.cancelText);
        titleEdit = view.findViewById(R.id.titleEdit);
        radioGroupLoF = view.findViewById(R.id.lofRadioGroup);
        radioButtonLost = view.findViewById(R.id.lostRadioButton);
        radioButtonFound = view.findViewById(R.id.foundRadioButton);
        radioGroupAddress = view.findViewById(R.id.addressRadioGroup);
        radioButtonSouth = view.findViewById(R.id.southRadioButton);
        radioButtonNorth = view.findViewById(R.id.northRadioButton);

        spinnerCategory = view.findViewById(R.id.categoeySpinner);
        List<SpinnerItem> spinnerItemList=new ArrayList<>();
        SpinnerItem spinnerItems[]=new SpinnerItem[6];
        spinnerItems[0]=new SpinnerItem("1","钥匙/U盘");
        spinnerItems[1]=new SpinnerItem("2","手机/证件");
        spinnerItems[2]=new SpinnerItem("3","书籍/文具");
        spinnerItems[3]=new SpinnerItem("4","现金/钱包");
        spinnerItems[4]=new SpinnerItem("5","眼镜/衣物");
        spinnerItems[5]=new SpinnerItem("6","其他");
        Collections.addAll(spinnerItemList,spinnerItems);
        ArrayAdapter<SpinnerItem> spinnerItemArrayAdapter=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,spinnerItemList);
        spinnerCategory.setAdapter(spinnerItemArrayAdapter);

        buttonDate = view.findViewById(R.id.dateButton);
        textViewDate = view.findViewById(R.id.dateText);
        detailEdit = view.findViewById(R.id.detailEditText);
        imageViewPhoto = view.findViewById(R.id.photoImageView);
        buttonPost=view.findViewById(R.id.postButton);

        cancelText.setOnClickListener(this);
        buttonDate.setOnClickListener(this);
        //点击editText全选
        detailEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    detailEdit.setSelectAllOnFocus(true);
                    detailEdit.selectAll();
                }
                return false;
            }
        });
        imageViewPhoto.setOnClickListener(this);
        buttonPost.setOnClickListener(this);
    }
}

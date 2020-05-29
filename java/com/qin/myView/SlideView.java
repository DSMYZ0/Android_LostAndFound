package com.qin.myView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.qin.lafapplication.R;

public class SlideView extends FrameLayout {

    private TextView titleTextView;
    private TextView addressTextView;
    private TextView dateTextView;
    private TextView detailTextView;
    private ImageView pictureImageView;

    public String getTitleText() {
        return titleTextView.getText().toString();
    }

    public void setTitleText(String titleText) {
        titleTextView.setText(titleText);
    }

    public String getAddressText() {
        return addressTextView.getText().toString();
    }

    public void setAddressText(String addressText) {
        addressTextView.setText(addressText);
    }

    public String getDateText() {
        return dateTextView.getText().toString();
    }

    public void setDateText(String dateText) {
        dateTextView.setText(dateText);
    }

    public String getDetailText() {
        return detailTextView.getText().toString();
    }

    public void setDetailText(String detailText) {
        detailTextView.setText(detailText);
    }

    public Bitmap getPictureImage() {
        return ((BitmapDrawable)(pictureImageView.getDrawable())).getBitmap();
    }

    public void setPictureImage(Bitmap pictureImage) {
        pictureImageView.setImageBitmap(pictureImage);
//        pictureImageView.setImageResource(R.mipmap.xiangji);
    }

    public SlideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.slideview,this);

        titleTextView=findViewById(R.id.titleText);
        addressTextView=findViewById(R.id.addressText);
        dateTextView=findViewById(R.id.dateText);
        detailTextView=findViewById(R.id.detailText);
        pictureImageView=findViewById(R.id.pictureView);
        detailTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

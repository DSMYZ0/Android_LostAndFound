package com.qin.vo;

import android.graphics.Bitmap;

public class PostVO {
    private int id;
    private String name;
    private int category_id;
    private int user_id;
    private String time;
    private String address;
    private String detail;
    private String picture_url;
    private String picture;
    private int LoF;    //json返回字段首字母默认小写
    private Bitmap bitmapPic;

    public Bitmap getBitmapPic() {
        return bitmapPic;
    }

    public void setBitmapPic(Bitmap bitmapPic) {
        this.bitmapPic = bitmapPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getLoF() {
        return LoF;
    }

    public void setLoF(int LoF) {
        this.LoF = LoF;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

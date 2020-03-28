package com.qin.vo;


/**
 * 封装前端返回的统一实体类
 * */
public class ServerResponse<T> {

    private int state; //状态 0:接口调用成功
    private T data; //当status=0，将返回的数据封装到data中
    private String msg; //提示信息


    public  ServerResponse(){}


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package com.qin;

import android.content.Intent;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qin.lafapplication.R;
import com.qin.utils.OkHttpCallback;
import com.qin.utils.OkHttpUtils;
import com.qin.utils.SharedPreferencesUtil;
import com.qin.vo.ServerResponse;
import com.qin.vo.UserVO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText_username;
    EditText editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取控件
        editText_username=findViewById(R.id.username);
        editText_password=findViewById(R.id.password);
        Button button_login=findViewById(R.id.login);
        TextView textView_forgetPassword=findViewById(R.id.forget_password);
        TextView textView_register=findViewById(R.id.register);

        //注册点击监听
        button_login.setOnClickListener(this);
        textView_forgetPassword.setOnClickListener(this);
        textView_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                //获取用户名和密码
                String username=editText_username.getText().toString();
                String password=editText_password.getText().toString();
//                Toast.makeText(this,username,Toast.LENGTH_LONG).show();
                //请求接口
                OkHttpUtils.get("http://192.168.137.1:8089/portal/user/login?username="+username+"&password="+password,
                        new OkHttpCallback(){
                            @Override
                            public void onFinish(String status, String msg) {
                                super.onFinish(status, msg);
                                //子线程调用Toast加上Looper
                                /*Looper.prepare();
                                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                                Looper.loop();*/
                                //解析数据
                                Gson gson = new Gson();
                                //复杂对象解析
                                ServerResponse<UserVO> serverResponse=gson.fromJson(msg,new TypeToken<ServerResponse<UserVO>>(){}.getType());
                                int state=serverResponse.getState();
                                if(state==0){
                                    //保存用户信息
/*   封装到SharedPreferencesUtil    SharedPreferences sharedPreferences=LoginActivity.this.getSharedPreferences("userInfo",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putBoolean("isLogin",true);
                                    editor.putString("user",msg);
                                    editor.commit();*/
                                    SharedPreferencesUtil sharedPreferencesUtil=SharedPreferencesUtil.getInstance(LoginActivity.this);
                                    sharedPreferencesUtil.putBoolean("isLogin",true);
                                    sharedPreferencesUtil.putString("user",gson.toJson(serverResponse.getData()));
                                    //页面跳转
                                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                    //在子线程中不能用startActivity(intent)
                                    LoginActivity.this.startActivity(intent);
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, serverResponse.getMsg(), Toast.LENGTH_LONG).show();
                                    Looper.loop();
                                }
                            }
                        });
                //解析接口返回的数据
        }
    }
}

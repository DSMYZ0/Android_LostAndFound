package com.qin;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextContact;
    EditText editTextPassword;
    EditText editTextRePassword;
    Button buttonSaveProfile;
    TextView textViewRePassword;
    TextView textViewUsername;


    String passwordUrl = "";
    String password = "";
    String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextContact = findViewById(R.id.contactProfile);
        editTextPassword = findViewById(R.id.passwordProfile);
        editTextRePassword = findViewById(R.id.repasswordProfile);
        buttonSaveProfile = findViewById(R.id.saveProfileButton);
        textViewRePassword = findViewById(R.id.repasswordTextView);
        textViewUsername = findViewById(R.id.usernameProfile);

        //获取用户信息
        UserVO userVO = (UserVO) SharedPreferencesUtil.getInstance(ProfileActivity.this).readObject("user", UserVO.class);
        textViewUsername.setText(userVO.getUsername());
        editTextContact.setText(userVO.getContact());
        editTextPassword.setText("12345678");

        editTextRePassword.setVisibility(View.GONE);
        textViewRePassword.setVisibility(View.GONE);

        buttonSaveProfile.setOnClickListener(this);
        //监听EditView
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textViewRePassword.setVisibility(View.VISIBLE);
                editTextRePassword.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordUrl = "&password=";
            }
        });

    }

    @Override
    public void onClick(View v) {

        contact = editTextContact.getText().toString();


        //修改密码
        if (passwordUrl.equals("&password=")) {

            password = editTextPassword.getText().toString().trim();

            if (!password.equals(editTextRePassword.getText().toString().trim())) {
                Toast.makeText(ProfileActivity.this, "密码不一致", Toast.LENGTH_LONG).show();
            }
        }

        OkHttpUtils.get("http://47.105.143.99:8089/portal/user/update?contact=" + contact + passwordUrl + password, new OkHttpCallback() {
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                //解析数据
                Gson gson = new Gson();
                //复杂对象解析
                ServerResponse<UserVO> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<UserVO>>() {
                }.getType());
                int state = serverResponse.getState();
                if (state == 0) {
                    //保存用户信息
                    SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance(ProfileActivity.this);
                    sharedPreferencesUtil.putString("user", gson.toJson(serverResponse.getData()));

                    Looper.prepare();
                    Toast.makeText(ProfileActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }


}

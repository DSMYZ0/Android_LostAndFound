package com.qin.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qin.CameraActivity;
import com.qin.HomeActivity;
import com.qin.LoginActivity;
import com.qin.ProfileActivity;
import com.qin.lafapplication.R;
import com.qin.utils.SharedPreferencesUtil;
import com.qin.vo.UserVO;

public class MineFragment extends Fragment implements View.OnClickListener {

    TextView textViewUsername;
    Button buttonLoginout;
    Button buttonModifyProfile;
    Button buttonMyPost;
    ImageView imageViewProfilePicture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        textViewUsername=view.findViewById(R.id.mineTextView);
        buttonLoginout=view.findViewById(R.id.logoutButton);
        buttonModifyProfile=view.findViewById(R.id.modifyProdileButton);
        buttonMyPost=view.findViewById(R.id.myPostButton);
        imageViewProfilePicture=view.findViewById(R.id.profilePictureImageView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //判断用户是否登录
        Boolean isLogin= SharedPreferencesUtil.getInstance(getActivity()).readBoolean("isLogin");
        if(isLogin){
            UserVO userVO=(UserVO) SharedPreferencesUtil.getInstance(getActivity()).readObject("user",UserVO.class);
            textViewUsername.setText(userVO.getUsername());
            buttonLoginout.setVisibility(View.VISIBLE);
            buttonMyPost.setVisibility(View.VISIBLE);

            buttonModifyProfile.setVisibility(View.VISIBLE);
            buttonLoginout.setOnClickListener(this);
            buttonModifyProfile.setOnClickListener(this);
            imageViewProfilePicture.setOnClickListener(this);
        }
        else {
            changeView();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mineTextView:
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.logoutButton:
                SharedPreferencesUtil.getInstance(getActivity()).clear();
                changeView();
                break;
            case R.id.modifyProdileButton:
                Intent intent1=new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent1);
            case R.id.myPostButton:
                break;
            case R.id.profilePictureImageView:
                Intent intent2=new Intent(getActivity(), CameraActivity.class);
                startActivity(intent2);
        }
    }

    public void changeView(){
        textViewUsername.setText("点击登录");
        buttonLoginout.setVisibility(View.GONE);
        buttonModifyProfile.setVisibility(View.GONE);
        buttonMyPost.setVisibility(View.GONE);
        textViewUsername.setOnClickListener(this);
    }
}

package com.qin;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.qin.fragment.GroundFragment;
import com.qin.fragment.MineFragment;
import com.qin.fragment.PostFragment;
import com.qin.lafapplication.R;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout linearLayoutSquare;
    LinearLayout linearLayoutPost;
    LinearLayout linearLayoutMine;
    ImageView imageViewSquare;
    ImageView imageViewPost;
    ImageView imageViewMine;
    TextView textViewSqure;
    TextView textViewPost;
    TextView textViewMine;

    List<TextView> textViewList=new ArrayList<>();

    public static final String GROUNDFRAGMENT_TAG="GROUND";
    public static final String POSTFRAGMENT_TAG="POST";
    public static final String MINEFRAGMENT_TAG="MINE";

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        linearLayoutSquare=findViewById(R.id.square);
        linearLayoutPost=findViewById(R.id.post);
        linearLayoutMine=findViewById(R.id.mine);
        imageViewSquare=findViewById(R.id.squareImage);
        imageViewPost=findViewById(R.id.postImage);
        imageViewMine=findViewById(R.id.mineImage);
        textViewSqure=findViewById(R.id.squareText);
        textViewPost=findViewById(R.id.postText);
        textViewMine=findViewById(R.id.mineText);

        textViewList.add(textViewSqure);
        textViewList.add(textViewPost);
        textViewList.add(textViewMine);

        linearLayoutSquare.setOnClickListener(this);
        linearLayoutPost.setOnClickListener(this);
        linearLayoutMine.setOnClickListener(this);

        attachFragment(GROUNDFRAGMENT_TAG);

    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.square:attachFragment(GROUNDFRAGMENT_TAG);
                setTextViewState(R.id.squareText);
                imageViewSquare.setImageResource(R.mipmap.square);
                imageViewPost.setImageResource(R.mipmap.post_u);
                imageViewMine.setImageResource(R.mipmap.mine_u);
                break;
            case R.id.post:attachFragment(POSTFRAGMENT_TAG);
                setTextViewState(R.id.postText);
                imageViewPost.setImageResource(R.mipmap.post);
                imageViewSquare.setImageResource(R.mipmap.square_u);
                imageViewMine.setImageResource(R.mipmap.mine_u);
                break;
            case R.id.mine:attachFragment(MINEFRAGMENT_TAG);
                setTextViewState(R.id.mineText);
                imageViewMine.setImageResource(R.mipmap.mine);
                imageViewSquare.setImageResource(R.mipmap.square_u);
                imageViewPost.setImageResource(R.mipmap.post_u);
                break;
        }
    }

    public void setTextViewState(int id){
        for(TextView textView:textViewList){
            if(textView.getId()==id)
                textView.setSelected(true);
            else
                textView.setSelected(false);
        }
    }

    public void attachFragment(String fragmentTag){
        //获取fragment管理器
        FragmentManager fragmentManager=getSupportFragmentManager();
        //开启事务
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        Fragment fragment=fragmentManager.findFragmentByTag(fragmentTag);
        if(fragment==null){
            //管理器中不存在该fragment
            switch (fragmentTag){
                case GROUNDFRAGMENT_TAG:fragment=new GroundFragment();break;
                case POSTFRAGMENT_TAG:fragment=new PostFragment();break;
                case MINEFRAGMENT_TAG:fragment=new MineFragment();break;
            }
            //添加fragment
            fragmentTransaction.add(fragment,fragmentTag);
        }

        fragmentTransaction.replace(R.id.content,fragment,fragmentTag);

        fragmentTransaction.commit();
    }
}

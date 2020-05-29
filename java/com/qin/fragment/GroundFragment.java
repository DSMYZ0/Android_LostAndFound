package com.qin.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qin.PostDetailActivity;
import com.qin.adapter.RVAdapter;
import com.qin.adapter.RecyclerViewAdapter;
import com.qin.adapter.ViewPagerAdapter;
import com.qin.lafapplication.R;
import com.qin.myView.SlideView;
import com.qin.utils.OkHttpCallback;
import com.qin.utils.OkHttpCallbackFile;
import com.qin.utils.OkHttpUtils;
import com.qin.vo.PageInfo;
import com.qin.vo.PostVO;
import com.qin.vo.ServerResponse;

import java.util.ArrayList;
import java.util.List;

public class GroundFragment extends Fragment implements View.OnClickListener {

    List<PostVO> postVOListS;
    ViewPager viewPager;
    RecyclerView recyclerView;
    PagerAdapter pagerAdapter;
    RecyclerViewAdapter recyclerViewAdapter;
    private List<SlideView> carouselViewList = new ArrayList<>();
    private List<SlideView> slideViewList = new ArrayList<>();
    private static final int CAROUSEL_NOTIFY = 1;
    private static final int SLIDE_NOTIFY = 2;
    private static final int LOAD_CAROUSEL_PIC = 3;
    private static final int LOAD_SLIDE_PIC = 4;
    private static final int SET_VIEWPAGER_ADAPTER = 5;
    private static final int SET_RECYCLERVIEW_ADAPTER = 6;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case CAROUSEL_NOTIFY:
                    List<PostVO> postVOListC = (List<PostVO>) msg.obj;
                    //创建view 渲染到viewpager上
                    render(postVOListC, CAROUSEL_NOTIFY);
                    break;
                case SLIDE_NOTIFY:
                    postVOListS = (List<PostVO>) msg.obj;
                    render(postVOListS, SLIDE_NOTIFY);
                    break;
                case LOAD_CAROUSEL_PIC:
                    SlideView carouselView = carouselViewList.get(msg.arg1);
                    byte[] carouselBytes = (byte[]) msg.obj;
                    Bitmap carouselBitmap = BitmapFactory.decodeByteArray(carouselBytes, 0, carouselBytes.length);
                    carouselView.setPictureImage(carouselBitmap);
                    if(msg.arg2==-1){
                        Message message = handler.obtainMessage();
                        message.what = SET_RECYCLERVIEW_ADAPTER;
                        handler.sendMessage(message);
                    }
                    break;
                case LOAD_SLIDE_PIC:
                    SlideView slideView = slideViewList.get(msg.arg1);
                    byte[] slideBytes = (byte[]) msg.obj;
                    Bitmap slideBitmap = BitmapFactory.decodeByteArray(slideBytes, 0, slideBytes.length);
                    slideView.setPictureImage(slideBitmap);
                    if(msg.arg2==-1){
                        Message slideMessage = handler.obtainMessage();
                        slideMessage.what = SET_VIEWPAGER_ADAPTER;
                        handler.sendMessage(slideMessage);
                    }
                    break;
                case SET_VIEWPAGER_ADAPTER:
                    pagerAdapter = new ViewPagerAdapter(carouselViewList);
                    viewPager.setAdapter(pagerAdapter);
                    break;
                case SET_RECYCLERVIEW_ADAPTER:
                    //        recyclerView需要设置布局管理器
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
//                    RVAdapter adapter = new RVAdapter(postVOListS);
                    RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(slideViewList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    break;
            }
        }
    };

    private void render(List<PostVO> postVOList, int sign) {
        switch (sign) {
            case CAROUSEL_NOTIFY:
                getCarouselPicture(postVOList);
                break;
            case SLIDE_NOTIFY:
                getSlidePicture(postVOList);
                break;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ground, container, false);

        viewPager = view.findViewById(R.id.carouselViewPager);
        recyclerView = view.findViewById(R.id.recyclerView);

        initData();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("postID", v.getId());
        startActivity(intent);
    }

    public void getCarouselPicture(final List<PostVO> postVOList) {
        for (int i = 0; i < postVOList.size(); i++) {
            PostVO postVO = postVOList.get(i);
            //获取图片uri
            String uri = postVO.getPicture();

            SlideView carouselView = new SlideView(getContext(), null);
            carouselView.setId(postVO.getId());
            carouselView.setTitleText(postVO.getName());
            carouselView.setAddressText(postVO.getAddress());
            carouselView.setDateText(postVO.getTime());
            carouselViewList.add(carouselView);


            OkHttpUtils.get("http://47.105.143.99/" + uri, new OkHttpCallbackFile(i) {
                @Override
                public void onFinish(String status, byte[] msg, int position) {
                    Message message = handler.obtainMessage();
                    message.what = LOAD_CAROUSEL_PIC;
                    message.obj = msg;
                    message.arg1 = position;
                    if(position==postVOList.size()-1)
                        message.arg2=-1;
                    handler.sendMessage(message);
                }
            });
        }

    }

    public void getSlidePicture(final List<PostVO> postVOList) {
        for (int i = 0; i < postVOList.size(); i++) {
            PostVO postVO = postVOList.get(i);
            //获取图片uri
            String uri = postVO.getPicture();

            SlideView slideView = new SlideView(getContext(), null);
            slideView.setId(postVO.getId());
            slideView.setTitleText(postVO.getName());
            slideView.setAddressText(postVO.getAddress());
            slideView.setDateText(postVO.getTime());
            slideViewList.add(slideView);


            OkHttpUtils.get("http://47.105.143.99/" + uri, new OkHttpCallbackFile(i) {
                @Override
                public void onFinish(String status, byte[] msg, int position) {
                    Message message = handler.obtainMessage();
                    message.what = LOAD_SLIDE_PIC;
                    message.obj = msg;
                    message.arg1 = position;
                    //最后一次调用
                    if(position==postVOList.size()-1)
                        message.arg2=-1;
                    handler.sendMessage(message);
                }
            });
        }

    }

    public void initData() {
        OkHttpUtils.get("http://47.105.143.99:8089/portal/post/carousel", new OkHttpCallback() {
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                Gson gson = new Gson();
                ServerResponse<List<PostVO>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<PostVO>>>() {
                }.getType());

                if (serverResponse.getState() == 0) {

                    List<PostVO> postVOList = serverResponse.getData();

                    Message message = new Message();
                    message.what = CAROUSEL_NOTIFY;
                    message.obj = postVOList;
                    handler.sendMessage(message);
                }
            }
        });

        OkHttpUtils.get("http://47.105.143.99:8089/portal/post/retrieve", new OkHttpCallback() {
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);

                Gson gson = new Gson();
                ServerResponse<PageInfo> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<PageInfo>>() {
                }.getType());

                if (serverResponse.getState() == 0) {

                    List<PostVO> postVOList = serverResponse.getData().getList();

                    Message message = new Message();
                    message.what = SLIDE_NOTIFY;
                    message.obj = postVOList;

                    handler.sendMessage(message);
                }
            }
        });
    }
}

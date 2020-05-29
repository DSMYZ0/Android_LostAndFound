package com.qin.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qin.lafapplication.R;
import com.qin.vo.PostVO;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.InnerHolder> {

    private final List<PostVO> mdata;


    public RVAdapter(List<PostVO> data) {
        this.mdata=data;
    }

    /**
     * 创建ItemView
     * */
    @NonNull
    @Override
    public RVAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.slideview,null);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.InnerHolder holder, int position) {
        holder.setData(mdata.get(position));
    }

    @Override
    public int getItemCount() {
            return mdata.size();
    }


    public class InnerHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewTitle;
        TextView textViewAddress;
        TextView textViewDate;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);

             imageView=itemView.findViewById(R.id.pictureView);
             textViewTitle=itemView.findViewById(R.id.titleText);
             textViewAddress=itemView.findViewById(R.id.addressText);
             textViewDate=itemView.findViewById(R.id.dateText);
        }


        public void setData(PostVO postVO) {
//            imageView.setImageResource(postVO);
            textViewAddress.setText(postVO.getAddress());
            textViewTitle.setText(postVO.getName());
            textViewDate.setText(postVO.getTime());
        }
    }
}

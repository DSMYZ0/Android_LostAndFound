package com.qin.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.qin.lafapplication.R;
import com.qin.myView.SlideView;
import com.qin.vo.PostVO;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    public RecyclerViewAdapter(List<SlideView> slideViewList) {
        this.slideViewList = slideViewList;
    }

    private List<SlideView> slideViewList;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slideView= LayoutInflater.from(parent.getContext()).inflate(R.layout.slideview,parent,false);
        return new RecyclerViewHolder(slideView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        SlideView slideView=slideViewList.get(position);
        holder.titleTextView.setText(slideView.getTitleText());
        holder.addressTextView.setText(slideView.getAddressText());
        holder.dateTextView.setText(slideView.getDateText());
        holder.picImageView.setImageBitmap(slideView.getPictureImage());
    }

    @Override
    public int getItemCount() {
        return slideViewList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView addressTextView;
        TextView dateTextView;
        ImageView picImageView;

        public RecyclerViewHolder(View slideView) {
            super(slideView);
            this.titleTextView = slideView.findViewById(R.id.titleText);
            this.addressTextView = slideView.findViewById(R.id.addressText);
            this.dateTextView = slideView.findViewById(R.id.dateText);
            this.picImageView = slideView.findViewById(R.id.pictureView);
        }
    }

}

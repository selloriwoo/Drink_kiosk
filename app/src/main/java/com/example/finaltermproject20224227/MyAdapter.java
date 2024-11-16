package com.example.finaltermproject20224227;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public Context ctx;
    public LayoutInflater mInflater;
    public ArrayList<ReviewData> reviewDataArrayList;
    private ItemClickListener mClickListener;

    public MyAdapter(Context ctx, ArrayList<ReviewData> reviewDataArrayList){
        this.ctx = ctx;
        this.reviewDataArrayList = reviewDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv1.setText(reviewDataArrayList.get(position).review);
        holder.img1.setImageResource(reviewDataArrayList.get(position).pic);
        holder.rb1.setRating(reviewDataArrayList.get(position).starPoint);
    }


    @Override
    public int getItemCount() {
        return reviewDataArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv1;
        ImageView img1;
        RatingBar rb1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.communityItemTv1);
            img1 = itemView.findViewById(R.id.communityItemIv1);
            rb1 = itemView.findViewById(R.id.communityItemRb1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
    ReviewData getItem(int id) {return reviewDataArrayList.get(id);}
}

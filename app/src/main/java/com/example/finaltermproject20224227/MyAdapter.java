package com.example.finaltermproject20224227;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public Context ctx;
    public ArrayList<Review> reviewArrayList;

    public MyAdapter(Context ctx, ArrayList<Review> reviewArrayList){
        this.ctx = ctx;
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.communityItemTv1.setText(reviewArrayList.get(position).getReviewText());
        holder.communityItemIv1.setImageBitmap(getImage(reviewArrayList.get(position).getDrinkItemPic()));
        holder.communityItemRb1.setRating(reviewArrayList.get(position).getRating());
        holder.communityItemName.setText(reviewArrayList.get(position).getDrinkItemName());
    }


    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView communityItemTv1, communityItemName;
        ImageView communityItemIv1;
        RatingBar communityItemRb1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            communityItemTv1 = itemView.findViewById(R.id.communityItemTv1);
            communityItemIv1 = itemView.findViewById(R.id.communityItemIv1);
            communityItemRb1 = itemView.findViewById(R.id.communityItemRb1);
            communityItemName = itemView.findViewById(R.id.communityItemName);
        }

    }

    // 이미지를 로컬 저장소에서 불러오기
    private Bitmap getImage(String imageName) {
        if (imageName == null) {
            return null; // 이미지 파일 이름이 null인 경우 null 반환
        }
        // 파일 이름에 .png 확장자가 이미 포함되어 있는지 확인
        if (!imageName.endsWith(".png")) {
            imageName += ".png";
        }
        File directory = ctx.getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName);
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Log.e("getImage", "Image file not found: " + imageFile.getAbsolutePath());
            return null;
        }
    }
}


package com.example.finaltermproject20224227;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ShoppingCartRecycleViewAdapter extends RecyclerView.Adapter<ShoppingCartRecycleViewAdapter.ViewHolder>{

    private Context ctx;
    private ArrayList<DrinkItem> drinkItemArrayList;
    private ShoppingCart shoppingCart;

    public ShoppingCartRecycleViewAdapter(Context ctx, ArrayList<DrinkItem> drinkItemArrayList, ShoppingCart shoppingCart) {
        this.drinkItemArrayList = drinkItemArrayList;
        this.ctx = ctx;
        this.shoppingCart = shoppingCart;
    }

    @NonNull
    @Override
    public ShoppingCartRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartRecycleViewAdapter.ViewHolder holder, int position) {
        holder.cartItemTvName.setText(drinkItemArrayList.get(position).getName());
        holder.cartItemTvPrice.setText(String.valueOf(drinkItemArrayList.get(position).getPrice()));
        holder.cartItemImg.setImageBitmap(getImage(drinkItemArrayList.get(position).getPic()));
        holder.cartViewTvQuantity.setText(String.valueOf(drinkItemArrayList.get(position).getQuantity()));

        // 삭제 버튼 클릭 이벤트 처리
        holder.cartViewBtnDelete.setOnClickListener(v -> {
            int position1 = holder.getAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                // total 값 업데이트
                shoppingCart.updateTotal(-drinkItemArrayList.get(position1).getPrice());

                drinkItemArrayList.remove(position1);
                notifyItemRemoved(position1);
                notifyItemRangeChanged(position1, drinkItemArrayList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cartItemTvName;
        TextView cartItemTvPrice;
        TextView cartViewTvQuantity;
        ImageView cartItemImg;
        Button cartViewBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemTvName = itemView.findViewById(R.id.cartItemTvName);
            cartItemTvPrice = itemView.findViewById(R.id.cartItemTvPrice);
            cartItemImg = itemView.findViewById(R.id.cartItemImg);
            cartViewTvQuantity = itemView.findViewById(R.id.cartViewTvQuantity);
            cartViewBtnDelete = itemView.findViewById(R.id.cartViewBtnDelete);
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

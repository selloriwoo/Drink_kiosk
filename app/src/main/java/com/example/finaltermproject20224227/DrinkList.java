package com.example.finaltermproject20224227;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrinkList extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain, tvCartCount;
    TextView tvPrice1, tvPrice2, tvPrice3, tvPrice4, tvPrice5, tvPrice6, tvPrice7, tvPrice8, tvPrice9;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, orderCartIb;
    Button btnWhisky, btnVodka, btnCocktail, btnSake, btnBack, btnCommunity, listAddBtn, addItemFinish, addImageBtn;
    ImageView addIv;
    LinearLayout listLayout1, listLayout2, listLayout3, listLayout4, listLayout5, listLayout6, listLayout7, listLayout8, listLayout9;
    TextView[] textViews = {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain};
    TextView[] tvPrices = {tvPrice1, tvPrice2, tvPrice3, tvPrice4, tvPrice5, tvPrice6, tvPrice7, tvPrice8, tvPrice9};
    ImageView[] imageViews = {iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9};
    Button[] buttons = {btnWhisky, btnVodka, btnCocktail, btnSake};
    LinearLayout[] linearLayouts = {listLayout1, listLayout2, listLayout3, listLayout4, listLayout5, listLayout6, listLayout7, listLayout8, listLayout9};
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Bitmap selectedBitmap;
    DBhelper dBhelper;
    int categoryPtr = 1;
    int cartCountVal = 0;
    ArrayList<DrinkItem> shoppingCartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drink_list);

        int[] textViewId = {R.id.tv1, R.id.tv2,R.id.tv3,R.id.tv4,R.id.tv5,R.id.tv6,R.id.tv7,R.id.tv8,R.id.tv9};
        int[] tvPriceID = {R.id.tvPrice1, R.id.tvPrice2, R.id.tvPrice3, R.id.tvPrice4, R.id.tvPrice5, R.id.tvPrice6, R.id.tvPrice7, R.id.tvPrice8, R.id.tvPrice9};
        int[] imageButtonId = {R.id.iv1,R.id.iv2,R.id.iv3,R.id.iv4,R.id.iv5,R.id.iv6,R.id.iv7,R.id.iv8,R.id.iv9};
        int[] buttonId = {R.id.btnWhisky, R.id.btnVodka, R.id.btnCocktail, R.id.btnSake};
        int[] linearLayoutId = {R.id.listLayout1, R.id.listLayout2, R.id.listLayout3, R.id.listLayout4, R.id.listLayout5, R.id.listLayout6, R.id.listLayout7, R.id.listLayout8, R.id.listLayout9};
        btnBack = findViewById(R.id.btnBack);
        btnCommunity = findViewById(R.id.btnCommunity);
        orderCartIb = findViewById(R.id.orderCartIb);
        listAddBtn = findViewById(R.id.listAddBtn);
        dBhelper = new DBhelper(this);
        listLayout1 = findViewById(R.id.listLayout1);



        listLayout1.setOnClickListener(view -> {
            Toast.makeText(this, "www", Toast.LENGTH_SHORT).show();
        });
        //db파일 없으면 생성
        if (!dBhelper.checkDatabase()) {
            dBhelper.getWritableDatabase();
        }

        //초기화
        tvCartCount = findViewById(R.id.tvCartCount);
                for (int i = 0; i<imageViews.length; i++){
                    textViews[i] = findViewById(textViewId[i]);
                    imageViews[i] = findViewById(imageButtonId[i]);
                }
                for (int i = 0; i<linearLayouts.length; i++)
                    linearLayouts[i] = findViewById(linearLayoutId[i]);

                for (int i = 0; i<tvPrices.length; i++)
                    tvPrices[i] = findViewById(tvPriceID[i]);

                for (int i = 0; i < buttons.length; i++)
                    buttons[i]= findViewById(buttonId[i]);

        for (int i = 0; i <buttons.length; i++){
            int index = i+1;
            buttons[i].setOnClickListener(view -> {
                categoryPtr= index;
                List<DrinkItem> drinkItems = dBhelper.getDrinkItemsByKindId(categoryPtr);
                viewListItem(drinkItems);
            });
        }
        for (int i =0; i < linearLayouts.length; i++){
            int index = i; //안 적으면 리스너 안에서 i 값 사용 불가
            linearLayouts[i].setOnClickListener(view -> {
                //todo 클릭시 장바구니에 담게.
                addToCart(new DrinkItem(textViews[index].getText().toString(),getBytesFromBitmap(((BitmapDrawable)imageViews[index].getDrawable()).getBitmap()),categoryPtr, Integer.parseInt(tvPrices[index].getText().toString())));
                Bitmap bitmap = BitmapFactory.decodeByteArray(shoppingCartList.get(index).getPic(), 0, shoppingCartList.get(index).getPic().length);
                imageViews[7].setImageBitmap(bitmap);
                cartCountVal++;
                tvCartCount.setText(""+cartCountVal);
                Toast.makeText(this,shoppingCartList.get(index).getName()+"를 장바구니에 넣었습니다. 개수"+shoppingCartList.get(index).getQuantity()+index,Toast.LENGTH_SHORT).show();
                for (DrinkItem item: shoppingCartList) {
                    Log.d("item", "이름:"+item.getName());
                }
            });
        }

        //todo: 시작시 위스키로 시작하고 카테고리 클릭시 해당 술 나오게 하기.
        List<DrinkItem> firstDrinkItems = dBhelper.getDrinkItemsByKindId(1);
        viewListItem(firstDrinkItems);

        btnBack.setOnClickListener(view -> {
            finish();
        });
        btnCommunity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Community.class);
            startActivity(intent);
        });

        orderCartIb.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), shoppingCart.class);
            intent.putExtra("shopping_card",shoppingCartList);
            startActivity(intent);
        });

        listAddBtn.setOnClickListener(view -> {
            final Dialog finishDialog = new Dialog(this);
            finishDialog.setContentView(R.layout.activity_add_shopping_item);
            finishDialog.setTitle("ww");
            addItemFinish = finishDialog.findViewById(R.id.addItemFinish);
            addImageBtn = finishDialog.findViewById(R.id.addImageBtn);
            addIv = finishDialog.findViewById(R.id.addIv);

            //todo: radio버튼
            addImageBtn.setOnClickListener(view1 -> {
                    openFileChooser();
            });
            addItemFinish.setOnClickListener(view1 -> {
                saveStoreItem();
                finish();
            });
            finishDialog.show();
        });

        //todo 술 종류별 처리

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                addIv.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveStoreItem() {
        if (selectedBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            //todo: 여러값 추가
//            dBhelper.addItem(imageBytes, "ww", 1L,200);
            Toast.makeText(this, "상품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "먼저 이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    //동일한 술 선택시 갯수 증가
    private void addToCart(DrinkItem newItem) {
        boolean itemExists = false;
        for (DrinkItem item : shoppingCartList) {
            if (item.getName().equals(newItem.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                Log.d("add", "addToCart: "+item.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            shoppingCartList.add(newItem);
        }
    }

    private void viewListItem(List<DrinkItem> drinkItems){
        int imageButtonIndex=0;
        for (DrinkItem item : drinkItems) {
            Log.d("MainActivity", item.toString());
            // 첫 번째 아이템의 이미지를 ImageButton에 설정
            if (item.getPic() != null) {
                byte[] imageBytes = item.getPic();
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageViews[imageButtonIndex].setImageBitmap(bitmap);
            }
            textViews[imageButtonIndex].setText(item.getName());
            tvPrices[imageButtonIndex].setText(""+item.getPrice());
            //todo:가격 넣기
            imageButtonIndex++;

        }
    }
}
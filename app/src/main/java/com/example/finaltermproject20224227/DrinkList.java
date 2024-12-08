package com.example.finaltermproject20224227;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrinkList extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain, tvCartCount;
    TextView tvPrice1, tvPrice2, tvPrice3, tvPrice4, tvPrice5, tvPrice6, tvPrice7, tvPrice8, tvPrice9;
    TextView addReviewItemNameTv;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, orderCartIb;
    EditText etName, etPrice;
    Button btnWhisky, btnVodka, btnCocktail, btnSake, btnCommunity, listAddBtn, addItemFinish, addImageBtn, selectAddCart, selectReviewTv, addReviewFinishBtn;
    ImageView addIv, addReviewItemIv;
    LinearLayout listLayout1, listLayout2, listLayout3, listLayout4, listLayout5, listLayout6, listLayout7, listLayout8, listLayout9;
    TextView[] textViews = {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain};
    TextView[] tvPrices = {tvPrice1, tvPrice2, tvPrice3, tvPrice4, tvPrice5, tvPrice6, tvPrice7, tvPrice8, tvPrice9};
    ImageView[] imageViews = {iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9};
    Button[] buttons = {btnWhisky, btnVodka, btnCocktail, btnSake};
    LinearLayout[] linearLayouts = {listLayout1, listLayout2, listLayout3, listLayout4, listLayout5, listLayout6, listLayout7, listLayout8, listLayout9};
    RadioButton rbWisky, rbVodka, rbCocktail, rbSake;
    RadioGroup rgCategory;
    RatingBar addReviewRb;
    EditText addReviewEt;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Bitmap selectedBitmap;
    DBhelper dBhelper;
    int categoryPtr = 1;
    int cartCountVal = 0;
    int radioItemCategory = 0;
    int orderNum=0;
    ArrayList<DrinkItem> shoppingCartList = new ArrayList<>();
    private static final int REQUEST_CODE_SHOPPING_CART = 2;

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
                //각 카테고리 클릭시 아이템 나오게 함.
                categoryPtr= index;
                List<DrinkItem> drinkItems = dBhelper.getDrinkItemsByKindId(categoryPtr);
                viewListItem(drinkItems);
            });
        }
        for (int i =0; i < linearLayouts.length; i++){
            int index = i; //안 적으면 리스너 안에서 i 값 사용 불가
            linearLayouts[i].setOnClickListener(view -> {
                //없는 상품 처리
                if(textViews[index].getText().toString().equals("")) {
                    Toast.makeText(this,"상품이 없습니다!",Toast.LENGTH_SHORT).show();
                    return;
                }
                final Dialog selectDialog = new Dialog(this);
                selectDialog.setContentView(R.layout.select_review_or_cart);
                selectAddCart = selectDialog.findViewById(R.id.selectAddCart);
                selectReviewTv = selectDialog.findViewById(R.id.selectReviewTv);
                DrinkItem drinkItem = new DrinkItem(
                        textViews[index].getText().toString(),
                        textViews[index].getText().toString(), // 파일 이름을 전달
                        categoryPtr,
                        Integer.parseInt(tvPrices[index].getText().toString())
                );
                int itemId = dBhelper.getDrinkItemIdByNameAndKind(textViews[index].getText().toString(), categoryPtr);
                drinkItem.setId(itemId);

                selectAddCart.setOnClickListener(view1 -> {
                    addToCart(drinkItem);
                    cartCountVal++;
                    tvCartCount.setText(""+cartCountVal);

                    selectDialog.dismiss();
                });

                selectReviewTv.setOnClickListener(view1 -> {
                    final Dialog addReviewDialog = new Dialog(this);
                    addReviewDialog.setContentView(R.layout.add_review);
                    addReviewFinishBtn = addReviewDialog.findViewById(R.id.addReviewFinishBtn);
                    addReviewRb = addReviewDialog.findViewById(R.id.addReviewRb);
                    addReviewEt = addReviewDialog.findViewById(R.id.addReviewEt);
                    addReviewItemNameTv = addReviewDialog.findViewById(R.id.addReviewItemNameTv);
                    addReviewItemIv = addReviewDialog.findViewById(R.id.addReviewItemIv);

                    addReviewItemNameTv.setText(drinkItem.getName());
                    addReviewItemIv.setImageBitmap(getImage(drinkItem.getPic()));

                    addReviewFinishBtn.setOnClickListener(view2 -> {
                        float rating = addReviewRb.getRating();

                        if(rating == 0.0 || addReviewEt.getText().toString().trim().equals(""))
                            Toast.makeText(view2.getContext(), "별점과 리뷰를 작성해 주세요.", Toast.LENGTH_SHORT).show();

                        dBhelper.addReview(addReviewEt.getText().toString(), rating, drinkItem.getId());
                        Toast.makeText(view2.getContext(), "리뷰가 작성되었습니다.", Toast.LENGTH_SHORT).show();

                        addReviewDialog.dismiss();
                    });
                    addReviewDialog.show();
                });
                selectDialog.show();
            });
        }

        //시작시 첫 술 아이템 나옴
        List<DrinkItem> firstDrinkItems = dBhelper.getDrinkItemsByKindId(1);
        viewListItem(firstDrinkItems);

        btnCommunity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Community.class);
            startActivity(intent);
        });

        orderCartIb.setOnClickListener(view -> {
            if (shoppingCartList.isEmpty()) {
                Toast.makeText(this, "상품을 선택해 주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(view.getContext(), ShoppingCart.class);
            intent.putExtra("shoppingCartList", shoppingCartList);
            intent.putExtra("orderNum", orderNum);
            startActivityForResult(intent, REQUEST_CODE_SHOPPING_CART);
        });

        listAddBtn.setOnClickListener(view -> {
            final Dialog finishDialog = new Dialog(this);
            finishDialog.setContentView(R.layout.activity_add_shopping_item);
            finishDialog.setTitle("ww");
            addItemFinish = finishDialog.findViewById(R.id.addItemFinish);
            addImageBtn = finishDialog.findViewById(R.id.addImageBtn);
            addIv = finishDialog.findViewById(R.id.addIv);
            etName = finishDialog.findViewById(R.id.etName);
            etPrice = finishDialog.findViewById(R.id.etPrice);
            rgCategory =finishDialog.findViewById(R.id.rgCategory);
            //카테고리 기본으로 보드카
            rbWisky = finishDialog.findViewById(R.id.rbWisky);
            rbWisky.setChecked(true);
            radioItemCategory =1;

            rgCategory.setOnCheckedChangeListener((radioGroup, i) -> {
                if(R.id.rbWisky == i){
                    radioItemCategory =1;
                } else if (R.id.rbVodka == i) {
                    radioItemCategory =2;
                } else if (R.id.rbCocktail == i) {
                    radioItemCategory =3;
                } else if (R.id.rbSake == i) {
                    radioItemCategory =4;
                }
            });
            finishDialog.show();
            addImageBtn.setOnClickListener(view1 -> {
                openFileChooser();
            });
            addItemFinish.setOnClickListener(view1 -> {
                Boolean isStore = saveStoreItem();
                //추가 완료시 다이어로그 끄기
                if(isStore == true){
                    finishDialog.dismiss();
                    List<DrinkItem> drinkItems = dBhelper.getDrinkItemsByKindId(categoryPtr);
                    viewListItem(drinkItems);
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // 파일 선택기 열기
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("else333", "onActivityResult: "+"image");
        if (requestCode == REQUEST_CODE_SHOPPING_CART && resultCode == RESULT_OK) {
            if (data != null && data.getSerializableExtra("shoppingCartList") != null) {
                Log.d("if22", "onActivityResult: "+"image");
                shoppingCartList = (ArrayList<DrinkItem>) data.getSerializableExtra("shoppingCartList");
                // shoppingCartList 업데이트 후 필요한 작업 수행
                int count = 0;
                for (DrinkItem drinkItem: shoppingCartList) {
                    count += drinkItem.getQuantity();
                }
                cartCountVal = count;
                tvCartCount.setText(""+cartCountVal);

            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Log.d("else22", "onActivityResult: "+"image");
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                addIv.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean saveStoreItem() {
        String imageName = etName.getText().toString().trim();
        if (isImageNameDuplicate(imageName)) {
            Toast.makeText(this, "이미 동일한 이름의 이미지가 존재합니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String savedImageName = null;
            if (selectedBitmap != null) {
                savedImageName = saveImageToLocal(selectedBitmap, imageName); // 로컬 저장소에 이미지 저장
            }
            dBhelper.addItem(savedImageName, etName.getText().toString().trim(), (long) radioItemCategory, Integer.parseInt(etPrice.getText().toString().trim()));
            Toast.makeText(this, "상품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    // 이미지를 로컬 저장소에 저장
    private String saveImageToLocal(Bitmap image, String imageName) {
        // 파일 이름에 .png 확장자가 이미 포함되어 있는지 확인
        if (!imageName.endsWith(".png")) {
            imageName += ".png";
        }
        File directory = getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName);
        // 파일 이름 중복 검사 및 고유 이름 생성
        int counter = 1;
        while (imageFile.exists()) {
            String baseName = imageName.substring(0, imageName.lastIndexOf("."));
            imageFile = new File(directory, baseName + "_" + counter + ".png");
            counter++;
        }
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile.getName(); // 파일 이름 반환
    }

    //동일한 술 선택시 갯수 증가
    private void addToCart(DrinkItem newItem) {

        boolean itemExists = false;
        for (DrinkItem item : shoppingCartList) {
            if (item.getName().equals(newItem.getName()) && item.getKindId() == newItem.getKindId()) {
                item.setQuantity(item.getQuantity() + 1);
                Log.d("add", "addToCart: "+item.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            shoppingCartList.add(newItem);
            Log.d("addItem", "addToCart: "+shoppingCartList.get(0).getId());
        }
    }

    private void viewListItem(List<DrinkItem> drinkItems) {
        int imageButtonIndex = 0;
        resetItem();
        for (DrinkItem item : drinkItems) {
            Log.d("MainActivity", item.toString());
            // 첫 번째 아이템의 이미지를 ImageButton에 설정
            if (item.getPic() != null) {
                Bitmap bitmap = getImage(item.getPic()); // 파일 이름을 사용하여 로컬 저장소에서 이미지 불러오기
                if (bitmap != null) {
                    Log.d("aa", "viewListItem: ");
                    imageViews[imageButtonIndex].setImageBitmap(bitmap);
                } else {
                    Log.e("viewListItem", "Failed to load image for item: " + item.getName());
                }
            }
            textViews[imageButtonIndex].setText(item.getName());
            tvPrices[imageButtonIndex].setText("" + item.getPrice());
            imageButtonIndex++;
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
        File directory = getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName);
        if (imageFile.exists()) {
            return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Log.e("getImage", "Image file not found: " + imageFile.getAbsolutePath());
            return null;
        }
        }
    //각 아이템 클린 초기화
    private void resetItem(){
        for (ImageView iv: imageViews)
            if (iv != null)
                iv.setImageResource(R.color.white);
        for (TextView tv: textViews)
            if (tv != null)
                tv.setText("");
        for (TextView tv: tvPrices)
            if (tv != null)
                tv.setText("");
    }

    private boolean isImageNameDuplicate(String imageName) {
        // 파일 이름에 .png 확장자가 이미 포함되어 있는지 확인
        if (!imageName.endsWith(".png")) {
            imageName += ".png";
        }

        File directory = getDir("images", Context.MODE_PRIVATE);
        File imageFile = new File(directory, imageName);
        return imageFile.exists();
    }

}
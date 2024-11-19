package com.example.finaltermproject20224227;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DrinkList extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain;
    ImageButton iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6, iBtn7, iBtn8, iBtn9, orderCartIb;
    Button btn1, btn2, btn3, btn4, btnBack, btnCommunity, listAddBtn, addItemFinish, addImageBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;
    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drink_list);
        TextView[] textViews = {tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain};
        ImageButton[] imageButtons = {iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6, iBtn7, iBtn8, iBtn9};
        int[] textViewId = {R.id.tv1, R.id.tv2,R.id.tv3,R.id.tv4,R.id.tv5,R.id.tv6,R.id.tv7,R.id.tv8,R.id.tv9};
        int[] imageButtonId = {R.id.iBtn1,R.id.iBtn2,R.id.iBtn3,R.id.iBtn4,R.id.iBtn5,R.id.iBtn6,R.id.iBtn7,R.id.iBtn8,R.id.iBtn9};
        Button[] buttons = {btn1, btn2, btn3, btn4};
        int[] buttonId = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4};
        btnBack = findViewById(R.id.btnBack);
        btnCommunity = findViewById(R.id.btnCommunity);
        orderCartIb = findViewById(R.id.orderCartIb);
        listAddBtn = findViewById(R.id.listAddBtn);
        dBhelper = new DBhelper(this);
        //초기화
        for (int i = 0; i<imageButtons.length; i++){
            textViews[i] = findViewById(textViewId[i]);
            imageButtons[i] = findViewById(imageButtonId[i]);
        }
        for (int i = 0; i < buttons.length; i++)
            buttons[i]= findViewById(buttonId[i]);

        for (int i =0; i < imageButtons.length; i++){
            imageButtons[i].setOnClickListener(view -> {
                //todo 클릭시 디테일 나오게
            });
        }
        btnBack.setOnClickListener(view -> {
            finish();
        });
        btnCommunity.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), Community.class);
            startActivity(intent);
        });

        orderCartIb.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), shoppingCart.class);
            startActivity(intent);
        });

        listAddBtn.setOnClickListener(view -> {
            final Dialog finishDialog = new Dialog(this);
            finishDialog.setContentView(R.layout.activity_add_shopping_item);
            finishDialog.setTitle("ww");
            addItemFinish = finishDialog.findViewById(R.id.addItemFinish);
            addImageBtn = finishDialog.findViewById(R.id.addImageBtn);
            addImageBtn.setOnClickListener(view1 -> {
                    openFileChooser();
            });
            addItemFinish.setOnClickListener(view1 -> {
                finish();
            });
            finishDialog.show();
        });

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                saveImageToDatabase(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImageToDatabase(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            //todo: 여러값 추가
//            dBhelper.addItem(imageBytes, "ww", 1L,200);
            Toast.makeText(this, "이미지가 데이터베이스에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
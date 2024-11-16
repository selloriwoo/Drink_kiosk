package com.example.finaltermproject20224227;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DrinkList extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvMain;
    ImageButton iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6, iBtn7, iBtn8, iBtn9;
    Button btn1, btn2, btn3, btn4, btnBack, btnCommunity;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
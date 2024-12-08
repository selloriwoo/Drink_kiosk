package com.example.finaltermproject20224227;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class ShoppingCart extends AppCompatActivity implements TextToSpeech.OnInitListener{
    ShoppingCartRecycleViewAdapter adapter;
    Button orderBtn;
    Button orderFinishBtn;
    Button orderBackBtn;
    DBhelper dbhelper;
    TextView orderFinishDate;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        // DBhelper 객체 초기화
        dbhelper = new DBhelper(this);

        orderBtn = findViewById(R.id.orderBtn);
        orderBackBtn = findViewById(R.id.orderBackBtn);
        textToSpeech = new TextToSpeech(this, this);
        Intent getIntent = getIntent();
        ArrayList<DrinkItem> shoppingCartList = (ArrayList<DrinkItem>) getIntent.getSerializableExtra("shoppingCartList");
        RecyclerView recyclerView = findViewById(R.id.cartView);
        adapter = new ShoppingCartRecycleViewAdapter(this, shoppingCartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        orderBtn.setOnClickListener(v -> {
            dbhelper.insertOrder(shoppingCartList);
            final Dialog finishDialog = new Dialog(this);
            finishDialog.setContentView(R.layout.activity_order_finish);
            orderFinishDate = finishDialog.findViewById(R.id.orderFinishDate);
            orderFinishBtn = finishDialog.findViewById(R.id.orderFinishBtn);

            LocalDate nowDate = LocalDate.now();
            int year = nowDate.getYear();
            int month = nowDate.getMonthValue();
            int day = nowDate.getDayOfMonth();

            orderFinishDate.setText(year+"/"+month+"/"+day);

            //주문 완료 사운드
            textToSpeech.speak("주문이 완료되었습니다.", TextToSpeech.QUEUE_FLUSH, null);

            orderFinishBtn.setOnClickListener(v1 -> {
                Intent intent = new Intent(this, DrinkList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            });
            finishDialog.show();
        });
        orderBackBtn.setOnClickListener(v1 -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("shoppingCartList", shoppingCartList);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onInit(int i) {
        if(i != TextToSpeech.ERROR)
            textToSpeech.setLanguage(getResources().getConfiguration().locale);
    }

    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
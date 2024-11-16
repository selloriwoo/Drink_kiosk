package com.example.finaltermproject20224227;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class shoppingCart extends AppCompatActivity {
    Button orderBtn;
    Button orderFinishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_cart);
        orderBtn = findViewById(R.id.orderBtn);

        orderBtn.setOnClickListener(v -> {
            final Dialog finishDialog = new Dialog(this);
            finishDialog.setContentView(R.layout.activity_order_finish);
            finishDialog.setTitle("ww");
            orderFinishBtn = finishDialog.findViewById(R.id.orderFinishBtn);

            orderFinishBtn.setOnClickListener(v1 -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
}
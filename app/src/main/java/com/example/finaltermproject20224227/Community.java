package com.example.finaltermproject20224227;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Community extends AppCompatActivity {
    ArrayList<Review> reviewDataArrayList = new ArrayList<>();
    MyAdapter adapter;
    Button communityBackBtn;
    DBhelper dBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_community);
        dBhelper = new DBhelper(this);
        communityBackBtn = findViewById(R.id.communityBackBtn);
        RecyclerView recyclerView = findViewById(R.id.communityRcView);

        //리뷰 불러오기
        reviewDataArrayList = dBhelper.getAllReviewsWithDrinkItemDetails();
        adapter = new MyAdapter(this, reviewDataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        communityBackBtn.setOnClickListener(view -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
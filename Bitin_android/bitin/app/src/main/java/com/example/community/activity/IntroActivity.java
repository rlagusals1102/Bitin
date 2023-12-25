package com.example.community.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;

import com.example.community.R;
import com.example.community.manager.SharedPrefManager;
import com.example.community.manager.UpbitApiManager;

public class IntroActivity extends AppCompatActivity {
    private AppCompatButton startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SharedPrefManager.initialize(getApplicationContext());
        if (!SharedPrefManager.getUserName().isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        UpbitApiManager.initialize();

        initialize();
    }

    private void initialize() {
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        });
    }
}
package com.example.community.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.community.R;
import com.example.community.manager.SharedPrefManager;
import com.example.community.manager.ToastManager;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameEditText;
    private EditText accessKeyEditText;
    private EditText secretKeyEditText;
    private CheckBox policyCheckBox;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        userNameEditText = findViewById(R.id.editTextUserName);
        accessKeyEditText = findViewById(R.id.editTextAccessKey);
        secretKeyEditText = findViewById(R.id.editTextSecretKey);
        policyCheckBox = findViewById(R.id.checkBoxPolicy);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEditText.getText().toString().trim();
                String accessKey = accessKeyEditText.getText().toString().trim();
                String secretKey = secretKeyEditText.getText().toString().trim();

                Context context = getApplicationContext();
                if (userName.length() == 0) {
                    ToastManager.showError(context, "사용자 이름을 입력해주세요.");
                    return;
                }
                if (accessKey.length() == 0) {
                    ToastManager.showError(context, "Access Key를 입력해주세요.");
                    return;
                }
                if (secretKey.length() == 0) {
                    ToastManager.showError(context, "Secret Key를 입력해주세요.");
                    return;
                }
                if (!policyCheckBox.isChecked()) {
                    ToastManager.showError(context, "약관에 동의 해주세요.");
                    return;
                }
                SharedPrefManager.initialize(context);
                SharedPrefManager.setUserName(userName);
                SharedPrefManager.setUpbitAccessApiKey(accessKey);
                SharedPrefManager.setUpbitSecretApiKey(secretKey);

                ToastManager.showSuccess(context, "모든 설정이 완료되었어요!");
                startActivity(new Intent(context, MainActivity.class));
                finish();
            }
        });
    }
}
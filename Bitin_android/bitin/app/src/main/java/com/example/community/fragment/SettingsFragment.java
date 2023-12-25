package com.example.community.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.community.R;
import com.example.community.manager.SharedPrefManager;
import com.example.community.manager.ToastManager;

public class SettingsFragment extends Fragment {
    private Context context;
    private View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_settings, container, false);
        context = contentView.getContext();

        initialize();

        return contentView;
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {
        Button resetBtn = contentView.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(view -> {
            SharedPrefManager.setUserName("");
            System.exit(0);
        });

        try {
            // set access api key
            EditText accessKeyEditText = contentView.findViewById(R.id.accessKeyEditText);
            String accessApiKey = SharedPrefManager.getUpbitAccessApiKey();
            accessKeyEditText.setText(
                    accessApiKey.substring(0, 4) +
                            new String(new char[25]).replace("\0", "*") +
                            accessApiKey.substring(accessApiKey.length() - 4, accessApiKey.length())
            );
            accessKeyEditText.setKeyListener(null);

            // set secret api key
            EditText secretKeyEditText = contentView.findViewById(R.id.secretKeyEditText);
            String secretApiKey = SharedPrefManager.getUpbitSecretApiKey();
            secretKeyEditText.setText(
                    secretApiKey.substring(0, 4) +
                            new String(new char[25]).replace("\0", "*") +
                            secretApiKey.substring(secretApiKey.length() - 4, secretApiKey.length())
            );
            secretKeyEditText.setKeyListener(null);
        }
        catch (Exception ex) {
            ToastManager.showError(context, "API Key가 잘못되었어요!");
        }
    }
}
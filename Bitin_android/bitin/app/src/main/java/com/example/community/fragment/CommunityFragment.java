package com.example.community.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.community.R;
import com.example.community.activity.SplashActivity;

public class CommunityFragment extends Fragment {
    private Context context;
    private View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_community, container, false);
        context = contentView.getContext();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startActivity(new Intent(context, SplashActivity.class));
            }
        }, 300);

        return contentView;
    }
}
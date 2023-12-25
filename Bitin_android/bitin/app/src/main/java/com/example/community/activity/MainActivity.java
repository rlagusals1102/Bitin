package com.example.community.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.community.R;
import com.example.community.databinding.ActivitySplashBinding;
import com.example.community.fragment.AssetFragment;
import com.example.community.fragment.CommunityFragment;
import com.example.community.fragment.SettingsFragment;
import com.example.community.fragment.TransactionFragment;
import com.example.community.manager.SharedPrefManager;
import com.example.community.manager.ToastManager;
import com.example.community.manager.UpbitApiManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        SharedPrefManager.initialize(getApplicationContext());
        UpbitApiManager.initialize();
        initialize();
    }

    private void initialize() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.transaction_menu) {
                    loadFragment(new TransactionFragment(), false);
                }
                else if (itemId == R.id.user_asset_menu) {
                    loadFragment(new AssetFragment(), false);
                }
                else if (itemId == R.id.community_menu) {
                    loadFragment(new CommunityFragment(), false);
                    loadFragment(new TransactionFragment(), false);
                }
                else {
                    loadFragment(new SettingsFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new TransactionFragment(), true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 101);
            }
        }
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter_fragment, R.anim.exit_fragment, R.anim.pop_enter_fragment, R.anim.pop_exit_fragment);
        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            boolean granted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (!granted) {
                ToastManager.showError(getApplicationContext(), "권한을 활성화해주세요.");
                finish();
            }
        }
    }
}

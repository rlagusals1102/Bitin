package com.example.community.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.community.R;
import com.example.community.fragment.CommunityHomeFragment;
import com.example.community.fragment.CommunityProfileFragment;
import com.example.community.fragment.CommunityPublishFragment;
import com.example.community.databinding.ActivityDrawerBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DrawerActivity extends AppCompatActivity {

    ActivityDrawerBinding binding;
    GoogleSignInAccount account;
    private BottomNavigationView topNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topNavigationView = findViewById(R.id.topNavigationView);
        frameLayout = findViewById(R.id.frame_layout);
        setupdrawer();

        topNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if(itemId == R.id.nav_home) {
                    loadFragment(new CommunityHomeFragment(), false);
                }
                else if(itemId == R.id.nav_publish) {
                    loadFragment(new CommunityPublishFragment(), false);
                }
                else if(itemId == R.id.nav_profile) {
                    loadFragment(new CommunityProfileFragment(), false);
                }

                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInitialized) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }

        fragmentTransaction.commit();
    }

    private void setupdrawer() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new CommunityHomeFragment());
        fragmentTransaction.commit();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new CommunityHomeFragment());
            fragmentTransaction.commit();
        }
        else if(item.getItemId() == R.id.nav_publish) {
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.frame_layout, new CommunityPublishFragment());
            fragmentTransaction1.commit();
        }
        else if(item.getItemId() == R.id.nav_profile) {
            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction2.replace(R.id.frame_layout, new CommunityProfileFragment());
            fragmentTransaction2.commit();
        }

        binding.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
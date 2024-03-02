package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationBarView;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.databinding.ActivityMainBinding;
import com.jawadkhansahil.grossmart.fragments.CartFragment;
import com.jawadkhansahil.grossmart.fragments.HomeFragment;
import com.jawadkhansahil.grossmart.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(binding.getRoot());


        fragmentTransition(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id  = item.getItemId();
                
                if (id == R.id.home){
                    fragmentTransition(new HomeFragment());
                } else if (id == R.id.cart) {
                    fragmentTransition(new CartFragment());
                } else if (id == R.id.profile) {
                    fragmentTransition(new ProfileFragment());
                }else {

                }
                return true;
            }
        });

    }

    public void fragmentTransition(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}
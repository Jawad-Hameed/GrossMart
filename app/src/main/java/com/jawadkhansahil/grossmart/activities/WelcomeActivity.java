package com.jawadkhansahil.grossmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.adapter.SlideViewPagerAdapter;
import com.jawadkhansahil.grossmart.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {
    public ActivityWelcomeBinding binding;
    SlideViewPagerAdapter slideViewPagerAdapter;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        SharedPreference sharedPreference = new SharedPreference(WelcomeActivity.this);
        if(sharedPreference.getString("email") != null){
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }

        slideViewPagerAdapter = new SlideViewPagerAdapter(WelcomeActivity.this);
        binding.viewPager2.setAdapter(slideViewPagerAdapter);

        binding.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SigninActivity.class));
                sharedPreference.saveString("firstOpen", "TRUE");
                finish();
            }
        });

        binding.viewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void changeIndicator(int position){
        if (position==0){
            binding.indicator1.setImageResource(R.drawable.selected);
            binding.indicator2.setImageResource(R.drawable.unselected);
            binding.indicator3.setImageResource(R.drawable.unselected);
        } else if (position == 1) {
            binding.indicator1.setImageResource(R.drawable.unselected);
            binding.indicator2.setImageResource(R.drawable.selected);
            binding.indicator3.setImageResource(R.drawable.unselected);
        }else if (position==2){
            binding.indicator1.setImageResource(R.drawable.unselected);
            binding.indicator2.setImageResource(R.drawable.unselected);
            binding.indicator3.setImageResource(R.drawable.selected);
        }
    }
}
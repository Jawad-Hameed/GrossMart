package com.jawadkhansahil.grossmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.card.MaterialCardView;
import com.jawadkhansahil.grossmart.R;

public class SlideViewPagerAdapter extends PagerAdapter {

    Context context;
    public SlideViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onbord_layout, container, false);

        ImageView sliderImage = view.findViewById(R.id.silderImage);
        TextView sliderHeading = view.findViewById(R.id.sliderHeading);
        TextView sliderDescription = view.findViewById(R.id.sliderDescription);

        int slideImages[] = {
                R.drawable.choose_products,
                R.drawable.buy_grocery,
                R.drawable.delivery_man
        };
        String headings[] = {
                "Choose from our best menu",
                "Find your nearby grocery store",
                "Quick Delivery at your DoorStep"
        };


        sliderImage.setImageResource(slideImages[position]);
        sliderHeading.setText(headings[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

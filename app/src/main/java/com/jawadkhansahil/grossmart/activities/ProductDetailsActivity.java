package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.databinding.ActivityProductDetailsBinding;
import com.jawadkhansahil.grossmart.models.ProductModel;

public class ProductDetailsActivity extends AppCompatActivity {

    String productID, categoryID;
    FirebaseFirestore db;
    ActivityProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(binding.getRoot());

        productID = getIntent().getStringExtra("productID");
        categoryID = getIntent().getStringExtra("categoryID");

        db = FirebaseFirestore.getInstance();

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quan = binding.quantityLarge.getText().toString();
                int quantity = Integer.parseInt(quan);
                quantity++;
                binding.quantityLarge.setText(String.valueOf(quantity));
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.markImage.setImageResource(R.drawable.mark);
                binding.addedToCart.setText("Added to Cart");
                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        binding.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quan = binding.quantityLarge.getText().toString();
                int quantity = Integer.parseInt(quan);
                if (quantity > 1){
                    quantity--;
                    binding.quantityLarge.setText(String.valueOf(quantity));
                }
            }
        });
        db.collection("Category").document(categoryID).collection("Products").document(productID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    ProductModel productModel = value.toObject(ProductModel.class);
                    Glide.with(ProductDetailsActivity.this).load(productModel.getProductImage()).into(binding.productImageLarge);
                    binding.productNameLarge.setText(productModel.getProductName());
                    binding.productDescriptionLarge.setText(productModel.getProductDescription());
                    binding.productCaloriesLarge.setText("\uD83D\uDD25 "+productModel.getProductCalories()+" calories");
                    binding.deliveryTimeLarge.setText("⏰ "+productModel.getDeliveryTime());
                    binding.productPriceLarge.setText(productModel.getProductPrice());
                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Kuch ni mila", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
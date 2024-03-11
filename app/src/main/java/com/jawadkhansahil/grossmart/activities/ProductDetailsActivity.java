package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.databinding.ActivityProductDetailsBinding;
import com.jawadkhansahil.grossmart.models.ProductModel;

public class ProductDetailsActivity extends AppCompatActivity {

    String productID, categoryID;
    FirebaseFirestore db;
    ActivityProductDetailsBinding binding;
    ProductModel productModel;
    String getProductQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(binding.getRoot());

        productID = getIntent().getStringExtra("productID");
        categoryID = getIntent().getStringExtra("categoryID");

        db = FirebaseFirestore.getInstance();


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.addedToCart.getText().toString().equals("Go to Cart")){
                    startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
                    finish();
                }else {
                    SharedPreference sharedPreference = new SharedPreference(ProductDetailsActivity.this);
                    ProductModel model = new ProductModel(productID, productModel.getProductImage(), productModel.getProductName(), productModel.getProductQuantity(), productModel.getProductPrice(), productModel.getProductCalories(), productModel.getProductDescription(), productModel.getDeliveryTime(), productModel.getProductUnit());
                    db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productID).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            binding.markImage.setImageResource(R.drawable.mark);
                            binding.addedToCart.setText("Go to Cart");
                        }
                    });
                    Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        db.collection("Category").document(categoryID).collection("Products").document(productID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    productModel = value.toObject(ProductModel.class);
                    Glide.with(ProductDetailsActivity.this).load(productModel.getProductImage()).into(binding.productImageLarge);
                    binding.productNameLarge.setText(productModel.getProductName());
                    binding.productDescriptionLarge.setText(productModel.getProductDescription());
                    binding.productCaloriesLarge.setText("\uD83D\uDD25 "+productModel.getProductCalories()+" calories");
                    binding.deliveryTimeLarge.setText("⏰ "+productModel.getDeliveryTime());
                    binding.productPriceLarge.setText(productModel.getProductPrice()+ " Rs");

                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Kuch ni mila", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.jawadkhansahil.grossmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.adapter.CartAdapater;
import com.jawadkhansahil.grossmart.adapter.CategoryAdapter;
import com.jawadkhansahil.grossmart.adapter.ProductAdapter;
import com.jawadkhansahil.grossmart.databinding.ActivityCartBinding;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    ArrayList<ProductModel> productModelArrayList;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding =ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CartAdapater cartAdapater = new CartAdapater(CartActivity.this, productModelArrayList);
        binding.cartRecyclerView.setAdapter(cartAdapater);

        SharedPreference sharedPreference = new SharedPreference(CartActivity.this);
        db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){
                    ProductModel model = snapshot.toObject(ProductModel.class);
                    productModelArrayList.add(model);
                }
                binding.cartItems.setText(productModelArrayList.size()+"");
                cartAdapater.notifyDataSetChanged();
            }
        });
    }
}
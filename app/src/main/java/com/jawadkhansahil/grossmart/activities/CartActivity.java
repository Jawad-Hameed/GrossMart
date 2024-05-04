package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawadkhansahil.grossmart.DeleteClickListener;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.adapter.CartAdapater;
import com.jawadkhansahil.grossmart.adapter.CategoryAdapter;
import com.jawadkhansahil.grossmart.adapter.ProductAdapter;
import com.jawadkhansahil.grossmart.databinding.ActivityCartBinding;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements DeleteClickListener {

    ActivityCartBinding binding;
    ArrayList<ProductModel> productModelArrayList;
    CartAdapater cartAdapater;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productModelArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cartAdapater = new CartAdapater(CartActivity.this, productModelArrayList, this);
        binding.cartRecyclerView.setAdapter(cartAdapater);

        updateItemCount();
        SharedPreference sharedPreference = new SharedPreference(CartActivity.this);
        db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Handle error
                    return;
                }

                // Clearing the list is unnecessary since we're updating it anyway
                // productModelArrayList.clear(); // Remove this line
                productModelArrayList.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    ProductModel model = snapshot.toObject(ProductModel.class);
                    productModelArrayList.add(model);
                }

                // Calculate and display total price
                binding.cartTotalPrice.setText(String.valueOf(calculateTotalPrice(productModelArrayList)));
                updateItemCount(); // Update UI with new item count
                cartAdapater.notifyDataSetChanged();
            }


        });
    }


    public double calculateTotalPrice(ArrayList<ProductModel> productList) {
        double totalPrice = 0.0;

        for (ProductModel product : productList) {
            // Parse quantity and price to double
            double quantity = Double.parseDouble(product.getProductQuantity());
            double price = Double.parseDouble(product.getProductPrice());

            // Calculate subtotal for each product and add to total price
            totalPrice += quantity * price;
        }

        return totalPrice;
    }
    private void updateItemCount() {
        int itemCount = cartAdapater.getItemCount();
        binding.cartItems.setText(String.valueOf(itemCount));
        cartAdapater.notifyDataSetChanged();
    }
    @Override
    public void deleteItem() {
        updateItemCount();
    }
}
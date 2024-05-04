package com.jawadkhansahil.grossmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawadkhansahil.grossmart.DeleteClickListener;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.activities.CartActivity;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class CartAdapater extends RecyclerView.Adapter<CartAdapater.CartViewHolder> {

    Context context;
    ArrayList<ProductModel> productModelArrayList;
    DeleteClickListener listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CartAdapater(Context context, ArrayList<ProductModel> productModelArrayList, DeleteClickListener listener) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapater.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapater.CartViewHolder holder, int position) {
        final int[] quantity = new int[1];
        ProductModel productModel = productModelArrayList.get(position);
        quantity[0] = Integer.parseInt(productModel.getProductQuantity());
        holder.productName.setText(productModel.getProductName());
        Glide.with(context).load(productModel.getProductImage()).into(holder.productImage);
        holder.productPrice.setText(productModel.getProductPrice()+ " Rs");
        holder.productCount.setText(quantity[0] + " " +productModel.getProductUnit());
        SharedPreference sharedPreference = new SharedPreference(context);

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0]++;
                db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productModel.getProductID()).update("productQuantity", String.valueOf(quantity[0]))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                double price = quantity[0]*Double.parseDouble(productModel.getProductPrice());
                                db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productModel.getProductID()).update("productPrice", String.valueOf(price)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });
                                holder.productCount.setText(String.valueOf(quantity[0]) + " " + productModel.getProductUnit());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                            }
                        });
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productModel.getProductID()).update("productQuantity", String.valueOf(quantity[0]))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    double price = quantity[0]*Double.parseDouble(productModel.getProductPrice());
                                    db.collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productModel.getProductID()).update("productPrice", String.valueOf(price)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                    holder.productCount.setText(String.valueOf(quantity[0]) + " " + productModel.getProductUnit());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                }
                            });
                }
            }
        });


        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference sharedPreference = new SharedPreference(context);
                FirebaseFirestore.getInstance().collection("Users").document(sharedPreference.getString("email")).collection("UserCart").document(productModel.getProductID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Removed Successfully", Toast.LENGTH_SHORT).show();
                        listener.deleteItem();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size(); // This should return the actual size of the dataset
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {


        CardView addBtn, minusBtn;
        TextView productName, productCount, productPrice;
        ImageView productImage, deleteItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            addBtn = itemView.findViewById(R.id.addButton);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            minusBtn = itemView.findViewById(R.id.minusButton);
            productName = itemView.findViewById(R.id.cartProductName);
            productCount = itemView.findViewById(R.id.cardProductWeight);
            productPrice = itemView.findViewById(R.id.cartProductPrice);
            productImage = itemView.findViewById(R.id.cartProductImage);
        }
    }
}

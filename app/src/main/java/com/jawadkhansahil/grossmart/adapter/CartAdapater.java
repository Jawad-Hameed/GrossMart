package com.jawadkhansahil.grossmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class CartAdapater extends RecyclerView.Adapter<CartAdapater.CartViewHolder> {

    Context context;
    ArrayList<ProductModel> productModelArrayList;

    public CartAdapater(Context context, ArrayList<ProductModel> productModelArrayList) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
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
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0]++;
                holder.productCount.setText(quantity[0] +" " +productModel.getProductUnit());
            }
        });

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1){
                    quantity[0]--;
                    holder.productCount.setText(quantity[0] +" " +productModel.getProductUnit());
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

                        productModelArrayList.remove(holder.getAdapterPosition());

                        // Notify adapter about the item removal
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
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

package com.jawadkhansahil.grossmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.activities.ProductDetailsActivity;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    ArrayList<ProductModel> productModelArrayList;
    String categoeyID;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModelArrayList, String id) {
        this.context = context;
        this.categoeyID = id;
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);

        Glide.with(context).load(productModel.getProductImage()).into(holder.productImage);
        holder.productName.setText(productModel.getProductName());
        holder.productPrice.setText(productModel.getProductPrice());
        holder.productWeight.setText(productModel.getProductWeight());

        holder.productClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productID", productModel.getProductID());
                intent.putExtra("categoryID", categoeyID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productWeight, productPrice;
        ImageView productImage;
        CardView productClick;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productClick = itemView.findViewById(R.id.cardProduct);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productWeight = itemView.findViewById(R.id.productWeight);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}

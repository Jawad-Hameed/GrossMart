package com.jawadkhansahil.grossmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.models.CategoryModel;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewModel> {
    Context context;
    ArrayList<CategoryModel> categoryModelArrayList;
    FirebaseFirestore db;
    RecyclerView productList;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList, RecyclerView productList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
        this.productList = productList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new CategoryViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewModel holder, int position) {
        CategoryModel categoryModel = categoryModelArrayList.get(position);
        Glide.with(context).load(categoryModel.getImage()).into(holder.categoryImage);
        holder.categoryName.setText(categoryModel.getCategory());

        getProducts(categoryModelArrayList.get(0).getId());

        holder.fruitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProducts(categoryModel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public void getProducts(String id) {
        ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        db.collection("Category").document(id).collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    ProductModel productModel = documentSnapshot.toObject(ProductModel.class);
                    productModelArrayList.add(productModel);
                }

                ProductAdapter productAdapter = new ProductAdapter(context, productModelArrayList, id);
                productList.setAdapter(productAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public class CategoryViewModel extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        CardView fruitCard;

        public CategoryViewModel(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);
            fruitCard = itemView.findViewById(R.id.fruitCard);
        }
    }
}

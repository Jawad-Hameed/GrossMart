package com.jawadkhansahil.grossmart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.adapter.CategoryAdapter;
import com.jawadkhansahil.grossmart.adapter.ProductAdapter;
import com.jawadkhansahil.grossmart.databinding.FragmentHomeBinding;
import com.jawadkhansahil.grossmart.models.CategoryModel;
import com.jawadkhansahil.grossmart.models.ProductModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore db;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<ProductModel> productModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        db = FirebaseFirestore.getInstance();
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        categoryModelArrayList = new ArrayList<>();
        productModelArrayList = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.imageone, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.imagetwo, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.imagethree, ScaleTypes.FIT));

        binding.imageSlider.setImageList(slideModels);

        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categoryModelArrayList, binding.productList);
        binding.categoryList.setAdapter(categoryAdapter);

        db.collection("Category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){
                    CategoryModel categoryModel = documentSnapshot.toObject(CategoryModel.class);
                    categoryModelArrayList.add(categoryModel);
                }

                categoryAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return binding.getRoot();
    }
}
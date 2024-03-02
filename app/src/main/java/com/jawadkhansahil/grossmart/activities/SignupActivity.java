package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawadkhansahil.grossmart.dialogs.LoadingDialog;
import com.jawadkhansahil.grossmart.databinding.ActivitySignupBinding;
import com.jawadkhansahil.grossmart.models.UserModel;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(SignupActivity.this);
        loadingDialog.setCancelable(false);

        binding.gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.INSTANCE.animateZoom(SignupActivity.this);
            }
        });

        binding.buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameInput.getText().toString();
                String email = binding.emailInput.getText().toString();
                String phone =  binding.countryCodePicker.getSelectedCountryCode() + binding.phoneInput.getText().toString();
                String password = binding.passwordInput.getText().toString();


                if (!name.isEmpty()){
                    if (!email.isEmpty() && email.contains("@")) {
                        if (!phone.isEmpty()){
                            if (!password.isEmpty()){
                                loadingDialog.show();
                                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        UserModel userModel = new UserModel(name, email, phone, password, "NOT_VERIFIED");
                                        db.collection("Users").document(email).set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                loadingDialog.dismiss();
                                                Toast.makeText(SignupActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignupActivity.this, VerifyEmailActivity.class);
                                                intent.putExtra("email", email);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingDialog.dismiss();
                                                Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(SignupActivity.this, "Please create your password", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(SignupActivity.this, "Please type your phone number", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignupActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignupActivity.this, "Please type your name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawadkhansahil.grossmart.dialogs.ForgetPasswordDialog;
import com.jawadkhansahil.grossmart.dialogs.LoadingDialog;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.databinding.ActivitySigninBinding;
import com.jawadkhansahil.grossmart.models.UserModel;

public class SigninActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(SigninActivity.this);
        loadingDialog.setCancelable(false);

        binding.gotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });

        binding.foregetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordDialog forgetPasswordDialog = new ForgetPasswordDialog(SigninActivity.this);
                forgetPasswordDialog.setCancelable(true);
                forgetPasswordDialog.show();
            }
        });

        binding.buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailInput.getText().toString();
                String password = binding.passwordInput.getText().toString();
                if (!email.isEmpty()) {
                    if (!password.isEmpty()) {
                        loadingDialog.show();
                        db.collection("Users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserModel userModel = documentSnapshot.toObject(UserModel.class);
                                if (userModel != null) {
                                    if (userModel.getStatus().equals("VERIFIED")) {
                                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                loadingDialog.dismiss();
                                                db.collection("Users").document(email).update("password",password);
                                                SharedPreference sharedPreference = new SharedPreference(SigninActivity.this);
                                                sharedPreference.saveString("email", email);
                                                startActivity(new Intent(SigninActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                loadingDialog.dismiss();
                                                Toast.makeText(SigninActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        loadingDialog.dismiss();
                                        Intent intent = new Intent(SigninActivity.this, VerifyEmailActivity.class);
                                        intent.putExtra("email", email);
                                        startActivity(intent);
                                        Toast.makeText(SigninActivity.this, "Please Verify Your Email to Login", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    loadingDialog.dismiss();
                                    Toast.makeText(SigninActivity.this, "Account not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingDialog.dismiss();
                                Toast.makeText(SigninActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(SigninActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SigninActivity.this, "Please enter you email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
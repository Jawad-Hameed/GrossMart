package com.jawadkhansahil.grossmart.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jawadkhansahil.grossmart.R;
import com.jawadkhansahil.grossmart.SharedPreference;
import com.jawadkhansahil.grossmart.databinding.ActivityVerifyEmailBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VerifyEmailActivity extends AppCompatActivity {
    ActivityVerifyEmailBinding binding;
    int otpCode;
    String email;
    int selectedETPosition = 0;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");
        db = FirebaseFirestore.getInstance();

        sendVerifyEmail();

        binding.textView6.setText("Enter the code we just send you on your \nemail address " + email);


        binding.ET1.addTextChangedListener(textWatcher);
        binding.ET2.addTextChangedListener(textWatcher);
        binding.ET3.addTextChangedListener(textWatcher);
        binding.ET4.addTextChangedListener(textWatcher);

        showKeyBoard(binding.ET1);
        binding.ET1.setBackground(getDrawable(R.drawable.button_gradient));
        binding.ET2.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        binding.ET3.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        binding.ET4.setBackground(getDrawable(R.drawable.background_gradient_unselected));

        binding.buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifyEmail();
                int resendTime = 60;
                new CountDownTimer(resendTime * 1000, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        binding.buttonResend.setEnabled(false);
                        binding.buttonResend.setTextColor(Color.GRAY);
                        binding.buttonResend.setText("Resend Code (" +(millisUntilFinished / 1000)+ ")");
                    }

                    @Override
                    public void onFinish() {
                        binding.buttonResend.setEnabled(true);
                        binding.buttonResend.setTextColor(getResources().getColor(R.color.red));
                        binding.buttonResend.setText("Resend");
                    }
                }.start();
            }
        });
        binding.buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String otpVerificationCode = binding.ET1.getText().toString()+
                                       binding.ET2.getText().toString()+
                                       binding.ET3.getText().toString()+
                                       binding.ET4.getText().toString();

                if (otpVerificationCode.length() == 4){
                    if (Integer.parseInt(otpVerificationCode) == otpCode){
                        startActivity(new Intent(VerifyEmailActivity.this, MainActivity.class));
                        SharedPreference sharedPreference = new SharedPreference(VerifyEmailActivity.this);
                        sharedPreference.saveString("email", email);
                        db.collection("Users").document(email).update("status", "VERIFIED");
                        finishAffinity();
                    }else {
                        Toast.makeText(VerifyEmailActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void sendVerifyEmail(){
        Random random = new Random();
        otpCode = random.nextInt(8999)+1000;
        String url = "https://kinemastermodapk.tech/otp/phpmailer/test.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("code", String.valueOf(otpCode));
                return  params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void showKeyBoard(EditText editText){
        editText.requestFocus();
        binding.ET2.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        binding.ET1.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        binding.ET3.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        binding.ET4.setBackground(getDrawable(R.drawable.background_gradient_unselected));
        InputMethodManager inputMethodManager = (InputMethodManager) VerifyEmailActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.setBackground(getDrawable(R.drawable.button_gradient));

    }

    public void CountDownTimer(){

    }
    public final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String otpVerificationCode = binding.ET1.getText().toString()+
                    binding.ET2.getText().toString()+
                    binding.ET3.getText().toString()+
                    binding.ET4.getText().toString();

            if (otpVerificationCode.length()<4){
                binding.buttonVerify.setBackground(getDrawable(R.drawable.background_gradient_unselected));
                binding.buttonVerify.setEnabled(false);
            }else {
                binding.buttonVerify.setBackground(getDrawable(R.drawable.button_gradient));
                binding.buttonVerify.setEnabled(true);
            }

            if (otpVerificationCode.length() == 4){
                if (Integer.parseInt(otpVerificationCode) == otpCode){
                    SharedPreference sharedPreference = new SharedPreference(VerifyEmailActivity.this);
                    sharedPreference.saveString("email", email);
                    db.collection("Users").document(email).update("status", "VERIFIED");
                    startActivity(new Intent(VerifyEmailActivity.this, MainActivity.class));
                    finishAffinity();
                }else {
                    Toast.makeText(VerifyEmailActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()>0){
                if (selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyBoard(binding.ET2);
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2;
                    showKeyBoard(binding.ET3);
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3;
                    showKeyBoard(binding.ET4);
                }else{
                    binding.buttonVerify.setEnabled(true);
                    binding.buttonVerify.setBackground(getDrawable(R.drawable.button_gradient));
                    binding.ET4.setBackground(getDrawable(R.drawable.background_gradient_unselected));
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL){

            if (selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyBoard(binding.ET3);
            } else if (selectedETPosition == 2) {
                selectedETPosition = 1;
                showKeyBoard(binding.ET2);
            } else if (selectedETPosition == 1) {
                selectedETPosition = 0;
                showKeyBoard(binding.ET1);
            }
            return true;
        }else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
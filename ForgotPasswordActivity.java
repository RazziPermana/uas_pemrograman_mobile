package com.example.uastest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextVerificationCode;
    private Button btnVerify;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        btnVerify = findViewById(R.id.btnVerify);

        dbHelper = new DBHelper(this);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCodeAndEmail();
            }
        });
    }

    private void verifyCodeAndEmail() {
        String email = editTextEmail.getText().toString().trim();
        String verificationCode = editTextVerificationCode.getText().toString().trim();

        if (isValidEmail(email) && isValidVerificationCode(verificationCode)) {
            User user = dbHelper.getUserByEmail(email);

            if (user != null) {
                if (verificationCode.equals("12345")) {
                    UserCacheUtil.setCachedUser(user);
                    startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                    finish();
                } else {
                    showToast("Kode verifikasi Anda salah. Silakan ulangi prosedur dari awal.");
                }
            } else {
                showToast("Email Anda tidak terdaftar. Silakan register ulang.");
            }
        } else {
            showToast("Email atau kode verifikasi tidak benar.");
        }
    }

    private boolean isValidEmail(String email) {
        User user = dbHelper.getUserByEmail(email);
        return user != null;
    }

    private boolean isValidVerificationCode(String verificationCode) {
        return "12345".equals(verificationCode);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

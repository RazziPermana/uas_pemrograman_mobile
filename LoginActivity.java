package com.example.uastest;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        User loggedInUser = dbHelper.getUserByEmailAndPassword(email, password);
        if (loggedInUser != null) {
            // Login berhasil
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();

            // Menyimpan informasi pengguna ke dalam cache
            UserCacheUtil.setCachedUser(loggedInUser);

            // Navigasi ke aktivitas utama atau halaman lainnya
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Jika ingin menutup activity login setelah login berhasil
        } else {
            // Login gagal
            Toast.makeText(this, "Login Gagal. Periksa email dan password Anda.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void goToForgotPassword(View view) {
        String phoneNumber = "1234567";
        String verificationCode = "12345";
        sendVerificationCode(phoneNumber, verificationCode);
        Toast.makeText(this, "Cek Pesan Anda", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    private void sendVerificationCode(String phoneNumber, String verificationCode) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, "Kode verifikasi Anda: " + verificationCode, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

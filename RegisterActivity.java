package com.example.uastest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextNama, editTextAngkatan, editTextEmail, editTextPassword;
    private Button btnRegister;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        editTextNama = findViewById(R.id.editTextNama);
        editTextAngkatan = findViewById(R.id.editTextAngkatan);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });
    }

    private void register(View view) {
        String nama = editTextNama.getText().toString().trim();
        String angkatan = editTextAngkatan.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        long result = dbHelper.addUserDetail(nama, angkatan, email, password);

        if (result != -1) {
            Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
            goToLogin(view);
        } else {
            Toast.makeText(this, "Registrasi Gagal. email sudah digunakan data Anda.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}

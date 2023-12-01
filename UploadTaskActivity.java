package com.example.uastest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadTaskActivity extends AppCompatActivity implements SelectTypeDialogFragment.OnOptionDialogListener {

    private EditText editTextTaskDescription;
    private EditText editTextTaskTitle;
    private Button btnUploadTask;
    private Button btnPilihJenis;
    private DBHelper dbHelper;
    private String selectedType; // Menambahkan variabel untuk menyimpan jenis tugas yang dipilih
    private String selectedLogoUrl;

    private String getCurrentTimestamp() {
        // Mendapatkan waktu sekarang
        Date now = new Date();

        // Mengonversi waktu ke format timestamp yang diinginkan
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(now);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_task);
        dbHelper = new DBHelper(this);

        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        btnUploadTask = findViewById(R.id.btnUploadTask);
        btnPilihJenis = findViewById(R.id.btnPilihJenis);

        btnUploadTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan deskripsi tugas
                String taskDescription = editTextTaskDescription.getText().toString();
                String taskTimestamp = getCurrentTimestamp();
                String taskTitle = editTextTaskTitle.getText().toString();

                // Mendapatkan user ID dari cache atau sumber lainnya
                int userId = UserCacheUtil.getCachedUser().getId();

                // Memastikan deskripsi tugas tidak kosong sebelum menyimpan ke database
                if (!taskDescription.isEmpty() && selectedType != null && selectedLogoUrl != null && !selectedLogoUrl.isEmpty()) {
                    // Menyimpan tugas ke database
                    long result = dbHelper.addTask(taskDescription, taskTimestamp, userId, taskTitle, selectedType, selectedLogoUrl);

                    // Memeriksa apakah penyimpanan berhasil
                    if (result != -1) {
                        // Jika berhasil, memberikan umpan balik kepada pengguna atau melakukan operasi lainnya
                        Toast.makeText(UploadTaskActivity.this, "Tugas diunggah: " + taskTitle, Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        // Jika gagal, memberikan umpan balik atau melakukan penanganan kesalahan
                        Toast.makeText(UploadTaskActivity.this, "Gagal mengunggah tugas. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Menampilkan pesan jika deskripsi tugas kosong
                    Toast.makeText(UploadTaskActivity.this, "Tolong isi Deskripsi, Jenis, dan Judul tugas Anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPilihJenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTypeDialogFragment selectTypeDialogFragment = new SelectTypeDialogFragment();
                selectTypeDialogFragment.setOnOptionDialogListener(UploadTaskActivity.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                selectTypeDialogFragment.show(fragmentManager, SelectTypeDialogFragment.class.getSimpleName());
            }
        });
    }

    @Override
    public void onOptionChoosen(String selectedType, String selectedLogoUrl) {
        this.selectedType = selectedType;
        this.selectedLogoUrl = selectedLogoUrl;
        Toast.makeText(this, "Jenis Tugas: " + selectedType, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(UploadTaskActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


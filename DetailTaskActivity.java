package com.example.uastest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailTaskActivity extends AppCompatActivity {
    private TextView textViewTaskDescription;
    private TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTaskDescription = findViewById(R.id.textViewTaskDescription);
        // Inisialisasi elemen UI lainnya jika diperlukan

        // Dapatkan data tugas dari intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("task_description") && intent.hasExtra("task_title"))  {
            String taskTitle = intent.getStringExtra("task_title");
            String taskDescription = intent.getStringExtra("task_description");
            textViewTitle.setText(taskTitle);
            textViewTaskDescription.setText(taskDescription);
            // Set elemen UI lainnya sesuai data tugas
        }
    }
}
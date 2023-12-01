package com.example.uastest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity {
    private EditText editTextDescription;
    private EditText editTextTitle;
    private Button btnSaveEdit;

    private DBHelper dbHelper;
    private int taskId;
    private User loggedInUser;
    private String selectedtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTitle = findViewById(R.id.editTextTitle);
        btnSaveEdit = findViewById(R.id.btnSaveEdit);

        dbHelper = new DBHelper(this);
        loggedInUser = UserCacheUtil.getCachedUser();

        // Mendapatkan ID tugas dari Intent
        taskId = getIntent().getIntExtra("task_id", -1);

        // Mengisi formulir dengan informasi tugas yang ada
        Task existingTask = findTaskById(taskId);

        if (existingTask != null) {
            editTextTitle.setText(existingTask.getTitle());
            editTextDescription.setText(existingTask.getDescription());
            selectedtype = existingTask.getSelectedType(); // Mendapatkan selectedType
        }

        // Menambahkan listener untuk tombol simpan
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mendapatkan nilai dari formulir
                String newTitle = editTextTitle.getText().toString().trim();
                String newDescription = editTextDescription.getText().toString().trim();

                // Memperbarui tugas di database
                dbHelper.updateTask(taskId, newDescription, existingTask.getTimestamp(), loggedInUser.getId(), newTitle, selectedtype, existingTask.getLinkUrl());


                // Tutup activity setelah menyimpan perubahan
                finish();
            }
        });
    }

    // Metode untuk mencari tugas berdasarkan ID
    private Task findTaskById(int taskId) {
        List<Task> userTasks = dbHelper.getTasksForUser(loggedInUser.getId());

        for (Task task : userTasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
}

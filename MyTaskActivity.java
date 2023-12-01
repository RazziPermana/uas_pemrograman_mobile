package com.example.uastest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyTaskActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMyTasks;
    private TaskAdapter myTaskAdapter;
    private List<Task> myTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);

        recyclerViewMyTasks = findViewById(R.id.recyclerViewMyTasks);
        recyclerViewMyTasks.setLayoutManager(new LinearLayoutManager(this));

        myTaskList = new ArrayList<>();
        myTaskAdapter = new TaskAdapter(myTaskList, this, true);
        recyclerViewMyTasks.setAdapter(myTaskAdapter);

        // Mendapatkan pengguna yang saat ini login
        User loggedInUser = UserCacheUtil.getCachedUser();

        // Implementasikan logika untuk mendapatkan dan menampilkan tugas pengguna yang login
        // Anda mungkin perlu menyesuaikan ini dengan metode di DBHelper atau sumber data lainnya
        // Contoh:
        DBHelper dbHelper = new DBHelper(this);
        myTaskList = dbHelper.getTasksForUser(loggedInUser.getId());

        // Menetapkan daftar tugas ke adapter
        myTaskAdapter.setTaskList(myTaskList);
        myTaskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyTaskActivity.this);
                builder.setMessage("Apakah Anda yakin ingin menghapus tugas ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Task clickedTask = myTaskList.get(position);

                        // Panggil metode deleteTask di DBHelper
                        dbHelper.deleteTask(clickedTask.getId());

                        // Hapus tugas dari myTaskList dan perbarui tampilan RecyclerView
                        myTaskList.remove(position);
                        myTaskAdapter.notifyItemRemoved(position);
                        myTaskAdapter.notifyItemRangeChanged(position, myTaskList.size());
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Batalkan penghapusan jika pengguna menekan "Tidak"
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyTaskActivity.this);
                builder.setMessage("Apakah Anda yakin ingin mengedit tugas ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Buka EditTaskActivity jika pengguna menekan "Ya"
                        Task clickedTask = myTaskList.get(position);
                        Intent editIntent = new Intent(MyTaskActivity.this, EditTaskActivity.class);
                        editIntent.putExtra("task_id", clickedTask.getId());
                        startActivity(editIntent);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Batalkan pengeditan jika pengguna menekan "Tidak"
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    protected void onResume() {
        super.onResume();
        // Update task list each time the activity becomes active
        updateTaskList();
    }

    private void updateTaskList() {
        // Refresh the task list from the database
        User loggedInUser = UserCacheUtil.getCachedUser();
        DBHelper dbHelper = new DBHelper(this);
        myTaskList = dbHelper.getTasksForUser(loggedInUser.getId());
        myTaskAdapter.setTaskList(myTaskList);
    }
}


package com.example.uastest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private TextView textViewWelcome;
    private ImageView btnMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private List<Task> taskList;

    private void displayWelcomeMessage() {
        User cachedUser = UserCacheUtil.getCachedUser();
        if (cachedUser != null) {
            String welcomeMessage = "Welcome, " + cachedUser.getNama();
            textViewWelcome.setText(welcomeMessage);
        }
    }

    private void updateTaskList() {
        DBHelper dbHelper = new DBHelper(this);

        List<Task> tasksFromDb = dbHelper.getAllTasks();
        taskList.clear();
        taskList.addAll(tasksFromDb);
        taskAdapter.notifyDataSetChanged();
    }

    private void initializeUI() {
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        textViewWelcome = findViewById(R.id.textViewWelcome);
        btnMenu = findViewById(R.id.btnMenu);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        // Menginisialisasi adapter dan mengaturnya ke RecyclerView
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this, false);
        recyclerViewTasks.setAdapter(taskAdapter);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item-menu yang dipilih di sini
                int itemId = item.getItemId();
                if (itemId == R.id.menu_upload) {
                    Intent uploadIntent = new Intent(MainActivity.this, UploadTaskActivity.class);
                    startActivity(uploadIntent);
                } else if (itemId == R.id.menu_my_task) {
                    Intent myTaskIntent = new Intent(MainActivity.this, MyTaskActivity.class);
                    startActivity(myTaskIntent);
                } else if (itemId == R.id.menu_logout) {
                    logoutUser();
                }
                // Tutup drawer setelah item dipilih
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Buka drawer saat tombol menu diklik
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        displayWelcomeMessage();

    }
    private void logoutUser() {
        // Hapus cache pengguna
        UserCacheUtil.clearCachedUser();

        // Pindah ke LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // Hapus MainActivity dari tumpukan aktivitas
        finish();
    }

    protected void onResume() {
        super.onResume();
        // Update task list each time the activity becomes active
        updateTaskList();
    }
}

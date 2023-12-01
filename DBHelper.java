package com.example.uastest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 7;

    // Tabel User
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_ANGKATAN = "angkatan";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Tabel Task
    private static final String TABLE_TASK = "task";
    private static final String COLUMN_TASK_ID = "task_id";
    private static final String COLUMN_TASK_DESCRIPTION = "description";
    private static final String COLUMN_TASK_TIMESTAMP = "timestamp";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_TASK_USER_ID = "user_id";
    private static final String COLUMN_SELECTED_TYPE ="selectedType";
    private static final String COLUMN_LINK_URL = "linkUrl";



    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMA + " TEXT, "
            + COLUMN_ANGKATAN + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK + " ("
            + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_DESCRIPTION + " TEXT, "
            + COLUMN_TASK_TIMESTAMP + " TEXT, "
            + COLUMN_LINK_URL + " TEXT, "  // Tambahkan kolom linkUrl
            + COLUMN_TASK_USER_ID + " INTEGER, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_SELECTED_TYPE + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + "))";

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    public long addUserDetail(String nama, String angkatan, String email, String password) {
        if (isEmailExists(email)) {
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_ANGKATAN, angkatan);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USER, null, values);

    }
    private boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_EMAIL};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);

        boolean emailExists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }

        return emailExists;
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASK, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

    public int updateTask(int taskId, String description, String timestamp, int userId, String title, String selectedType, String linkUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_TIMESTAMP, timestamp);
        values.put(COLUMN_TASK_USER_ID, userId);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SELECTED_TYPE, selectedType);
        values.put(COLUMN_LINK_URL, linkUrl);

        return db.update(TABLE_TASK, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

//    public int updateUser(int id, String nama, String angkatan, String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAMA, nama);
//        values.put(COLUMN_ANGKATAN, angkatan);
//        values.put(COLUMN_EMAIL, email);
//        values.put(COLUMN_PASSWORD, password);
//        return db.update(TABLE_USER, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
//    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_TASK_ID,
                COLUMN_TASK_DESCRIPTION,
                COLUMN_TASK_TIMESTAMP,
                COLUMN_TASK_USER_ID,
                COLUMN_TITLE,
                COLUMN_SELECTED_TYPE,
                COLUMN_LINK_URL
        };

        Cursor cursor = db.query(TABLE_TASK, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
                task.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TIMESTAMP)));
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_USER_ID));
                task.setSelectedType(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTED_TYPE)));
                task.setLinkUrl(cursor.getString(cursor.getColumnIndex(COLUMN_LINK_URL)));
                User user = getUserById(userId);
                task.setUser(user);

                taskList.add(task);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return taskList;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAMA,
                COLUMN_ANGKATAN,
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
            user.setAngkatan(cursor.getString(cursor.getColumnIndex(COLUMN_ANGKATAN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
        }
        return user;
    }
    public User getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAMA,
                COLUMN_ANGKATAN,
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
            user.setAngkatan(cursor.getString(cursor.getColumnIndex(COLUMN_ANGKATAN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
        }
        return user;
    }

    public long addTask(String description, String timestamp, int userId, String title, String selectedType, String linkUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_TIMESTAMP, timestamp);
        values.put(COLUMN_TASK_USER_ID, userId);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SELECTED_TYPE, selectedType);
        values.put(COLUMN_LINK_URL, linkUrl); // Simpan linkUrl
        return db.insert(TABLE_TASK, null, values);
    }

    public List<Task> getTasksForUser(int user_id) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_TASK_ID,
                COLUMN_TASK_DESCRIPTION,
                COLUMN_TASK_TIMESTAMP,
                COLUMN_TASK_USER_ID,
                COLUMN_TITLE,
                COLUMN_SELECTED_TYPE,
                COLUMN_LINK_URL
        };

        String selection = COLUMN_TASK_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(user_id)};

        Cursor cursor = db.query(TABLE_TASK, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_DESCRIPTION)));
                task.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TASK_TIMESTAMP)));
                task.setSelectedType(cursor.getString(cursor.getColumnIndex(COLUMN_SELECTED_TYPE)));
                task.setLinkUrl(cursor.getString(cursor.getColumnIndex(COLUMN_LINK_URL)));
                int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_USER_ID));
                User user = getUserById(userId);
                task.setUser(user);

                taskList.add(task);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return taskList;
    }
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAMA,
                COLUMN_ANGKATAN,
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            user.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
            user.setAngkatan(cursor.getString(cursor.getColumnIndex(COLUMN_ANGKATAN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
            cursor.close();
        }
        return user;
    }

}

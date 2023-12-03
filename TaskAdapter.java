package com.example.uastest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private boolean isMyTaskActivity;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    // Setter untuk listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Constructor
    public TaskAdapter(List<Task> taskList, Context context, boolean isMyTaskActivity) {
        this.taskList = taskList;
        this.context = context;
        this.isMyTaskActivity = isMyTaskActivity;
    }

    // Metode untuk mengatur daftar tugas
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged(); // Memberi tahu adapter bahwa data telah berubah
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_cardview, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        // Set judul dan deskripsi tugas ke TextView di cardView
        holder.textViewTaskTitle.setText(currentTask.getTitle());
        holder.textViewSelectedType.setText("Jenis         : " + currentTask.getSelectedType());

        User currentUser = currentTask.getUser();
        if (currentUser != null) {
            holder.textViewUsername.setText("Pencipta   : " + currentUser.getNama());
        }

        holder.textViewTimestamp.setText("Waktu       : " + currentTask.getTimestamp());

        // Tambahkan kode untuk memuat gambar menggunakan Glide
        String imageUrl = currentTask.getlogoUrl();
        Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .into(holder.imageView);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskDetail(currentTask);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onDeleteClick(position);
                }
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onEditClick(position);
                }
            }
        });
        holder.btnDelete.setVisibility(isMyTaskActivity ? View.VISIBLE : View.GONE);
        holder.btnEdit.setVisibility(isMyTaskActivity ? View.VISIBLE : View.GONE);
    }

    private void showTaskDetail(Task task) {
        Intent intent = new Intent(context, DetailTaskActivity.class);
        intent.putExtra("task_title", task.getTitle());
        intent.putExtra("task_description", task.getDescription());
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTaskTitle;
        TextView textViewUsername;
        TextView textViewTimestamp;
        TextView textViewSelectedType;
        Button btnDelete;
        Button btnEdit;
        Button btnDetail;
        ImageView imageView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewSelectedType = itemView.findViewById(R.id.textViewJenis);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}

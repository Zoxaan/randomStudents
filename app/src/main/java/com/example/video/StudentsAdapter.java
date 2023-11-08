package com.example.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {
    private List<String> students;

    public StudentsAdapter(List<String> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        String studentName = students.get(position);
        holder.bind(studentName);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
        }

        public void bind(String studentName) {
            studentNameTextView.setText(studentName);
        }
    }
}

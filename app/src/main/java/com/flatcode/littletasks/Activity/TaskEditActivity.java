package com.flatcode.littletasks.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityTaskAddBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TaskEditActivity extends AppCompatActivity {

    private ActivityTaskAddBinding binding;
    private final Context context = TaskEditActivity.this;

    private ProgressDialog dialog;

    private String taskId, categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        taskId = getIntent().getStringExtra(DATA.TASK_ID);
        categoryId = getIntent().getStringExtra(DATA.CATEGORY_ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        loadBooksInfo();
        loadCategories();

        binding.toolbar.nameSpace.setText(R.string.edit_task);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.name.setText(R.string.task_name);
        binding.toolbar.ok.setOnClickListener(v -> validateData());
    }

    private String name = DATA.EMPTY, points = DATA.ZERO + DATA.EMPTY;

    private void validateData() {
        //get data
        name = binding.nameEt.getText().toString().trim();
        points = binding.PointsEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(points)) {
            Toast.makeText(context, "Enter Points...", Toast.LENGTH_SHORT).show();
        } else {
            uploadTaskInfoDB();
        }
    }

    private void uploadTaskInfoDB() {
        dialog.setMessage("Uploading task info...");
        dialog.show();

        int point = Integer.parseInt(points);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put(DATA.POINTS, point);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        ref.child(taskId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Task info updated...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadBooksInfo() {
        DatabaseReference refBooks = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        refBooks.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Task item = snapshot.getValue(Task.class);
                assert item != null;
                String points = DATA.EMPTY + item.getPoints();
                String title = DATA.EMPTY + item.getName();

                binding.nameEt.setText(title);
                binding.PointsEt.setText(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadCategories() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES).child(categoryId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category item = snapshot.getValue(Category.class);
                assert item != null;
                String categoryImage = DATA.EMPTY + item.getImage();
                String categoryName = DATA.EMPTY + item.getName();
                VOID.Glide(false, context, categoryImage, binding.image);
                binding.category.setText(categoryName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
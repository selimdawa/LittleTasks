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

public class TaskAddActivity extends AppCompatActivity {

    private ActivityTaskAddBinding binding;
    private final Context context = TaskAddActivity.this;

    private ProgressDialog dialog;

    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        categoryId = getIntent().getStringExtra(DATA.CATEGORY_ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.toolbar.nameSpace.setText(R.string.add_new_task);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.ok.setOnClickListener(v -> validateData());

        loadCategories();
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        String id = ref.push().getKey();
        int point = Integer.parseInt(points);
        //setup data to upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, DATA.EMPTY + DATA.FirebaseUserUid);
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put(DATA.POINTS, point);
        hashMap.put(DATA.AVAILABLE_POINTS, DATA.ZERO);
        hashMap.put(DATA.RANK, DATA.ZERO);
        hashMap.put(DATA.CATEGORY, DATA.EMPTY + categoryId);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.START, DATA.ZERO);
        hashMap.put(DATA.END, DATA.ZERO);

        //db reference: DB > Books
        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failure to upload to db due to :" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCategories() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES).child(categoryId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category item = snapshot.getValue(Category.class);
                assert item != null;
                String categoryTitle = DATA.EMPTY + item.getName();
                String categoryImage = DATA.EMPTY + item.getImage();

                binding.category.setText(categoryTitle);
                VOID.Glide(false, context, categoryImage, binding.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
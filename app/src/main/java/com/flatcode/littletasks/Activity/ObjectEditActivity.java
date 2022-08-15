package com.flatcode.littletasks.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.databinding.ActivityObjectEditBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.HashMap;

import static com.flatcode.littletasks.Unit.DATA.EMPTY;

public class ObjectEditActivity extends AppCompatActivity {

    private ActivityObjectEditBinding binding;
    Context context = ObjectEditActivity.this;

    String id;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityObjectEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        id = getIntent().getStringExtra(DATA.ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setCanceledOnTouchOutside(false);

        loadInfo();

        binding.toolbar.nameSpace.setText(R.string.edit_object);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.name.setText(R.string.object_name);
        binding.go.setOnClickListener(v -> validateData());
    }

    private String name = EMPTY, points = EMPTY;

    private void validateData() {
        name = binding.nameEt.getText().toString().trim();
        points = binding.PointsEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(points)) {
            Toast.makeText(context, "Enter points...", Toast.LENGTH_SHORT).show();
        } else {
            Update();
        }
    }

    private void Update() {
        dialog.setMessage("Updating Object...");
        dialog.show();
        int point = Integer.parseInt(points);

        HashMap<String, java.lang.Object> hashMap = new HashMap<>();
        hashMap.put(DATA.NAME, EMPTY + name);
        hashMap.put(DATA.POINTS, point);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        reference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Object updated...", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                OBJECT item = snapshot.getValue(OBJECT.class);
                assert item != null;
                String name = item.getName();
                int points = item.getPoints();

                binding.nameEt.setText(name);
                binding.PointsEt.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, points));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
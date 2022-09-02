package com.flatcode.littletasks.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.Model.User;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    Context context = ProfileActivity.this;

    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        profileId = intent.getStringExtra(DATA.PROFILE_ID);

        binding.edit.setOnClickListener(v -> VOID.Intent(context, ProfileEditActivity.class));
        binding.back.setOnClickListener(v -> onBackPressed());
    }

    private void init() {
        loadUserInfo();
        getNrItems(DATA.TASKS, binding.numberTasks);
        getNrItems(DATA.PLANS, binding.numberPlans);
        getNrItems(DATA.OBJECTS, binding.numberObjects);
        getNrItems(DATA.CATEGORIES, binding.numberCategories);
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User item = snapshot.getValue(User.class);
                assert item != null;
                String username = DATA.EMPTY + item.getUsername();
                String profileImage = DATA.EMPTY + item.getProfileImage();

                binding.username.setText(username);
                VOID.Glide(true, context, profileImage, binding.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getNrItems(String database, TextView text) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(database);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Task item = data.getValue(Task.class);
                    assert item != null;
                    if (item.getPublisher().equals(profileId))
                        i++;
                }
                text.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
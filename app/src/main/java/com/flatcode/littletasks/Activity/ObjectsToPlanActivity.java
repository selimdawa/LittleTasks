package com.flatcode.littletasks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Adapter.ObjectOptionAdapter;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.databinding.ActivityObjectsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ObjectsToPlanActivity extends AppCompatActivity {

    private ActivityObjectsBinding binding;
    private Context context = ObjectsToPlanActivity.this;

    ArrayList<OBJECT> list;
    ObjectOptionAdapter adapter;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityObjectsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        id = getIntent().getStringExtra(DATA.ID);
        binding.toolbar.nameSpace.setText(R.string.add_object_to_plan);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.add.item.setVisibility(View.GONE);

        binding.toolbar.search.setOnClickListener(v -> {
            binding.toolbar.toolbar.setVisibility(GONE);
            binding.toolbar.toolbarSearch.setVisibility(VISIBLE);
            DATA.searchStatus = true;
        });

        binding.toolbar.close.setOnClickListener(v -> onBackPressed());

        binding.toolbar.textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapter.getFilter().filter(s);
                } catch (Exception e) {
                    //None
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ObjectOptionAdapter(context, list, id);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getItems() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    OBJECT object = data.getValue(OBJECT.class);
                    assert object != null;
                    if (object.getPublisher().equals(DATA.FirebaseUserUid))
                        list.add(object);
                }
                binding.bar.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (DATA.searchStatus) {
            binding.toolbar.toolbar.setVisibility(VISIBLE);
            binding.toolbar.toolbarSearch.setVisibility(GONE);
            DATA.searchStatus = false;
            binding.toolbar.textSearch.setText(DATA.EMPTY);
        } else
            super.onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getItems();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getItems();
    }
}
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

import com.flatcode.littletasks.Adapter.ObjectAdapter;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityObjectsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ObjectsPlanActivity extends AppCompatActivity {

    private ActivityObjectsBinding binding;
    private Context context = ObjectsPlanActivity.this;

    List<String> item;
    ArrayList<OBJECT> list;
    ObjectAdapter adapter;

    String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityObjectsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        id = getIntent().getStringExtra(DATA.ID);
        name = getIntent().getStringExtra(DATA.NAME);

        binding.toolbar.nameSpace.setText(R.string.objects_plan);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.add.item.setOnClickListener(v -> VOID.IntentExtra(context, CLASS.OBJECT_TO_PLAN, DATA.ID, id));

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
        adapter = new ObjectAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getData() {
        item = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(id).child(DATA.AUTO_TASKS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    item.add(snapshot.getKey());
                }
                getItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getItems() {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OBJECT object = snapshot.getValue(OBJECT.class);
                    for (String id : item) {
                        assert object != null;
                        if (object.getId().equals(id))
                            if (object.getPublisher().equals(DATA.FirebaseUserUid))
                                list.add(object);
                    }
                }
                binding.bar.setVisibility(GONE);
                if (!(list.isEmpty())) {
                    binding.recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                } else {
                    binding.recyclerView.setVisibility(GONE);
                    binding.emptyText.setVisibility(VISIBLE);
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
        getData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }
}
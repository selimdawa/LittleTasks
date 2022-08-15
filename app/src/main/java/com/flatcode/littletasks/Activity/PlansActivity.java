package com.flatcode.littletasks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Adapter.PlanAdapter;
import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityPlansBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class PlansActivity extends AppCompatActivity {

    private ActivityPlansBinding binding;
    Context context = PlansActivity.this;

    ArrayList<Plan> list;
    PlanAdapter adapter;
    String newPlan;
    private static Boolean isNew = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPlansBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        newPlan = intent.getStringExtra(DATA.NEW_PLAN);

        binding.toolbar.nameSpace.setText(R.string.plans);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.add.add.setText(R.string.add_plan);
        binding.add.item.setOnClickListener(v -> VOID.Intent(context, CLASS.PLAN_ADD));

        if (newPlan.equals("true"))
            isNew = true;
        else if (newPlan.equals("false"))
            isNew = false;

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
        adapter = new PlanAdapter(context, list, isNew);
        binding.recyclerView.setAdapter(adapter);

        getItems();
    }

    private void getItems() {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.PLANS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Plan item = data.getValue(Plan.class);
                    assert item != null;
                    if (item.getPublisher().equals(DATA.FirebaseUserUid)) {
                        list.add(item);
                        i++;
                    }
                }
                binding.toolbar.number.setText(MessageFormat.format("( {0} )", i));
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
}
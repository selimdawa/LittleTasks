package com.flatcode.littletasks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Adapter.TaskAdapter;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityPageSwitchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CategoryTasksActivity extends AppCompatActivity {

    private ActivityPageSwitchBinding binding;
    private final Context context = CategoryTasksActivity.this;

    ArrayList<Task> list;
    TaskAdapter adapter;

    String id, name, type;
    static Boolean searchStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPageSwitchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        id = intent.getStringExtra(DATA.ID);
        name = intent.getStringExtra(DATA.NAME);

        type = DATA.TIMESTAMP;
        binding.toolbar.nameSpace.setText(name);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.add.add.setText(R.string.add_task);
        binding.add.item.setOnClickListener(v -> VOID.IntentExtra(context, CLASS.TASK_ADD, DATA.CATEGORY_ID, id));

        binding.toolbar.search.setOnClickListener(v -> {
            binding.toolbar.toolbar.setVisibility(GONE);
            binding.toolbar.toolbarSearch.setVisibility(VISIBLE);
            searchStatus = true;
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
        adapter = new TaskAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerViewReverse.setAdapter(adapter);

        binding.filter.all.setOnClickListener(v -> {
            type = DATA.TIMESTAMP;
            getData(type, binding.recyclerView, binding.recyclerViewReverse);
        });
        binding.filter.points.setOnClickListener(v -> UpToDown(binding.filter.a1, DATA.POINTS));
        binding.filter.AVPoints.setOnClickListener(v -> UpToDown(binding.filter.a2, DATA.AVAILABLE_POINTS));
        binding.filter.add.setOnClickListener(v -> UpToDown(binding.filter.a3, DATA.TIMESTAMP));
        binding.filter.start.setOnClickListener(v -> UpToDown(binding.filter.a4, DATA.START));
        binding.filter.end.setOnClickListener(v -> UpToDown(binding.filter.a5, DATA.END));
    }

    private void UpToDown(ImageView item, String Type) {
        type = Type;
        if (item.getTag() != null) {
            if (item.getTag().equals("up")) {
                getData(type, binding.recyclerView, binding.recyclerViewReverse);
                item.setTag("down");
                item.setImageResource(R.drawable.ic_down);
            } else if (item.getTag().equals("down")) {
                getData(type, binding.recyclerViewReverse, binding.recyclerView);
                item.setTag("up");
                item.setImageResource(R.drawable.ic_up);
            }
        } else {
            getData(type, binding.recyclerView, binding.recyclerViewReverse);
            item.setTag("down");
            item.setImageResource(R.drawable.ic_down);
        }
    }

    private void getData(String orderBy, RecyclerView recyclerView, RecyclerView recyclerView2) {
        Query reference = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        reference.orderByChild(orderBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task item = snapshot.getValue(Task.class);
                    assert item != null;
                    if (item.getCategory().equals(id))
                        if (item.getPublisher().equals(DATA.FirebaseUserUid)) {
                            list.add(item);
                            i++;
                        }
                }
                binding.toolbar.number.setText(MessageFormat.format("( {0} )", i));
                recyclerView2.setVisibility(GONE);
                binding.bar.setVisibility(GONE);
                if (!(list.isEmpty())) {
                    recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                } else {
                    recyclerView.setVisibility(GONE);
                    binding.emptyText.setVisibility(VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPoints() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                int a = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task item = snapshot.getValue(Task.class);
                    assert item != null;
                    if (item.getCategory().equals(id)) {
                        i = i + item.getPoints();
                        a = a + item.getAVPoints();
                    }
                }
                binding.allPoints.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
                binding.av.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, a));
                binding.level.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, VOID.levelPoint(a, 10)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (searchStatus) {
            binding.toolbar.toolbar.setVisibility(VISIBLE);
            binding.toolbar.toolbarSearch.setVisibility(GONE);
            searchStatus = false;
            binding.toolbar.textSearch.setText(DATA.EMPTY);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(DATA.TIMESTAMP, binding.recyclerView, binding.recyclerViewReverse);
        getPoints();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData(DATA.TIMESTAMP, binding.recyclerView, binding.recyclerViewReverse);
        getPoints();
    }
}
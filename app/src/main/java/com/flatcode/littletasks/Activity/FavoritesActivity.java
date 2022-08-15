package com.flatcode.littletasks.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
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
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.databinding.ActivityFavoritesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;
    private final Context context = FavoritesActivity.this;

    List<String> item;
    ArrayList<Task> list;
    TaskAdapter adapter;

    String type, tasksType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tasksType = getIntent().getStringExtra(DATA.TASK_TYPE);

        if (tasksType == null)
            tasksType = DATA.TASKS_ALL;

        type = DATA.TIMESTAMP;
        binding.toolbar.nameSpace.setText(R.string.favorites);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

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

        getData(DATA.TIMESTAMP, binding.recyclerView, binding.recyclerViewReverse);
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
        item = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    item.add(snapshot.getKey());
                }
                getItems(orderBy, recyclerView, recyclerView2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getItems(String orderBy, RecyclerView recyclerView, RecyclerView recyclerView2) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    for (String id : item) {
                        assert task != null;
                        if (task.getId().equals(id))
                            if (task.getPublisher().equals(DATA.FirebaseUserUid))
                                if (tasksType.equals(DATA.TASKS_ALL)) {
                                    list.add(task);
                                    i++;
                                } else if (tasksType.equals(DATA.TASKS_UN_STARTED)) {
                                    if (task.getStart() == 0 && task.getEnd() == 0) {
                                        list.add(task);
                                        i++;
                                    } else if (tasksType.equals(DATA.TASKS_STARTED)) {
                                        if (task.getStart() != 0 && task.getEnd() == 0) {
                                            list.add(task);
                                            i++;
                                        }
                                    } else if (tasksType.equals(DATA.TASKS_COMPLETED)) {
                                        if (task.getStart() != 0 && task.getEnd() != 0) {
                                            list.add(task);
                                            i++;
                                        }
                                    }
                                }
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
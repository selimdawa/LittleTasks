package com.flatcode.littletasks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Filter.TaskCategoryFilter;
import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.GetTimeAgo;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemTaskBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {

    private ItemTaskBinding binding;
    private final Context context;

    public ArrayList<Task> list, filterList;
    private TaskCategoryFilter filter;

    public TaskAdapter(Context context, ArrayList<Task> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.ViewHolder holder, final int position) {

        final Task item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getName();
        String publisher = DATA.EMPTY + item.getPublisher();
        String category = DATA.EMPTY + item.getCategory();
        String timestamp = DATA.EMPTY + item.getTimestamp();
        String start = DATA.EMPTY + item.getStart();
        String end = DATA.EMPTY + item.getEnd();
        String points = DATA.EMPTY + item.getPoints();
        String AVPoints = DATA.EMPTY + item.getAVPoints();

        if (item.getName().equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }
        holder.points.setText(points);
        holder.AVPoints.setText(AVPoints);

        long addTime = Long.parseLong(timestamp);
        String Add = GetTimeAgo.getMessageAgo(addTime, context);
        holder.add.setText(Add);
        if (start.equals("0")) {
            holder.start.setText("-");
        } else {
            long startTime = Long.parseLong(start);
            String Start = GetTimeAgo.getMessageAgo(startTime, context);
            holder.start.setText(Start);
        }
        if (end.equals("0")) {
            holder.end.setText("-");
        } else {
            long endTime = Long.parseLong(end);
            String End = GetTimeAgo.getMessageAgo(endTime, context);
            holder.end.setText(End);
        }

        getData(category, holder.category, holder.image);
        VOID.isFavorite(holder.favorites, id, publisher);
        holder.favorites.setOnClickListener(v -> VOID.checkFavorite(holder.favorites, id));
        VOID.isTask(context, holder.task, id);
        holder.more.setOnClickListener(view -> VOID.moreTask(context, item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new TaskCategoryFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, favorites, task, more;
        TextView name, category, points, AVPoints, add, start, end;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            favorites = binding.favorites;
            task = binding.task;
            name = binding.name;
            category = binding.category;
            points = binding.points;
            AVPoints = binding.AVPoints;
            add = binding.add;
            start = binding.start;
            end = binding.end;
            more = binding.more;
            item = binding.item;
        }
    }

    private void getData(String categoryId, TextView name, ImageView image) {
        Query reference = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES).child(categoryId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Category item = dataSnapshot.getValue(Category.class);
                assert item != null;
                name.setText(item.getName());
                VOID.Glide(false, context, item.getImage(), image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
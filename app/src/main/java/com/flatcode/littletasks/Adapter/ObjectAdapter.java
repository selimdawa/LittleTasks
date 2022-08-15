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

import com.flatcode.littletasks.Filter.ObjectsFilter;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemObjectBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> implements Filterable {

    private ItemObjectBinding binding;
    private final Context context;

    public ArrayList<OBJECT> list, filterList;
    private ObjectsFilter filter;

    public ObjectAdapter(Context context, ArrayList<OBJECT> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemObjectBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final OBJECT item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getName();
        String points = DATA.EMPTY + item.getPoints();

        if (name.equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }

        if (points.equals(DATA.EMPTY)) {
            holder.points.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, DATA.ZERO));
        } else {
            holder.points.setText(points);
        }

        holder.more.setOnClickListener(view -> VOID.moreObject(context, item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ObjectsFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, points;
        ImageView more;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            points = binding.points;
            name = binding.name;
            more = binding.more;
            item = binding.item;
        }
    }
}
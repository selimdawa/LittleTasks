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

import com.flatcode.littletasks.Filter.PlansFilter;
import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemPlanBinding;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> implements Filterable {

    private ItemPlanBinding binding;
    private final Context context;

    public ArrayList<Plan> list, filterList;
    private PlansFilter filter;
    Boolean isNew;

    public PlanAdapter(Context context, ArrayList<Plan> list, Boolean isNew) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isNew = isNew;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPlanBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Plan item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getName();
        String image = DATA.EMPTY + item.getImage();

        VOID.Glide(false, context, image, holder.image);

        if (name.equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }

        if (isNew) {
            holder.more.setVisibility(View.GONE);
        } else {
            holder.more.setVisibility(View.VISIBLE);
        }

        holder.more.setOnClickListener(view -> VOID.morePlan(context, item));
        holder.item.setOnClickListener(view -> {
            if (isNew)
                VOID.IntentExtra(context, CLASS.CATEGORY_ADD, DATA.ID, id);
            else
                VOID.IntentExtra2(context, CLASS.OBJECTS_PLAN, DATA.ID, id, DATA.NAME, name);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PlansFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, more;
        TextView name;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            name = binding.name;
            more = binding.more;
            item = binding.item;
        }
    }
}
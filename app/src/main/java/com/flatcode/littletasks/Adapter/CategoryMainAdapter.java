package com.flatcode.littletasks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemCategoryBinding;

import java.util.ArrayList;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.ViewHolder> {

    private ItemCategoryBinding binding;
    private final Context context;

    public ArrayList<Category> list;

    public CategoryMainAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Category item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getName();
        String image = DATA.EMPTY + item.getImage();

        VOID.Glide(false, context, image, holder.image);
        VOID.GlideBlur(false, context, image, holder.imageBlur, 50);

        if (name.equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(name);
        }

        holder.more.setOnClickListener(view -> VOID.moreCategory(context, item));
        holder.card.setOnClickListener(v ->
                VOID.IntentExtra2(context, CLASS.CATEGORY_TASKS, DATA.ID, id, DATA.NAME, name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, imageBlur, more;
        TextView name;
        CardView card;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            imageBlur = binding.imageBlur;
            name = binding.name;
            more = binding.more;
            card = binding.card;
        }
    }
}
package com.flatcode.littletasks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Filter.CategoriesFilter;
import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemCategoriesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> implements Filterable {

    private ItemCategoriesBinding binding;
    private final Context context;

    public ArrayList<Category> list, filterList;
    private CategoriesFilter filter;

    public CategoriesAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemCategoriesBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Category item = list.get(position);
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

        nrBooks(holder.numer, id);

        holder.more.setOnClickListener(v -> VOID.moreCategory(context, item));
        holder.card.setOnClickListener(view ->
                VOID.IntentExtra2(context, CLASS.CATEGORY_TASKS, DATA.ID, id, DATA.NAME, name));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CategoriesFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, more;
        TextView name, numer;
        CardView card;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            name = binding.name;
            more = binding.more;
            numer = binding.numer;
            card = binding.card;
        }
    }

    public static void nrBooks(TextView number, String categoryId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task item = snapshot.getValue(Task.class);
                    assert item != null;
                    if (item.getCategory().equals(categoryId))
                        i++;
                }
                number.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
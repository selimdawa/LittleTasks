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

import com.flatcode.littletasks.Filter.ObjectOptionFilter;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemObjectBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ObjectOptionAdapter extends RecyclerView.Adapter<ObjectOptionAdapter.ViewHolder> implements Filterable {

    private ItemObjectBinding binding;
    private final Context context;

    public ArrayList<OBJECT> list, filterList;
    private ObjectOptionFilter filter;
    String planId;

    public ObjectOptionAdapter(Context context, ArrayList<OBJECT> list, String planId) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.planId = planId;
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

        holder.option.setVisibility(View.VISIBLE);
        holder.more.setVisibility(View.GONE);

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

        VOID.isPlan(holder.option, item.getId(), planId);
        holder.option.setOnClickListener(view -> VOID.checkPlan(holder.option, item.getId(), planId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ObjectOptionFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, points;
        ImageView more, option;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            points = binding.points;
            name = binding.name;
            more = binding.more;
            option = binding.option;
            item = binding.item;
        }
    }

    public static void isLoves(ImageView image, String planId, String objectId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(planId).child(DATA.OBJECTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(objectId).exists()) {
                    image.setImageResource(R.drawable.ic_remove);
                    image.setTag("added");
                } else {
                    image.setImageResource(R.drawable.ic___add);
                    image.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void addToPlan(ImageView image, String planId, String objectId) {
        if (image.getTag().equals("add")) {
            FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(planId)
                    .child(DATA.OBJECTS).child(objectId).setValue(true);
        } else {
            FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(planId)
                    .child(DATA.OBJECTS).child(objectId).removeValue();
        }
    }
}
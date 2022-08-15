package com.flatcode.littletasks.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.databinding.ItemNewPlanBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

public class ObjectAddAdapter extends RecyclerView.Adapter<ObjectAddAdapter.ViewHolder> {

    private ItemNewPlanBinding binding;
    private final Context context;
    public List<OBJECT> list;

    public ObjectAddAdapter(Context context, List<OBJECT> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemNewPlanBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int id = position + 1, last = list.size() - 1;

        if (position == last) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }

        holder.number.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, id));
        holder.add.setOnClickListener(v -> validateData(holder.name, holder.points, holder.add, holder.done));
        holder.done.setOnClickListener(v -> Toast.makeText(context, "Already done", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView add, number, done;
        EditText name, points;
        View line;

        public ViewHolder(View view) {
            super(view);
            name = binding.nameEt;
            add = binding.add;
            done = binding.done;
            number = binding.number;
            points = binding.PointsEt;
            line = binding.view;
        }
    }

    private void validateData(TextView name, TextView points, TextView add, TextView ok) {
        String Name = name.getText().toString().trim();
        String Points = points.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(context, "Enter Name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Points)) {
            Toast.makeText(context, "Enter Points...", Toast.LENGTH_SHORT).show();
        } else {
            uploadToDB(Name, Points, add, ok);
        }
    }

    private void uploadToDB(String name, String points, TextView add, TextView ok) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        String id = ref.push().getKey();
        int point = Integer.parseInt(points);
        //setup data to upload
        HashMap<String, java.lang.Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, DATA.EMPTY + DATA.FirebaseUserUid);
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put(DATA.POINTS, point);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());

        //db reference: DB > Books
        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            Toast.makeText(context, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
            add.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);
        }).addOnFailureListener(e -> Toast.makeText(context,
                "Failure to upload to db due to :" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
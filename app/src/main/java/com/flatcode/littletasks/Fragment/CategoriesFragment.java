package com.flatcode.littletasks.Fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littletasks.Adapter.CategoryMainAdapter;
import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.databinding.FragmentCategoriesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    ArrayList<Category> list;
    CategoryMainAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(LayoutInflater.from(getContext()), container, false);

        //binding.recyclerCategory.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new CategoryMainAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void loadItems() {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category item = snapshot.getValue(Category.class);
                    assert item != null;
                    if (item.getPublisher().equals(DATA.FirebaseUserUid))
                        list.add(item);
                }
                binding.bar.setVisibility(GONE);
                if (!(list.isEmpty())) {
                    binding.recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                } else {
                    binding.recyclerView.setVisibility(GONE);
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
    public void onResume() {
        super.onResume();
        loadItems();
    }
}
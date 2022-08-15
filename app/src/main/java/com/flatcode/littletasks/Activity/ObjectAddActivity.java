package com.flatcode.littletasks.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Adapter.ObjectAddAdapter;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.databinding.ActivityObjectAddBinding;

import java.util.ArrayList;

public class ObjectAddActivity extends AppCompatActivity {

    private ActivityObjectAddBinding binding;
    Context context = ObjectAddActivity.this;

    ArrayList<OBJECT> list;
    ObjectAddAdapter adapter;
    OBJECT editorsChoice = new OBJECT();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityObjectAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.add_new_object);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ObjectAddAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        IdeaPosts();
    }

    private void IdeaPosts() {
        list.clear();
        for (int i = 0; i < 20; i++) {
            list.add(editorsChoice);
        }
        adapter.notifyDataSetChanged();
    }
}
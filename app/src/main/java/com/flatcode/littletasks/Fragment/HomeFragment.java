package com.flatcode.littletasks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.one.setOnClickListener(v -> VOID.IntentExtra(getContext(), CLASS.FAVORITES, DATA.TASK_TYPE, DATA.TASKS_ALL));
        binding.two.setOnClickListener(v -> VOID.IntentExtra(getContext(), CLASS.FAVORITES, DATA.TASK_TYPE, DATA.TASKS_UN_STARTED));
        binding.three.setOnClickListener(v -> VOID.IntentExtra(getContext(), CLASS.FAVORITES, DATA.TASK_TYPE, DATA.TASKS_STARTED));
        binding.four.setOnClickListener(v -> VOID.IntentExtra(getContext(), CLASS.FAVORITES, DATA.TASK_TYPE, DATA.TASKS_COMPLETED));

        return binding.getRoot();
    }
}
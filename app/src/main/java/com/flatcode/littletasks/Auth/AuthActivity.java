package com.flatcode.littletasks.Auth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    Context context = AuthActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        ActivityAuthBinding binding = ActivityAuthBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);
        binding.loginBtn.setOnClickListener(v -> VOID.Intent(context, CLASS.LOGIN));
        binding.skipBtn.setOnClickListener(v -> VOID.Intent(context, CLASS.REGISTER));
    }
}
package com.flatcode.littletasks.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.littletasks.Model.Setting;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ItemSettingBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private ItemSettingBinding binding;
    private final Context context;
    private ArrayList<Setting> list;

    public SettingAdapter(Context context, ArrayList<Setting> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemSettingBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final SettingAdapter.ViewHolder holder, final int position) {

        final Setting item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String name = DATA.EMPTY + item.getName();
        int image = item.getImage();
        int number = item.getNumber();
        Class to = item.getC();
        String type = item.getType();

        holder.name.setText(name);
        holder.image.setImageResource(image);

        if (number != 0) {
            holder.number.setVisibility(View.VISIBLE);
            holder.number.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, number));
        } else {
            holder.number.setVisibility(View.GONE);
        }

        holder.item.setOnClickListener(view -> {
            if (!(type == null)) {
                if (type.equals(DATA.PLANS)) {
                    VOID.IntentExtra(context, CLASS.PLANS, DATA.NEW_PLAN, DATA.EMPTY + false);
                } else if (type.equals(DATA.CHOOSE_PLAN)) {
                    VOID.IntentExtra(context, CLASS.PLANS, DATA.NEW_PLAN, DATA.EMPTY + true);
                }
            } else {
                switch (id) {
                    case "10":
                        VOID.dialogAboutApp(context);
                        break;
                    case "11":
                        VOID.dialogLogout(context);
                        break;
                    case "12":
                        VOID.shareApp(context);
                        break;
                    case "13":
                        VOID.rateApp(context);
                        break;
                    default:
                        VOID.Intent(context, to);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, number;
        ImageView image;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = binding.image;
            name = binding.name;
            number = binding.number;
            item = binding.item;
        }
    }
}
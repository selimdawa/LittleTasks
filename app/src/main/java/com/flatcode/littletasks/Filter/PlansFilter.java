package com.flatcode.littletasks.Filter;

import android.widget.Filter;

import com.flatcode.littletasks.Adapter.PlanAdapter;
import com.flatcode.littletasks.Model.Plan;

import java.util.ArrayList;

public class PlansFilter extends Filter {

    ArrayList<Plan> list;
    PlanAdapter adapter;

    public PlansFilter(ArrayList<Plan> list, PlanAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<Plan> filter = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().toUpperCase().contains(constraint)) {
                    filter.add(list.get(i));
                }
            }
            results.count = filter.size();
            results.values = filter;
        } else {
            results.count = list.size();
            results.values = list;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<Plan>) results.values;
        adapter.notifyDataSetChanged();
    }
}
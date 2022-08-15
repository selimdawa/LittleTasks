package com.flatcode.littletasks.Filter;

import android.widget.Filter;

import com.flatcode.littletasks.Adapter.ObjectAdapter;
import com.flatcode.littletasks.Model.OBJECT;

import java.util.ArrayList;

public class ObjectsFilter extends Filter {

    ArrayList<OBJECT> list;
    ObjectAdapter adapter;

    public ObjectsFilter(ArrayList<OBJECT> list, ObjectAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<OBJECT> filter = new ArrayList<>();
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
        adapter.list = (ArrayList<OBJECT>) results.values;
        adapter.notifyDataSetChanged();
    }
}
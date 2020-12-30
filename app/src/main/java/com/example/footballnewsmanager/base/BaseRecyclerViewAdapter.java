package com.example.footballnewsmanager.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected ArrayList<T> items = new ArrayList<>();

    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutRes(), parent, false);
        return onCreateItemViewHolder(parent, viewType, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        onBindView(holder,position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @LayoutRes
    public abstract int getItemLayoutRes();

    public abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView);

    public abstract void onBindView(BaseViewHolder holder, int position);

}

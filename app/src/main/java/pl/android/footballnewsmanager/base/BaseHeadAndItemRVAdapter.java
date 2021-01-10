package pl.android.footballnewsmanager.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public abstract class BaseHeadAndItemRVAdapter <T, HVH extends BaseViewHolder, VH extends BaseViewHolder> extends BaseRecyclerViewAdapter<T,VH>{
    private static final int HEAD = 1;
    private static final int ITEM = 0;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(getHeaderLayoutRes(),parent, false);
                return onCreateHeaderViewHolder(parent,viewType,itemView);
            case 0:
                return super.onCreateViewHolder(parent, viewType);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position==0)
            onBindHeaderView(holder,position);
        else
            super.onBindViewHolder(holder, position-1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
            return HEAD;
        return ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent, int viewType, View itemView);

    @LayoutRes
    public abstract int getHeaderLayoutRes();

    public abstract void onBindHeaderView(BaseViewHolder holder, int position);
}

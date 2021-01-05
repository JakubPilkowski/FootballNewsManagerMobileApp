package com.example.footballnewsmanager.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class FabScrollListener extends PaginationScrollListener {

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        onScroll(dx, dy);
        super.onScrolled(recyclerView, dx, dy);
    }

    protected abstract void onScroll(int dx, int dy);
}

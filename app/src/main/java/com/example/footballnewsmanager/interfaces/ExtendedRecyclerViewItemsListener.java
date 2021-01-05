package com.example.footballnewsmanager.interfaces;

public interface ExtendedRecyclerViewItemsListener<T> extends RecyclerViewItemsListener<T> {
    void onDetached();
    void backToFront();
    void onChangeItems();
}

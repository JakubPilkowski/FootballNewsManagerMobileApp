package com.example.footballnewsmanager.interfaces;

public interface RecyclerViewItemsListener<T> {
    void onChangeItem(T oldItem, T newItem);
}

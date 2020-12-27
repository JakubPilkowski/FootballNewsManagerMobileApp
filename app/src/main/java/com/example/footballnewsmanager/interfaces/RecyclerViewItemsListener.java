package com.example.footballnewsmanager.interfaces;

import com.example.footballnewsmanager.models.UserNews;

public interface RecyclerViewItemsListener<T> {
    void onDetached();
    void backToFront();
    void onChangeItem(T oldItem, T newItem);
    void onChangeItems();
}

package com.example.footballnewsmanager.adapters.liked_news;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.newsForTeam.NewsForTeamAdapterViewModel;
import com.example.footballnewsmanager.databinding.NewsForTeamItemLayoutBinding;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class LikedNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserNews> items = new ArrayList<>();

    private List<NewsForTeamAdapterViewModel> viewModels = new ArrayList<>();

    private static final int HEADER = 0;
    private static final int ITEM = 1;
    private static final int LOADING = 2;
    public boolean isLoading = false;

    private Activity activity;

    public LikedNewsAdapter(Activity activity){
        this.activity = activity;
    }

    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start + 1, this.items.size());
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if(!isLoading)
            notifyItemChanged(items.size()+1);
    }

    public List<UserNews> getItems() {
        return items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_news_header, parent, false);
                return new HeaderViewHolder(view);
            }
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_item_layout, parent, false);
                NewsForTeamItemLayoutBinding binding = NewsForTeamItemLayoutBinding.bind(view);
                return new ItemViewHolder(view, binding);
            }
            case LOADING: {
                Log.d("News", "onCreateViewHolder IsLoading");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressDialogViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_item_layout, parent, false);
                NewsForTeamItemLayoutBinding binding = NewsForTeamItemLayoutBinding.bind(view);
                return new ItemViewHolder(view, binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ((position == items.size() + 1  && isLoading) || position == 0)
            return;
        else {
            NewsForTeamAdapterViewModel viewModel;
            int itemsPosition = position - 1;
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsForTeamAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            viewModel.init(items.get(itemsPosition), activity);
            ((ItemViewHolder) holder).binding.setViewModel(viewModel);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == items.size() + 1) {
            return LOADING;
        } else {
            return ITEM;
        }

    }

    @Override
    public int getItemCount() {
        if(isLoading)
            return items.size() + 2;
        return items.size() + 1;
    }



    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        NewsForTeamItemLayoutBinding binding;

        public ItemViewHolder(@NonNull View itemView, NewsForTeamItemLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }

    class ProgressDialogViewHolder extends RecyclerView.ViewHolder {

        public ProgressDialogViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

package com.example.footballnewsmanager.adapters.news_info;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsInfoHeaderBinding;
import com.example.footballnewsmanager.databinding.NewsInfoTeamLayoutBinding;
import com.example.footballnewsmanager.fragments.main.news_info.NewsInfoTeamViewModel;
import com.example.footballnewsmanager.models.UserNews;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class NewsInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserTeam> items = new ArrayList<>();
    private List<NewsInfoTeamViewModel> viewModels = new ArrayList<>();

    private Activity activity;
    public static final int HEADER = 0;
    public static final int ITEM = 1;
    private UserNews userNews;

    public NewsInfoAdapter(Activity activity){
        this.activity = activity;
    }

    public void setUserNews(UserNews userNews) {
        this.userNews = userNews;
    }

    public void setItems(List<UserTeam> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == HEADER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_info_header, parent, false);
            NewsInfoHeaderBinding binding = NewsInfoHeaderBinding.bind(itemView);
            return new HeaderViewHolder(itemView, binding);
        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_info_team_layout, parent, false);
        NewsInfoTeamLayoutBinding binding = NewsInfoTeamLayoutBinding.bind(itemView);
        return new ItemViewHolder(itemView, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(position == 0){
            NewsInfoHeaderViewModel newsInfoHeaderViewModel = new NewsInfoHeaderViewModel();
            ((HeaderViewHolder)holder).binding.setViewModel(newsInfoHeaderViewModel);
            newsInfoHeaderViewModel.init(userNews);
            return;
        }
        NewsInfoTeamViewModel viewModel;
        if(viewModels.size() <= position){
            viewModel = new NewsInfoTeamViewModel();
            viewModels.add(viewModel);
            ((ItemViewHolder)holder).binding.setViewModel(viewModel);
            viewModel.init(items.get(position-1), activity);
        }
        else{
            viewModel = viewModels.get(position-1);
            ((ItemViewHolder)holder).binding.setViewModel(viewModel);
            viewModel.init(items.get(position-1), activity);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return HEADER;
        }
        return ITEM;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        NewsInfoHeaderBinding binding;

        public HeaderViewHolder(@NonNull View itemView, NewsInfoHeaderBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        NewsInfoTeamLayoutBinding binding;

        public ItemViewHolder(@NonNull View itemView, NewsInfoTeamLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}

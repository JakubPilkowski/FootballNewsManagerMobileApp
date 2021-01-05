package com.example.footballnewsmanager.adapters.news.newsForTeam;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsForTeamHeaderBinding;
import com.example.footballnewsmanager.databinding.NewsForTeamItemLayoutBinding;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class NewsForTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserNews> items = new ArrayList<>();
    private List<NewsForTeamAdapterViewModel> viewModels = new ArrayList<>();

    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_LOADING = 2;

    public boolean isLoading = false;
    private ExtendedRecyclerViewItemsListener<UserTeam> headerExtendedRecyclerViewItemsListener;
    private Long countAll;
    private Long countToday;
    private String name;
    private String img;
    private boolean isFavourite;
    private Long id;

    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start + 1, items.size());
    }

    public List<UserNews> getItems() {
        return items;
    }

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public void setCountToday(Long countToday) {
        this.countToday = countToday;
    }


    public NewsForTeamAdapter(Activity activity) {
        this.activity = activity;
    }


    public void setLoading(boolean loading) {
        isLoading = loading;
        if(!isLoading)
            notifyItemChanged(items.size()+1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_header, parent, false);
                NewsForTeamHeaderBinding newsForTeamHeaderBinding = NewsForTeamHeaderBinding.bind(view);
                return new NewsHeaderViewHolder(view, newsForTeamHeaderBinding);
            }
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_item_layout, parent, false);
                NewsForTeamItemLayoutBinding allNewsLayoutBinding = NewsForTeamItemLayoutBinding.bind(view);
                return new NewsViewHolder(view, allNewsLayoutBinding);
            }
            case ITEM_LOADING: {
                Log.d("News", "onCreateViewHolder IsLoading");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_item_layout, parent, false);
                NewsForTeamItemLayoutBinding allNewsLayoutBinding = NewsForTeamItemLayoutBinding.bind(view);
                return new NewsViewHolder(view, allNewsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + 1 && isLoading) {
            return;
        } else if (position == 0) {
            NewsForTeamHeaderAdapterViewModel viewModel = new NewsForTeamHeaderAdapterViewModel();
            NewsForTeamHeaderBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            viewModel.init(id, name, img, isFavourite, countAll, countToday, headerExtendedRecyclerViewItemsListener, binding);
            binding.setViewModel(viewModel);
            return;
        } else {
            NewsForTeamAdapterViewModel viewModel;
            int itemsPosition = position - 1;
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsForTeamAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            viewModel.init(items.get(itemsPosition), activity);
            ((NewsViewHolder) holder).binding.setViewModel(viewModel);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == items.size() + 1) {
            return ITEM_LOADING;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (isLoading)
            return items.size() + 2;
        return items.size() + 1;
    }


    public void setHeaderItems(Long id, String name, String img, boolean isFavourite) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.isFavourite = isFavourite;
    }

    public void setHeaderExtendedRecyclerViewItemsListener(ExtendedRecyclerViewItemsListener<UserTeam> headerExtendedRecyclerViewItemsListener) {
        this.headerExtendedRecyclerViewItemsListener = headerExtendedRecyclerViewItemsListener;
    }

    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        NewsForTeamHeaderBinding binding;

        public NewsHeaderViewHolder(@NonNull View itemView, NewsForTeamHeaderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsForTeamHeaderBinding getBinding() {
            return binding;
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        NewsForTeamItemLayoutBinding binding;

        public NewsViewHolder(@NonNull View itemView, NewsForTeamItemLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsForTeamItemLayoutBinding getBinding() {
            return binding;
        }
    }


    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

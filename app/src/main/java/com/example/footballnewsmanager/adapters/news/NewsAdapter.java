package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.databinding.NewsHeaderLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsItemsPlaceholderBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.NewsView;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserNews> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();

    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_LOADING = 3;
    private final static int PLACEHOLDER = 4;

    public boolean isLoading = false;
    private boolean isPlaceholder = false;
    private ExtendedRecyclerViewItemsListener<UserNews> extendedRecyclerViewItemsListener;
    private BadgeListener badgeListener;
    private Long countAll;
    private Long countToday;


    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start + 1, items.size());
    }

    public void setPlaceholder(boolean placeholder) {
        isPlaceholder = placeholder;
        notifyDataSetChanged();
    }

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public void setCountToday(Long countToday) {
        this.countToday = countToday;
    }

    public void refresh(NewsResponse userNews) {
        items.clear();
        viewModels.clear();
        notifyItemRangeRemoved(0, items.size() + 2);
        setItems(userNews.getUserNews());
    }

    public void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }


    public NewsAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (!isLoading)
            notifyItemChanged(items.size() + 1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_header_layout, parent, false);
                NewsHeaderLayoutBinding newsHeaderLayoutBinding = NewsHeaderLayoutBinding.bind(view);
                return new NewsHeaderViewHolder(view, newsHeaderLayoutBinding);
            }
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
                NewsLayoutBinding allNewsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, allNewsLayoutBinding);
            }
            case ITEM_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            case PLACEHOLDER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items_placeholder, parent, false);
                NewsItemsPlaceholderBinding newsPlaceholderBinding = NewsItemsPlaceholderBinding.bind(view);
                return new PlaceholderViewHolder(view, newsPlaceholderBinding);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
                NewsLayoutBinding allNewsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, allNewsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + 1 && !isPlaceholder)
            return;
        else if (position == items.size() + 1 && isPlaceholder) {
            NewsAdapterPlaceholderViewModel viewModel = new NewsAdapterPlaceholderViewModel();
            NewsItemsPlaceholderBinding binding = ((PlaceholderViewHolder) holder).binding;
            viewModel.init(activity, binding);
            binding.setViewModel(viewModel);
        } else if (position == 0) {
            NewsHeaderAdapterViewModel viewModel = new NewsHeaderAdapterViewModel();
            viewModel.init(countAll, countToday, extendedRecyclerViewItemsListener, activity);
            NewsHeaderLayoutBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        } else {
            NewsAdapterViewModel viewModel;
            int itemsPosition = position - 1;
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            viewModel.init(items.get(itemsPosition), activity, extendedRecyclerViewItemsListener, badgeListener, NewsView.SELECTED);
            NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        }

    }

    public void setExtendedRecyclerViewItemsListener(ExtendedRecyclerViewItemsListener<UserNews> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == items.size() + 1) {
            return isPlaceholder ? PLACEHOLDER : ITEM_LOADING;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (!isLoading && !isPlaceholder)
            return items.size() + 1;
        return items.size() + 2;
    }


    public void onChange(UserNews oldNews, UserNews newNews) {
        int index = items.indexOf(oldNews);
        items.set(index, newNews);
        viewModels.get(index).update(newNews);
    }

    public void changeIfExists(UserNews oldUserNews, UserNews newUserNews){
        for (UserNews userNews : items) {
            News news = userNews.getNews();
            News oldNews = oldUserNews.getNews();
            if(news.getId().equals(oldNews.getId()) && news.getSiteId().equals(oldNews.getSiteId())){
                int index = items.indexOf(userNews);
                items.set(index, newUserNews);
                if(viewModels.size() > index){
                    viewModels.get(index).update(newUserNews);
                }
                break;
            }
        }
    }

    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        private NewsHeaderLayoutBinding binding;

        NewsHeaderViewHolder(@NonNull View itemView, NewsHeaderLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsHeaderLayoutBinding getBinding() {
            return binding;
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private NewsLayoutBinding binding;

        NewsViewHolder(@NonNull View itemView, NewsLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsLayoutBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {

        private NewsItemsPlaceholderBinding binding;

        PlaceholderViewHolder(@NonNull View itemView, NewsItemsPlaceholderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

    }
}

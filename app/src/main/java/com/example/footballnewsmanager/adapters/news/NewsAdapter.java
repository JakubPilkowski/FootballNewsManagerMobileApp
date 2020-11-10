package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsHeaderLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsHighlightedLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.interfaces.NewsPropertiesListener;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NewsPropertiesListener {

    private List<UserNews> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();
    private Activity activity;
    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_HIGHLIGHTED = 2;
    private final static int ADDITIONAL_INFO = 3;
    private final static int ITEM_LOADING = 4;
    private Long countAll;
    private Long countToday;
    public boolean isLoading = false;


    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public NewsAdapter(Activity activity) {
        this.activity = activity;
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
                NewsLayoutBinding newsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, newsLayoutBinding);
            }
            case ITEM_HIGHLIGHTED: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_highlighted_layout, parent, false);
                NewsHighlightedLayoutBinding newsLayoutBinding = NewsHighlightedLayoutBinding.bind(view);
                return new NewsHighlightedViewHolder(view, newsLayoutBinding);
            }
            case ITEM_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
                NewsLayoutBinding newsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, newsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + 1)
            return;
        else if (position == 0) {
            NewsHeaderAdapterViewModel viewModel = new NewsHeaderAdapterViewModel();
            viewModel.init(countAll, countToday);
            NewsHeaderLayoutBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
            return;
        }
        NewsAdapterViewModel viewModel;
        if (viewModels.size() <= position - 1) {
            viewModel = new NewsAdapterViewModel();
            viewModels.add(viewModel);
        } else {
            viewModel = viewModels.get(position - 1);
        }
        if(items.get(position-1).getNews().isHighlighted()){
            viewModel.init(items.get(position - 1), activity, this);
            NewsHighlightedLayoutBinding binding = ((NewsHighlightedViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        }else{
            viewModel.init(items.get(position - 1), activity, this);
            NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        if (position == items.size() + 1)
            return ITEM_LOADING;
        else {
            UserNews news = items.get(position - 1);
            if (news.getNews().isHighlighted()) {
                return ITEM_HIGHLIGHTED;
            } else {
                return ITEM;
            }
        }
    }

    @Override
    public int getItemCount() {
//        if(isLoading)
//            return items.size()+1;
        return items.size() + 2;
    }

    @Override
    public void onChange(UserNews oldNews, UserNews newNews) {
        int index = items.indexOf(oldNews);
        items.set(index, newNews);
        viewModels.get(index).update(newNews);
    }

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public void setCountToday(Long countToday) {
        this.countToday = countToday;
    }


    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        NewsHeaderLayoutBinding binding;

        public NewsHeaderViewHolder(@NonNull View itemView, NewsHeaderLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsHeaderLayoutBinding getBinding() {
            return binding;
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        NewsLayoutBinding binding;

        public NewsViewHolder(@NonNull View itemView, NewsLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsLayoutBinding getBinding() {
            return binding;
        }
    }

    public class NewsHighlightedViewHolder extends RecyclerView.ViewHolder {

        NewsHighlightedLayoutBinding binding;

        public NewsHighlightedViewHolder(@NonNull View itemView, NewsHighlightedLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsHighlightedLayoutBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

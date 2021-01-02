package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.databinding.NewsHeaderLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsHighlightedLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsItemsPlaceholderBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserNews> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();

    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_HIGHLIGHTED = 2;
    private final static int ITEM_LOADING = 3;
    private final static int PLACEHOLDER = 4;

    public boolean isLoading = false;
    public boolean isPlaceholder = false;
    private RecyclerViewItemsListener recyclerViewItemsListener;
    private BadgeListener badgeListener;
    private Long countAll;
    private Long countToday;
    private Long id;
    private String name;
    private String img;


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
        notifyItemRangeRemoved(0, items.size()+2);
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
        if(!isLoading)
            notifyItemChanged(items.size()+1);
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
            case ITEM_HIGHLIGHTED: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_highlighted_layout, parent, false);
                NewsHighlightedLayoutBinding allNewsHighlightedLayoutBinding = NewsHighlightedLayoutBinding.bind(view);
                return new NewsHighlightedViewHolder(view, allNewsHighlightedLayoutBinding);
            }
            case ITEM_LOADING: {
                Log.d("News", "onCreateViewHolder IsLoading");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            case PLACEHOLDER: {
                Log.d("News", "onCreateViewHolder Placeholder");
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
            View bottleView = ((PlaceholderViewHolder) holder).itemView.findViewById(R.id.news_items_placeholder_bottle);
            Animation animation = AnimationUtils.loadAnimation(bottleView.getContext(), R.anim.shake);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    bottleView.startAnimation(animation);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    onAnimationRepeat(animation);

                }

                @Override
                public void onAnimationRepeat(final Animation animation) {
                    new Handler().postDelayed(() -> onAnimationStart(animation), 750);
                }
            });
            bottleView.startAnimation(animation);
            NewsAdapterPlaceholderViewModel viewModel = new NewsAdapterPlaceholderViewModel();
            viewModel.init(activity, recyclerViewItemsListener);
            NewsItemsPlaceholderBinding binding = ((PlaceholderViewHolder) holder).binding;
            binding.setViewModel(viewModel);
            return;
        } else if (position == 0) {
            NewsHeaderAdapterViewModel viewModel = new NewsHeaderAdapterViewModel();
            viewModel.init(countAll, countToday, recyclerViewItemsListener, activity);
            NewsHeaderLayoutBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
            return;
        }
        else{
            NewsAdapterViewModel viewModel;
            int itemsPosition = position - 1;
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            if (items.get(itemsPosition).getNews().isHighlighted()) {
                viewModel.init(items.get(itemsPosition), activity, recyclerViewItemsListener, badgeListener);
                NewsHighlightedLayoutBinding binding = ((NewsHighlightedViewHolder) holder).getBinding();
                binding.setViewModel(viewModel);
            } else {
                viewModel.init(items.get(itemsPosition), activity, recyclerViewItemsListener, badgeListener);
                NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
                binding.setViewModel(viewModel);
            }
        }

    }

    public void setRecyclerViewItemsListener(RecyclerViewItemsListener recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == items.size() + 1) {
            return isPlaceholder ? PLACEHOLDER : ITEM_LOADING;
        } else {
            UserNews news = items.get(position - 1);
            return news.getNews().isHighlighted() ? ITEM_HIGHLIGHTED : ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if(!isLoading && !isPlaceholder)
            return items.size() + 1;
        return items.size() + 2;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof PlaceholderViewHolder) {
            recyclerViewItemsListener.onDetached();
        }
    }

    public void onChange(UserNews oldNews, UserNews newNews) {
        int index = items.indexOf(oldNews);
        items.set(index, newNews);
        viewModels.get(index).update(newNews);
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

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {

        NewsItemsPlaceholderBinding binding;

        public PlaceholderViewHolder(@NonNull View itemView, NewsItemsPlaceholderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

    }
}

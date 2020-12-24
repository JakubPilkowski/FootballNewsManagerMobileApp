package com.example.footballnewsmanager.adapters.news.newsForTeam;

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
import com.example.footballnewsmanager.activites.news_for_team.NewsForTeamViewModel;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.databinding.NewsForTeamHeaderBinding;
import com.example.footballnewsmanager.databinding.NewsForTeamItemLayoutBinding;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class NewsForTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserNews> items = new ArrayList<>();
    private List<NewsForTeamAdapterViewModel> viewModels = new ArrayList<>();

    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_LOADING = 2;
    private final static int PLACEHOLDER = 3;

    public boolean isLoading = false;
    public boolean isPlaceholder = false;
    private NewsRecyclerViewListener newsRecyclerViewListener;
    private Long countAll;
    private Long countToday;
    private String name;
    private String img;


    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start + 1, items.size());
    }

    public void setPlaceholder(boolean placeholder) {
        Log.d(NewsForTeamViewModel.TAG, "setPlaceholder: ");
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


    public NewsForTeamAdapter(Activity activity) {
        this.activity = activity;
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
            case PLACEHOLDER: {
                Log.d("News", "onCreateViewHolder Placeholder");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_placeholder, parent, false);
                return new PlaceholderViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_for_team_item_layout, parent, false);
                NewsForTeamItemLayoutBinding allNewsLayoutBinding = NewsForTeamItemLayoutBinding.bind(view);
                return new NewsViewHolder(view, allNewsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + 1 && !isPlaceholder)
            return;
        else if (position == items.size() + 1 && isPlaceholder) {
            View bottleView = ((PlaceholderViewHolder) holder).itemView.findViewById(R.id.news_for_team_placeholder_bottle);
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
            return;
        } else if (position == 0) {
            NewsForTeamHeaderAdapterViewModel viewModel = new NewsForTeamHeaderAdapterViewModel();
            viewModel.init(name, img, countAll, countToday);
            NewsForTeamHeaderBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
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
            viewModel.init(items.get(itemsPosition), activity, newsRecyclerViewListener);
            NewsForTeamItemLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
//
//            if (items.get(itemsPosition).getNews().isHighlighted()) {
//                viewModel.init(items.get(itemsPosition), activity, newsRecyclerViewListener, badgeListener);
//                NewsHighlightedLayoutBinding binding = ((NewsHighlightedViewHolder) holder).getBinding();
//                binding.setViewModel(viewModel);
//            } else {
//                viewModel.init(items.get(itemsPosition), activity, newsRecyclerViewListener, badgeListener);
//                NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
//                binding.setViewModel(viewModel);
//            }
        }

    }

    public void setNewsRecyclerViewListener(NewsRecyclerViewListener newsRecyclerViewListener) {
        this.newsRecyclerViewListener = newsRecyclerViewListener;
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
        return items.size() + 2;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof PlaceholderViewHolder) {
            newsRecyclerViewListener.onDetached();
        }
    }

    public void onChange(UserNews oldNews, UserNews newNews) {
        int index = items.indexOf(oldNews);
        items.set(index, newNews);
        viewModels.get(index).update(newNews);
    }

    public void setHeaderItems(String name, String img) {
        this.name = name;
        this.img = img;
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

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {

        public PlaceholderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

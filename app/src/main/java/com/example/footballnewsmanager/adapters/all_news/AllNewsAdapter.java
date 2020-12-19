package com.example.footballnewsmanager.adapters.all_news;

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
import com.example.footballnewsmanager.adapters.news.NewsAdapterPlaceholderViewModel;
import com.example.footballnewsmanager.adapters.news.NewsAdapterViewModel;
import com.example.footballnewsmanager.adapters.news.NewsAdditionalInfoViewModel;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.api.responses.main.BaseNewsAdjustment;
import com.example.footballnewsmanager.api.responses.main.NewsInfoType;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsBinding;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamsBinding;
import com.example.footballnewsmanager.databinding.AllNewsHeaderBinding;
import com.example.footballnewsmanager.databinding.NewsHighlightedLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsPlaceholderBinding;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class AllNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserNews> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();
    private List<NewsAdditionalInfoViewModel> additionalInfoViewModels = new ArrayList<>();
    private List<BaseNewsAdjustment> additionalItems = new ArrayList<>();
    private Activity activity;
    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ITEM_HIGHLIGHTED = 2;
    private final static int ADDITIONAL_INFO_NEWS = 3;
    private final static int ADDITIONAL_INFO_TEAMS = 4;
    private final static int ITEM_LOADING = 5;
    private final static int PLACEHOLDER = 6;
    private Long countAll;
    private Long countToday;
    public boolean isLoading = false;
    public boolean isPlaceholder = false;
    private NewsRecyclerViewListener newsRecyclerViewListener;
    private BadgeListener badgeListener;

    public void setItems(List<UserNews> items, BaseNewsAdjustment additionalContent) {
        int start = this.items.size() + this.additionalItems.size();
        this.items.addAll(items);
        this.additionalItems.add(additionalContent);
        notifyItemRangeChanged(start + 1, items.size() + additionalItems.size());
    }

    public void setPlaceholder(boolean placeholder) {
        isPlaceholder = placeholder;
        notifyDataSetChanged();
    }

    public AllNewsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_news_header, parent, false);
                AllNewsHeaderBinding allNewsHeaderBinding = AllNewsHeaderBinding.bind(view);
                return new NewsHeaderViewHolder(view, allNewsHeaderBinding);
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            case ADDITIONAL_INFO_NEWS: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.additional_info_news, parent, false);
                AdditionalInfoNewsBinding additionalInfoNewsBinding = AdditionalInfoNewsBinding.bind(view);
                return new AdditionalNewsViewHolder(view, additionalInfoNewsBinding);
            }
            case ADDITIONAL_INFO_TEAMS: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.additional_info_teams, parent, false);
                AdditionalInfoTeamsBinding additionalInfoTeamsBinding = AdditionalInfoTeamsBinding.bind(view);
                return new AdditionalTeamsViewHolder(view, additionalInfoTeamsBinding);
            }
            case PLACEHOLDER: {
                Log.d("News", "onCreateViewHolder Placeholder");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_placeholder, parent, false);
                NewsPlaceholderBinding newsPlaceholderBinding = NewsPlaceholderBinding.bind(view);
                return new PlaceholderViewHolder(view, newsPlaceholderBinding);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
                NewsLayoutBinding newsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, newsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + additionalItems.size() + 1 && !isPlaceholder)
            return;
        else if (position == items.size() + additionalItems.size() + 1 && isPlaceholder) {
            View bottleView = ((PlaceholderViewHolder) holder).itemView.findViewById(R.id.news_placeholder_bottle);
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
            viewModel.init(activity, newsRecyclerViewListener);
            NewsPlaceholderBinding binding = ((PlaceholderViewHolder) holder).binding;
            binding.setViewModel(viewModel);
            return;
        } else if (position == 0) {
            AllNewsHeaderViewModel viewModel = new AllNewsHeaderViewModel();
            viewModel.init(countAll, countToday);
            AllNewsHeaderBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
            return;
        }
        //additional content
        else if (position % 16 ==0  || (position == items.size()+additionalItems.size() && (items.size()+additionalItems.size()) % 16 != 0)) {
            NewsAdditionalInfoViewModel viewModel;
            int addContentPosition;
            if((position == items.size()+additionalItems.size() && (items.size()+additionalItems.size()) % 16 != 0)){
                addContentPosition = additionalItems.size()-1;
            }
            else{
                addContentPosition = position/16-1;
            }
            if (additionalInfoViewModels.size() <= addContentPosition) {
                viewModel = new NewsAdditionalInfoViewModel();
                additionalInfoViewModels.add(viewModel);
            } else {
                viewModel = additionalInfoViewModels.get(addContentPosition);
            }
            BaseNewsAdjustment baseNewsAdjustment = additionalItems.get(addContentPosition);
            if (baseNewsAdjustment.getType().equals(NewsInfoType.HOT_NEWS) || baseNewsAdjustment.getType().equals(NewsInfoType.PROPOSED_NEWS)) {
                viewModel.initForNews(baseNewsAdjustment, activity, ((AdditionalNewsViewHolder)holder).binding);
                ((AdditionalNewsViewHolder) holder).binding.setViewModel(viewModel);
            } else if (baseNewsAdjustment.getType().equals(NewsInfoType.PROPOSED_TEAMS)) {
                viewModel.initForTeams(baseNewsAdjustment,activity, ((AdditionalTeamsViewHolder)holder).binding);
                ((AdditionalTeamsViewHolder) holder).binding.setViewModel(viewModel);
            }
            return;
        } else {
            NewsAdapterViewModel viewModel;
            int itemsPosition = position - (position/16+1);
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            if (items.get(itemsPosition).getNews().isHighlighted()) {
                viewModel.init(items.get(itemsPosition), activity, newsRecyclerViewListener, badgeListener);
                NewsHighlightedLayoutBinding binding = ((NewsHighlightedViewHolder) holder).getBinding();
                binding.setViewModel(viewModel);
            } else {
                viewModel.init(items.get(itemsPosition), activity, newsRecyclerViewListener, badgeListener);
                NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
                binding.setViewModel(viewModel);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Position", String.valueOf(position));
        if (position == 0)
            return HEADER;
        else if (position == items.size() + additionalItems.size() + 1) {
            return isPlaceholder ? PLACEHOLDER : ITEM_LOADING;
        } else {
            //additionalContent
            if (position % 16 == 0 || (position == items.size()+additionalItems.size() && (items.size()+additionalItems.size()) % 16 != 0)) {

                int addContentPosition;
                if((position == items.size()+additionalItems.size() && (items.size()+additionalItems.size()) % 16 != 0)){
                    addContentPosition = additionalItems.size()-1;
                }
                else{
                    addContentPosition = position/16-1;
                }
                switch (additionalItems.get(addContentPosition).getType()){
                    case HOT_NEWS:
                    case PROPOSED_NEWS:
                        return ADDITIONAL_INFO_NEWS;
                    case PROPOSED_TEAMS:
                        return ADDITIONAL_INFO_TEAMS;
                }
            }

            UserNews news = items.get(position - (position/16 +1));
            return news.getNews().isHighlighted() ? ITEM_HIGHLIGHTED : ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return items.size()+additionalItems.size() + 2;
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

    public void setCountAll(Long countAll) {
        this.countAll = countAll;
    }

    public void setCountToday(Long countToday) {
        this.countToday = countToday;
    }

    public void setNewsRecyclerViewListener(NewsRecyclerViewListener newsRecyclerViewListener) {
        this.newsRecyclerViewListener = newsRecyclerViewListener;
    }

    public void refresh(AllNewsResponse userNews) {
        items.clear();
        additionalItems.clear();
        viewModels.clear();
        additionalInfoViewModels.clear();
        notifyItemRangeRemoved(0, items.size()+additionalItems.size()+2);
        setItems(userNews.getUserNews(), userNews.getAdditionalContent());
    }

    public void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }


    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        AllNewsHeaderBinding binding;

        public NewsHeaderViewHolder(@NonNull View itemView, AllNewsHeaderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AllNewsHeaderBinding getBinding() {
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

    public class AdditionalNewsViewHolder extends RecyclerView.ViewHolder {
        private AdditionalInfoNewsBinding binding;

        public AdditionalNewsViewHolder(@NonNull View itemView, AdditionalInfoNewsBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AdditionalInfoNewsBinding getBinding() {
            return binding;
        }
    }


    public class AdditionalTeamsViewHolder extends RecyclerView.ViewHolder {
        private AdditionalInfoTeamsBinding binding;

        public AdditionalTeamsViewHolder(@NonNull View itemView, AdditionalInfoTeamsBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AdditionalInfoTeamsBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class PlaceholderViewHolder extends RecyclerView.ViewHolder {

        NewsPlaceholderBinding binding;

        public PlaceholderViewHolder(@NonNull View itemView, NewsPlaceholderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

    }
}

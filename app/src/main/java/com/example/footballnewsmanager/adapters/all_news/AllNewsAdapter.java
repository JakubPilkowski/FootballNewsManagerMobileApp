package com.example.footballnewsmanager.adapters.all_news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.NewsAdapterViewModel;
import com.example.footballnewsmanager.adapters.news.NewsAdditionalInfoViewModel;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamsBinding;
import com.example.footballnewsmanager.databinding.AllNewsHeaderBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.ExtendedRecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserNews;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class AllNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserNews> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();
    private List<NewsAdditionalInfoViewModel> additionalInfoViewModels = new ArrayList<>();
    private List<List<UserTeam>> proposedTeams = new ArrayList<>();
    private Activity activity;
    private final static int HEADER = 0;
    private final static int ITEM = 1;
    private final static int ADDITIONAL_INFO_TEAMS = 2;
    private final static int ITEM_LOADING = 3;
    private Long countAll;
    private Long countToday;
    public boolean isLoading = false;
    private ExtendedRecyclerViewItemsListener<UserNews> extendedRecyclerViewItemsListener;
    private BadgeListener badgeListener;
    private boolean proposed;

    public void setItems(List<UserNews> items, List<UserTeam> additionalContent) {
        int start = this.items.size() + this.proposedTeams.size();
        this.items.addAll(items);
        this.proposedTeams.add(additionalContent);
        notifyItemRangeChanged(start + 1, items.size() + proposedTeams.size());
    }
    public void setItems(List<UserNews> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start + 1, items.size());
    }

    public AllNewsAdapter(Activity activity, boolean proposed) {
        this.proposed = proposed;
        this.activity = activity;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (!isLoading)
            notifyItemChanged(items.size() + proposedTeams.size() + 1);
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
            case ITEM_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            case ADDITIONAL_INFO_TEAMS: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.additional_info_teams, parent, false);
                AdditionalInfoTeamsBinding additionalInfoTeamsBinding = AdditionalInfoTeamsBinding.bind(view);
                return new AdditionalTeamsViewHolder(view, additionalInfoTeamsBinding);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
                NewsLayoutBinding newsLayoutBinding = NewsLayoutBinding.bind(view);
                return new NewsViewHolder(view, newsLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size() + proposedTeams.size() + 1 && isLoading)
            return;
        else if (position == 0) {
            AllNewsHeaderViewModel viewModel = new AllNewsHeaderViewModel();
            viewModel.init(countAll, countToday, activity);
            AllNewsHeaderBinding binding = ((NewsHeaderViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        }
        //additional content
        else if (proposed && (position % 16 == 0 || (position == items.size() + proposedTeams.size() && (items.size() + proposedTeams.size()) % 16 != 0))) {
            NewsAdditionalInfoViewModel viewModel;
            int addContentPosition;
            if ((position == items.size() + proposedTeams.size() && (items.size() + proposedTeams.size()) % 16 != 0)) {
                addContentPosition = proposedTeams.size() - 1;
            } else {
                addContentPosition = position / 16 - 1;
            }
            if (additionalInfoViewModels.size() <= addContentPosition) {
                viewModel = new NewsAdditionalInfoViewModel();
                additionalInfoViewModels.add(viewModel);
            } else {
                viewModel = additionalInfoViewModels.get(addContentPosition);
            }
            List<UserTeam> teams = proposedTeams.get(addContentPosition);
            viewModel.initForTeams(teams, activity, ((AdditionalTeamsViewHolder) holder).binding);
            ((AdditionalTeamsViewHolder) holder).binding.setViewModel(viewModel);
        } else {
            NewsAdapterViewModel viewModel;
            int itemsPosition = proposed ? (position - position/16-1) : position-1;
            if (viewModels.size() <= itemsPosition) {
                viewModel = new NewsAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(itemsPosition);
            }
            viewModel.init(items.get(itemsPosition), activity, extendedRecyclerViewItemsListener, badgeListener);
            NewsLayoutBinding binding = ((NewsViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == items.size() + proposedTeams.size() + 1) {
            return ITEM_LOADING;
        } else if (proposed && (position % 16 == 0 || (position == items.size() + proposedTeams.size() && (items.size() + proposedTeams.size()) % 16 != 0))) {
            return ADDITIONAL_INFO_TEAMS;
        }
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if (isLoading)
            return items.size() + proposedTeams.size() + 2;
        return items.size() + proposedTeams.size() + 1;
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

    public void setExtendedRecyclerViewItemsListener(ExtendedRecyclerViewItemsListener<UserNews> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }

    public void refresh(AllNewsResponse userNews) {
        items.clear();
        proposedTeams.clear();
        viewModels.clear();
        additionalInfoViewModels.clear();
        notifyItemRangeRemoved(0, items.size() + proposedTeams.size() + 2);
        setItems(userNews.getUserNews(), userNews.getProposedTeams());
    }

    public void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }


    public class NewsHeaderViewHolder extends RecyclerView.ViewHolder {

        private AllNewsHeaderBinding binding;

        NewsHeaderViewHolder(@NonNull View itemView, AllNewsHeaderBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AllNewsHeaderBinding getBinding() {
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

    public class AdditionalTeamsViewHolder extends RecyclerView.ViewHolder {
        private AdditionalInfoTeamsBinding binding;

        AdditionalTeamsViewHolder(@NonNull View itemView, AdditionalInfoTeamsBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public AdditionalInfoTeamsBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

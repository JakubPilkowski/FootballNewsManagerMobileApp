package com.example.footballnewsmanager.adapters.proposed_teams;

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
import com.example.footballnewsmanager.adapters.news.NewsAdapter;
import com.example.footballnewsmanager.adapters.news.NewsAdapterPlaceholderViewModel;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.NewsHighlightedLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.databinding.NewsPlaceholderBinding;
import com.example.footballnewsmanager.databinding.ProposedTeamLayoutBinding;
import com.example.footballnewsmanager.interfaces.NewsRecyclerViewListener;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserNews;

import java.util.ArrayList;
import java.util.List;

public class ProposedTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProposedTeamsAdapterViewModel> viewModels = new ArrayList<>();
    private List<Team> items = new ArrayList<>();

    private final static int ITEM = 0;
    private final static int ITEM_LOADING = 1;
    private final static int PLACEHOLDER = 2;
    public boolean isLoading = false;
    public boolean isPlaceholder = false;
    private NewsRecyclerViewListener newsRecyclerViewListener;


    public void setItems(List<Team> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public void setPlaceholder(boolean placeholder) {
        isPlaceholder = placeholder;
        notifyDataSetChanged();
    }

    public void setNewsRecyclerViewListener(NewsRecyclerViewListener newsRecyclerViewListener) {
        this.newsRecyclerViewListener = newsRecyclerViewListener;
    }

    public List<ProposedTeamsAdapterViewModel> getViewModels() {
        return viewModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_team_layout, parent, false);
                ProposedTeamLayoutBinding proposedTeamLayoutBinding = ProposedTeamLayoutBinding.bind(view);
                return new TeamsViewHolder(view, proposedTeamLayoutBinding);
            }
            case ITEM_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            case PLACEHOLDER: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_teams_placeholder, parent, false);
                return new PlaceholderViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_team_layout, parent, false);
                ProposedTeamLayoutBinding proposedTeamLayoutBinding = ProposedTeamLayoutBinding.bind(view);
                return new TeamsViewHolder(view, proposedTeamLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if ((position == items.size() && !isPlaceholder) || (position==items.size() && isPlaceholder))
            return;
        else {
            ProposedTeamsAdapterViewModel viewModel;
            if (viewModels.size() <= position) {
                viewModel = new ProposedTeamsAdapterViewModel();
                viewModels.add(viewModel);
                ((TeamsViewHolder)holder).getBinding().setViewModel(viewModel);
                viewModel.init(items.get(position));
            } else {
                viewModel = viewModels.get(position);
                ((TeamsViewHolder)holder).getBinding().setViewModel(viewModel);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == items.size()) {
            return isPlaceholder ? PLACEHOLDER : ITEM_LOADING;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof PlaceholderViewHolder) {
            newsRecyclerViewListener.onDetached();
        }
    }


    public class TeamsViewHolder extends RecyclerView.ViewHolder {

        ProposedTeamLayoutBinding binding;

        public TeamsViewHolder(@NonNull View itemView, ProposedTeamLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ProposedTeamLayoutBinding getBinding() {
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

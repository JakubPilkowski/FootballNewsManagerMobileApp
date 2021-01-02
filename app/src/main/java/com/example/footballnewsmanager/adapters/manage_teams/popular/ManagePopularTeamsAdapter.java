package com.example.footballnewsmanager.adapters.manage_teams.popular;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.manage_teams.teams.ManageTeamsViewModel;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class ManagePopularTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private List<UserTeam> items = new ArrayList<>();
    private List<ManageTeamsViewModel> viewModels = new ArrayList<>();

    private final static int ITEM = 1;
    private final static int ITEM_LOADING = 2;

    public boolean isLoading = false;

    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    public void setItems(List<UserTeam> items, int currentPage) {
        if (currentPage == 0) {
            this.items.clear();
        }
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if(!isLoading)
            notifyItemChanged(items.size());
    }

    public ManagePopularTeamsAdapter(Activity activity) {
        this.activity = activity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_team_item, parent, false);
                ManageTeamItemBinding manageTeamItemBinding = ManageTeamItemBinding.bind(view);
                return new TeamViewHolder(view, manageTeamItemBinding);
            }
            case ITEM_LOADING: {
                Log.d("News", "onCreateViewHolder IsLoading");
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_team_item, parent, false);
                ManageTeamItemBinding manageTeamItemBinding = ManageTeamItemBinding.bind(view);
                return new TeamViewHolder(view, manageTeamItemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == items.size())
            return;
        else {
            ManageTeamsViewModel viewModel;
            if (viewModels.size() <= position) {
                viewModel = new ManageTeamsViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(position);
            }
            ManageTeamItemBinding binding = ((TeamViewHolder) holder).getBinding();
            binding.setViewModel(viewModel);
            viewModel.init(items.get(position), activity, recyclerViewItemsListener, binding);
        }

    }

    public void setRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == items.size()) {
            return ITEM_LOADING;
        } else {
            return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (isLoading)
            return items.size() + 1;
        return items.size();
    }

    public void onChange(UserTeam oldTeam, UserTeam newTeam) {
        boolean exist = false;
        int index = 0;
        for (UserTeam userTeam : items) {
            if (userTeam.getTeam().getId().equals(oldTeam.getTeam().getId())) {
                exist = true;
                index = items.indexOf(userTeam);
                break;
            }
        }
        if (exist) {
            items.set(index, newTeam);
            if (viewModels.size() >= index) {
                viewModels.get(index).update(newTeam);
            }
        }
    }


    public class TeamViewHolder extends RecyclerView.ViewHolder {

        ManageTeamItemBinding binding;

        public TeamViewHolder(@NonNull View itemView, ManageTeamItemBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ManageTeamItemBinding getBinding() {
            return binding;
        }
    }


    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

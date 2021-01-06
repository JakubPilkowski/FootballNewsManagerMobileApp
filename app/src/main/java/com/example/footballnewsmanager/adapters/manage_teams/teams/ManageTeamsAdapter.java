package com.example.footballnewsmanager.adapters.manage_teams.teams;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class ManageTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewItemsListener<UserTeam> {

    private List<ManageTeamsViewModel> viewModels = new ArrayList<>();

    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    private List<UserTeam> items = new ArrayList<>();
    private final static int ITEM = 1;
    private final static int ITEM_LOADING = 2;

    public void setItems(List<UserTeam> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public boolean isLoading = false;

    public void setLoading(boolean loading) {
        isLoading = loading;
        if(!isLoading)
            notifyItemChanged(items.size());
    }


    private Activity activity;

    public ManageTeamsAdapter(Activity activity){
        this.activity = activity;
    }

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }


    public void onChange(UserTeam oldTeam, UserTeam newTeam) {
        int index = items.indexOf(oldTeam);
        items.set(index, newTeam);
        viewModels.get(index).update(newTeam);
    }


    @Override
    public void onChangeItem(UserTeam oldUserItem, UserTeam newUserItem) {
        onChange(oldUserItem, newUserItem);
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
            viewModel.init(items.get(position), activity, extendedRecyclerViewItemsListener, binding);
        }
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
        if(isLoading)
            return items.size()+1;
        return items.size();
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        ManageTeamItemBinding binding;

        TeamViewHolder(@NonNull View itemView, ManageTeamItemBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ManageTeamItemBinding getBinding() {
            return binding;
        }
    }


    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

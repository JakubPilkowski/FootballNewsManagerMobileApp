package com.example.footballnewsmanager.adapters.manage_teams.selected;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.manage_teams.teams.ManageTeamsViewModel;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class SelectedTeamsAdapter extends BaseRecyclerViewAdapter<UserTeam, BaseViewHolder> {

    private List<ManageTeamsViewModel> viewModels = new ArrayList<>();
    private RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener;

    @Override
    public int getItemLayoutRes() {
        return R.layout.manage_team_item;
    }

    private Activity activity;

    public SelectedTeamsAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        ManageTeamItemBinding binding = ManageTeamItemBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        ManageTeamsViewModel viewModel;


        if (viewModels.size() <= position) {
            viewModel = new ManageTeamsViewModel();
            viewModels.add(viewModel);
            ((ManageTeamItemBinding) holder.getBinding()).setViewModel(viewModel);
            viewModel.init(items.get(position),activity, extendedRecyclerViewItemsListener, (ManageTeamItemBinding) holder.getBinding());
        } else {
            viewModel = viewModels.get(position);
            ((ManageTeamItemBinding) holder.getBinding()).setViewModel(viewModel);
            viewModel.init(items.get(position),activity, extendedRecyclerViewItemsListener, (ManageTeamItemBinding) holder.getBinding());
        }

    }

    public void onChange(UserTeam oldTeam, UserTeam newTeam) {
        boolean exist = false;
        int index = 0;
        for (UserTeam userTeam : items) {
            if(userTeam.getTeam().getId().equals(oldTeam.getTeam().getId())){
                exist = true;
                index = items.indexOf(userTeam);
                break;
            }
        }

        if (exist) {
            items.remove(index);
            notifyItemRemoved(index);
        } else {
            int startPosition = items.size();
            items.add(newTeam);
            notifyItemRangeChanged(startPosition, items.size());
        }
    }

    public void setExtendedRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> extendedRecyclerViewItemsListener) {
        this.extendedRecyclerViewItemsListener = extendedRecyclerViewItemsListener;
    }
}

package com.example.footballnewsmanager.adapters.manage_teams.teams;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.ManageTeamItemBinding;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class ManageTeamsAdapter extends BaseRecyclerViewAdapter<UserTeam, BaseViewHolder> implements RecyclerViewItemsListener<UserTeam>{

    private List<ManageTeamsViewModel> viewModels = new ArrayList<>();

    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    @Override
    public int getItemLayoutRes() {
        return R.layout.manage_team_item;
    }

    private Activity activity;

    public ManageTeamsAdapter(Activity activity){
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

        if(viewModels.size() <= position){
            viewModel = new ManageTeamsViewModel();
            viewModels.add(viewModel);
            ((ManageTeamItemBinding)holder.getBinding()).setViewModel(viewModel);
            viewModel.init(items.get(position),activity, recyclerViewItemsListener, this);
        }
        else{
            viewModel = viewModels.get(position);
            ((ManageTeamItemBinding)holder.getBinding()).setViewModel(viewModel);
            viewModel.init(items.get(position),activity, recyclerViewItemsListener, this);
        }

    }

    public void setRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
    }


    public void onChange(UserTeam oldTeam, UserTeam newTeam) {
        int index = items.indexOf(oldTeam);
        items.set(index, newTeam);
        viewModels.get(index).update(newTeam);
    }

    @Override
    public void onDetached() {

    }

    @Override
    public void backToFront() {

    }

    @Override
    public void onChangeItem(UserTeam oldUserItem, UserTeam newUserItem) {
        onChange(oldUserItem, newUserItem);
    }

    @Override
    public void onChangeItems() {

    }
}

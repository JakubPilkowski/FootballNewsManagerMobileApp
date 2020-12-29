package com.example.footballnewsmanager.adapters.manage_teams.all;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.ManageLeagueItemBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.RecyclerViewItemsListener;
import com.example.footballnewsmanager.models.League;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class ManageLeaguesAdapter extends BaseRecyclerViewAdapter<League, BaseViewHolder> {

    private List<ManageLeaguesAdapterViewModel> viewModels = new ArrayList<>();
    private Navigator navigator;
    private RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener;

    public void setRecyclerViewItemsListener(RecyclerViewItemsListener<UserTeam> recyclerViewItemsListener) {
        this.recyclerViewItemsListener = recyclerViewItemsListener;
    }

    public ManageLeaguesAdapter(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.manage_league_item;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        ManageLeagueItemBinding binding = ManageLeagueItemBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        ManageLeaguesAdapterViewModel viewModel;

        if(viewModels.size() <= position){
            viewModel = new ManageLeaguesAdapterViewModel();
            viewModels.add(viewModel);
            holder.setViewModel(viewModel);
            ((ManageLeagueItemBinding) holder.getBinding()).setViewModel(viewModel);
            holder.setElement(items.get(position), navigator, recyclerViewItemsListener);
        }
        else{
            viewModel = viewModels.get(position);
            ((ManageLeagueItemBinding) holder.getBinding()).setViewModel(viewModel);
            holder.setElement(items.get(position), navigator, recyclerViewItemsListener);
        }

    }
}

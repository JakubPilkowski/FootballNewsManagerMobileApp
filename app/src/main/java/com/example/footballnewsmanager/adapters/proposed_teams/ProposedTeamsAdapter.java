package com.example.footballnewsmanager.adapters.proposed_teams;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.ProposedTeamLayoutBinding;
import com.example.footballnewsmanager.models.Team;

import java.util.ArrayList;
import java.util.List;

public class ProposedTeamsAdapter extends BaseRecyclerViewAdapter<Team, BaseViewHolder> {

    private List<ProposedTeamsAdapterViewModel> viewModels = new ArrayList<>();

    @Override
    public int getItemLayoutRes() {
        return R.layout.proposed_team_layout;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        ProposedTeamLayoutBinding proposedTeamLayoutBinding = ProposedTeamLayoutBinding.bind(itemView);
        return new BaseViewHolder(itemView, proposedTeamLayoutBinding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        ProposedTeamsAdapterViewModel viewModel;
        if (viewModels.size() <= position) {
            viewModel = new ProposedTeamsAdapterViewModel();
            viewModels.add(viewModel);
            ((ProposedTeamLayoutBinding) holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position));
        } else {
            viewModel = viewModels.get(position);
            ((ProposedTeamLayoutBinding) holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }
}

package com.example.footballnewsmanager.adapters.news.additionalInfo;

import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapterViewModel;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamBinding;
import com.example.footballnewsmanager.databinding.ProposedSitesLayoutBinding;
import com.example.footballnewsmanager.models.Team;

import java.util.ArrayList;
import java.util.List;

public class AdditionalTeamsAdapter extends BaseRecyclerViewAdapter<Team, BaseViewHolder> {

    private List<AdditionalTeamsAdapterViewModel> viewModels = new ArrayList<>();

    @Override
    public int getItemLayoutRes() {
        return R.layout.additional_info_team;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        AdditionalInfoTeamBinding binding = AdditionalInfoTeamBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        AdditionalTeamsAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new AdditionalTeamsAdapterViewModel();
            viewModels.add(viewModel);
            ((AdditionalInfoTeamBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position));
        }
        else{
            viewModel = viewModels.get(position);
            ((AdditionalInfoTeamBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }
}

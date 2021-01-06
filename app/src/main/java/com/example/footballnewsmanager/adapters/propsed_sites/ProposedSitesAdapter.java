package com.example.footballnewsmanager.adapters.propsed_sites;

import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.ProposedSitesLayoutBinding;
import com.example.footballnewsmanager.models.Site;

import java.util.ArrayList;
import java.util.List;

public class ProposedSitesAdapter extends BaseRecyclerViewAdapter<Site, BaseViewHolder> {

    private List<ProposedSitesAdapterViewModel> viewModels = new ArrayList<>();

    @Override
    public int getItemLayoutRes() {
        return R.layout.proposed_sites_layout;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        ProposedSitesLayoutBinding proposedSitesLayoutBinding = ProposedSitesLayoutBinding.bind(itemView);
        return new BaseViewHolder(itemView, proposedSitesLayoutBinding);
    }

    public List<ProposedSitesAdapterViewModel> getViewModels() {
        return viewModels;
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        ProposedSitesAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new ProposedSitesAdapterViewModel();
            viewModels.add(viewModel);
            ((ProposedSitesLayoutBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position));
        }
        else{
            viewModel = viewModels.get(position);
            ((ProposedSitesLayoutBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }
}

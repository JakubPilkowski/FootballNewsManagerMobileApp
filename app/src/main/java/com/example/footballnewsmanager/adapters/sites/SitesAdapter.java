package com.example.footballnewsmanager.adapters.sites;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseHeadAndItemRVAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.SitesItemBinding;
import com.example.footballnewsmanager.models.Site;

import java.util.ArrayList;
import java.util.List;

public class SitesAdapter extends BaseHeadAndItemRVAdapter<Site, BaseViewHolder, BaseViewHolder> {

    private List<SitesAdapterViewModel> viewModels = new ArrayList<>();

    private Activity activity;

    public SitesAdapter(Activity activity) {
        this.activity = activity;
    }


    @Override
    public BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType, View itemView) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sites_header, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public int getHeaderLayoutRes() {
        return R.layout.sites_header;
    }

    @Override
    public void onBindHeaderView(BaseViewHolder holder, int position) {
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.sites_item;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sites_item, parent, false);
        SitesItemBinding binding = SitesItemBinding.bind(view);
        return new BaseViewHolder(view, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        SitesAdapterViewModel viewModel;
        if (viewModels.size() <= position) {
            viewModel = new SitesAdapterViewModel();
            viewModels.add(viewModel);
            holder.setViewModel(viewModel);
        } else {
            viewModel = viewModels.get(position);
        }
        ((SitesItemBinding) holder.getBinding()).setViewModel(viewModel);
        holder.setElement(items.get(position), activity);
    }
}

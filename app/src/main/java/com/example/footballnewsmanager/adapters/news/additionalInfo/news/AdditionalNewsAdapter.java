package com.example.footballnewsmanager.adapters.news.additionalInfo.news;

import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.additionalInfo.news.AdditionalNewsAdapterViewModel;
import com.example.footballnewsmanager.base.BaseRecyclerViewAdapter;
import com.example.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsBinding;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsBindingImpl;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsBinding;
import com.example.footballnewsmanager.databinding.AdditionalInfoNewsItemBinding;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamBinding;
import com.example.footballnewsmanager.models.Team;

import java.util.ArrayList;
import java.util.List;

public class AdditionalNewsAdapter extends BaseRecyclerViewAdapter<Team, BaseViewHolder> {


    private List<AdditionalNewsAdapterViewModel> viewModels = new ArrayList<>();

    @Override
    public int getItemLayoutRes() {
        return R.layout.additional_info_news_item;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        AdditionalInfoNewsItemBinding binding = AdditionalInfoNewsItemBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        AdditionalNewsAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new AdditionalNewsAdapterViewModel();
            viewModels.add(viewModel);
            ((AdditionalInfoNewsItemBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position));
        }
        else{
            viewModel = viewModels.get(position);
            ((AdditionalInfoNewsItemBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }
}

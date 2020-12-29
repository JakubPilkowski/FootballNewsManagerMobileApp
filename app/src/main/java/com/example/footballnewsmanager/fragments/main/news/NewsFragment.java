package com.example.footballnewsmanager.fragments.main.news;

import androidx.databinding.ViewDataBinding;

import android.content.Intent;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.Providers;

public class NewsFragment extends BaseFragment<NewsFragmentBinding, NewsFragmentViewModel> implements Providers {


    private BadgeListener badgeListener;

    public static final String TAG = "NewsFragment";

    public static NewsFragment newInstance(BadgeListener badgeListener) {
        NewsFragment fragment = new NewsFragment();
        fragment.setBadgeListener(badgeListener);
        return fragment;
    }

    public void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }

    @Override
    public void onResume() {
        if(getActivity().getIntent()!= null){
            Intent intent = getActivity().getIntent();
            if(intent.getStringExtra("restart") != null && intent.getStringExtra("restart").equals("restart")){
                ProgressDialog.get().show();
                viewModel.updateNews();
            }
        }
        super.onResume();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public Class<NewsFragmentViewModel> getViewModelClass() {
        return NewsFragmentViewModel.class;
    }

    @Override
    public void bindData(NewsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(badgeListener);
    }

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public String getToolbarName() {
        return null;
    }

    @Override
    public int getHomeIcon() {
        return 0;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public Navigator getNavigator() {
        return ((MainActivity) getActivity()).navigator;
    }
}

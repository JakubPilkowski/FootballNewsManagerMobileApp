package com.example.footballnewsmanager.fragments.main.news_info;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.NewsInfoFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.models.News;
import com.example.footballnewsmanager.models.UserNews;

public class NewsInfoFragment extends BaseFragment<NewsInfoFragmentBinding, NewsInfoFragmentViewModel> implements Providers {


    public static final String TAG = "NewsInfo";

    private UserNews news;

    public static NewsInfoFragment newInstance(UserNews news) {
        NewsInfoFragment newsInfoFragment = new NewsInfoFragment();
        newsInfoFragment.setNews(news);
        return newsInfoFragment;
    }

    public void setNews(UserNews news) {
        this.news = news;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.news_info_fragment;
    }

    @Override
    public Class<NewsInfoFragmentViewModel> getViewModelClass() {
        return NewsInfoFragmentViewModel.class;
    }

    @Override
    public void bindData(NewsInfoFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(news);
    }

    @Override
    public int getBackPressType() {
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
        return ((MainActivity)getActivity()).getNavigator();
    }
}

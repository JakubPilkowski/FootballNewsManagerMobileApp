package com.example.footballnewsmanager.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.footballnewsmanager.interfaces.Providers;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment implements Providers {

    public B binding;
    public VM viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        viewModel = ViewModelProviders.of(this).get(getViewModelClass());
        bindData(binding);
        return binding.getRoot();
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract Class<VM> getViewModelClass();

    public abstract void bindData(B binding);

    public abstract int getBackPressType();
}
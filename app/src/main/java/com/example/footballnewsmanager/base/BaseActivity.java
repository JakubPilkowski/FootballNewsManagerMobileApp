package com.example.footballnewsmanager.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;


public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {



    public VM viewModel;
    public B binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this).get(getViewModel());
        initActivity(binding);
    }

    protected abstract void initActivity(B binding);

    protected abstract Class<VM> getViewModel();

    public abstract int getIdFragmentContainer();

    @LayoutRes
    public abstract int getLayoutRes();

}

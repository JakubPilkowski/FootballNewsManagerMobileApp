package com.example.footballnewsmanager.activites.main;

import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityMainBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ^ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//        }
    }

    @Override
    protected Class<MainActivityViewModel> getViewModel() {
        return MainActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return null;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}

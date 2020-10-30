package com.example.footballnewsmanager.activites.register;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityProposedSettingsBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ProposedSettingsActivity extends BaseActivity<ActivityProposedSettingsBinding, ProposedSettingsActivityViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityProposedSettingsBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.TRANSPARENT);
                        window.setNavigationBarColor(getResources().getColor(R.color.colorbackgroundPrimary));
        }


        viewModel.init();
    }

    @Override
    protected Class<ProposedSettingsActivityViewModel> getViewModel() {
        return ProposedSettingsActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_proposed_settings;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return getCurrentFragment();
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}

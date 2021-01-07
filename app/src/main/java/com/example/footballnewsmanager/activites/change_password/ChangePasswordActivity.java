package com.example.footballnewsmanager.activites.change_password;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityChangePasswordBinding;
import com.example.footballnewsmanager.fragments.auth.changePassword.ChangePasswordFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding, ChangePasswordActivityViewModel> implements Providers {

    @Override
    protected void initActivity(ActivityChangePasswordBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        navigator.attach(ChangePasswordFragment.newInstance(), ChangePasswordFragment.TAG);
    }

    @Override
    protected Class<ChangePasswordActivityViewModel> getViewModel() {
        return ChangePasswordActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.change_password_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_change_password;
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
        return null;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}

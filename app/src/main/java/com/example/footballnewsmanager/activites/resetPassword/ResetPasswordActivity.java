package com.example.footballnewsmanager.activites.resetPassword;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityResetPasswordBinding;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ResetPasswordActivity extends BaseActivity<ActivityResetPasswordBinding,ResetPasswordViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityResetPasswordBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        Uri data = this.getIntent().getData();
        viewModel.init(data);
    }

    @Override
    protected Class<ResetPasswordViewModel> getViewModel() {
        return ResetPasswordViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.reset_password_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_reset_password;
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

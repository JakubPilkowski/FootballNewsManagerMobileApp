package com.example.footballnewsmanager.fragments.auth.success_fragment;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.resetPassword.ResetPasswordActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.SuccessFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class SuccessFragment extends BaseFragment<SuccessFragmentBinding,SuccessViewModel> implements Providers {


    public static final String TAG = "SuccessFragment";

    private String type;

    public static SuccessFragment newInstance(String type) {
        SuccessFragment successFragment =  new SuccessFragment();
        successFragment.setType(type);
        return successFragment;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.success_fragment;
    }

    @Override
    public Class<SuccessViewModel> getViewModelClass() {
        return SuccessViewModel.class;
    }

    @Override
    public void bindData(SuccessFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(type);
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
        return ((ResetPasswordActivity)getActivity()).getNavigator();
    }
}

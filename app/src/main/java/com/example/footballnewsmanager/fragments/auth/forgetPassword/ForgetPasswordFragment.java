package com.example.footballnewsmanager.fragments.auth.forgetPassword;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class ForgetPasswordFragment extends BaseFragment<ForgetPasswordFragmentBinding, ForgetPasswordViewModel> implements Providers {

    public static final String TAG = "ForgetPasswordTag";

    public static ForgetPasswordFragment newInstance() {
        return new ForgetPasswordFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.forget_password_fragment;
    }

    @Override
    public Class<ForgetPasswordViewModel> getViewModelClass() {
        return ForgetPasswordViewModel.class;
    }

    @Override
    public void bindData(ForgetPasswordFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init();
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
        return ((AuthActivity)getActivity()).getNavigator();
    }
}

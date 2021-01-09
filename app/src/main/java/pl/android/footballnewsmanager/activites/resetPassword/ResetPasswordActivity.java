package pl.android.footballnewsmanager.activites.resetPassword;

import android.app.Activity;
import android.net.Uri;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.base.BaseActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityResetPasswordBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

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

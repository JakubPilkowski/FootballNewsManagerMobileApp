package pl.android.footballnewsmanager.activites.change_password;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.base.BaseActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityChangePasswordBinding;
import pl.android.footballnewsmanager.fragments.auth.changePassword.ChangePasswordFragment;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

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

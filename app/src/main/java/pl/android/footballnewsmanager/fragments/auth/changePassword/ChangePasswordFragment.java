package pl.android.footballnewsmanager.fragments.auth.changePassword;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.change_password.ChangePasswordActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ChangePasswordFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class ChangePasswordFragment extends BaseFragment<ChangePasswordFragmentBinding,ChangePasswordFragmentViewModel> implements Providers {


    public static final String TAG = "ChangePassword";

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.change_password_fragment;
    }

    @Override
    public Class<ChangePasswordFragmentViewModel> getViewModelClass() {
        return ChangePasswordFragmentViewModel.class;
    }

    @Override
    public void bindData(ChangePasswordFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init("application");
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
        return ((ChangePasswordActivity)getActivity()).navigator;
    }
}

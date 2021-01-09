package pl.android.footballnewsmanager.fragments.auth.resetPassword;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.resetPassword.ResetPasswordActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ResetPasswordFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class ResetPasswordFragment extends BaseFragment<ResetPasswordFragmentBinding, ResetPasswordFragmentViewModel> implements Providers {


    public static final String TAG = "ResetPasswordFragment";
    private String token;
    private String type;

    public void setToken(String token) {
        this.token = token;
    }

    public static ResetPasswordFragment newInstance(String token, String type) {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        resetPasswordFragment.setToken(token);
        resetPasswordFragment.setType(type);
        return resetPasswordFragment;
    }

    private void setType(String type) {
        this.type = type;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.reset_password_fragment;
    }

    @Override
    public Class<ResetPasswordFragmentViewModel> getViewModelClass() {
        return ResetPasswordFragmentViewModel.class;
    }

    @Override
    public void bindData(ResetPasswordFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(token, type);
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

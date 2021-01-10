package pl.android.footballnewsmanager.fragments.auth.forgetPassword;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.auth.AuthActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

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
        return ((AuthActivity)getActivity()).getNavigator();
    }
}

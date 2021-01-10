package pl.android.footballnewsmanager.fragments.auth.login;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.auth.AuthActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class LoginFragment extends BaseFragment<LoginFragmentBinding, LoginViewModel> implements Providers {

    public static final String TAG = "LoginFragmentTag";
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.login_fragment;
    }

    @Override
    public Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    public void bindData(LoginFragmentBinding binding) {
            binding.setViewModel(viewModel);
            viewModel.setProviders(this);
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

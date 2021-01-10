package pl.android.footballnewsmanager.fragments.auth.welcome;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.auth.AuthActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.WelcomeFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class WelcomeFragment extends BaseFragment<WelcomeFragmentBinding, WelcomeViewModel>  implements Providers {


    public static final String TAG = "WelcomeFragmentTag";

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.welcome_fragment;
    }

    @Override
    public Class<WelcomeViewModel> getViewModelClass() {
        return WelcomeViewModel.class;
    }

    @Override
    public void bindData(WelcomeFragmentBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
    }

    @Override
    public int getBackPressType() {
        return 1;
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
        return ((AuthActivity) getActivity()).getNavigator();
    }
}

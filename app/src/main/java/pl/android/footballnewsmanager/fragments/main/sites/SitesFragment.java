package pl.android.footballnewsmanager.fragments.main.sites;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.SitesFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class SitesFragment extends BaseFragment<SitesFragmentBinding, SitesFragmentViewModel> implements Providers {


    public static SitesFragment newInstance() {
        return new SitesFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.sites_fragment;
    }

    @Override
    public Class<SitesFragmentViewModel> getViewModelClass() {
        return SitesFragmentViewModel.class;
    }

    @Override
    public void bindData(SitesFragmentBinding binding) {
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
        return ((MainActivity)getActivity()).navigator;
    }
}

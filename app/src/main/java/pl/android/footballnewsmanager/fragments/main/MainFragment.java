package pl.android.footballnewsmanager.fragments.main;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.MainFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class MainFragment extends BaseFragment<MainFragmentBinding, MainFragmentViewModel> implements Providers {


    public static final String TAG = "MainFragment";

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.main_fragment;
    }

    @Override
    public Class<MainFragmentViewModel> getViewModelClass() {
        return MainFragmentViewModel.class;
    }

    @Override
    public void bindData(MainFragmentBinding binding) {
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
        return "";
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
        return ((MainActivity)getActivity()).getNavigator();
    }
}

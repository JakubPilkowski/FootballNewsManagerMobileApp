package pl.android.footballnewsmanager.fragments.main.all_news;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.BadgeListener;
import pl.android.footballnewsmanager.interfaces.Providers;

public class AllNewsFragment extends BaseFragment<AllNewsFragmentBinding, AllNewsFragmentViewModel> implements Providers {


    public static final String TAG = "AllNews";
    private BadgeListener badgeListener;

    public static AllNewsFragment newInstance(BadgeListener badgeListener) {
        AllNewsFragment fragment = new AllNewsFragment();
        fragment.setBadgeListener(badgeListener);
        return fragment;
    }

    private void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.all_news_fragment;
    }

    @Override
    public Class<AllNewsFragmentViewModel> getViewModelClass() {
        return AllNewsFragmentViewModel.class;
    }

    @Override
    public void bindData(AllNewsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        viewModel.init(badgeListener);
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

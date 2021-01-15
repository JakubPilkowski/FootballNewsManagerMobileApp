package pl.android.footballnewsmanager.fragments.main.news;

import android.util.Log;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;

import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.BadgeListener;
import pl.android.footballnewsmanager.interfaces.Providers;

public class NewsFragment extends BaseFragment<NewsFragmentBinding, NewsFragmentViewModel> implements Providers {


    private BadgeListener badgeListener;

    public static final String TAG = "NewsFragment";

    public static NewsFragment newInstance(BadgeListener badgeListener) {
        NewsFragment fragment = new NewsFragment();
        fragment.setBadgeListener(badgeListener);
        return fragment;
    }

    private void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: "+UserPreferences.get().getRefresh());
        if(UserPreferences.get().getRefresh()){
            viewModel.load();
            UserPreferences.get().setRefresh(false);
        }
        super.onResume();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public Class<NewsFragmentViewModel> getViewModelClass() {
        return NewsFragmentViewModel.class;
    }

    @Override
    public void bindData(NewsFragmentBinding binding) {
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
        return ((MainActivity) getActivity()).navigator;
    }
}

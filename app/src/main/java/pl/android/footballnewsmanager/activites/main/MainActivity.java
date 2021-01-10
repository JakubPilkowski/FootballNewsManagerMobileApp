package pl.android.footballnewsmanager.activites.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;

import java.util.Locale;

import pl.android.footballnewsmanager.base.BaseActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.fragments.main.all_news.AllNewsFragment;
import pl.android.footballnewsmanager.fragments.main.news.NewsFragment;
import pl.android.footballnewsmanager.fragments.main.news_info.NewsInfoFragment;
import pl.android.footballnewsmanager.fragments.main.profile.ProfileFragment;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.Providers;
import pl.android.footballnewsmanager.models.NewsView;
import pl.android.footballnewsmanager.models.UserNews;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> implements Providers {


    public static final int RESULT_MANAGE_TEAMS = 1001;
    public static final int RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS = 1002;

    @Override
    protected void initActivity(ActivityMainBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
        navigator.attach(MainFragment.newInstance(), MainFragment.TAG);
    }

    @Override
    protected Class<MainActivityViewModel> getViewModel() {
        return MainActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.main_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public BaseActivity getActivity() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_MANAGE_TEAMS && resultCode == RESULT_OK) {
            reloadNews();
            reloadProfile();
            boolean proposed = UserPreferences.get().getProposed();
            if (proposed)
                reloadAllNews();
        }
        if (requestCode == RESULT_MANAGE_TEAMS_FROM_TEAM_NEWS && resultCode == RESULT_OK) {
            reloadNews();
            reloadProfile();
            reloadNewsInfoPage();
            boolean proposed = UserPreferences.get().getProposed();
            if (proposed)
                reloadAllNews();
        }
    }


    @Override
    public void onBackPressed() {
        if (!doesLayoutNeedToScroll())
            super.onBackPressed();
    }


    private boolean doesLayoutNeedToScroll() {
        MainFragment mainFragment = getMainFragment();
        if (getCurrentFragment() == mainFragment) {
            ViewPager2 viewPager2 = mainFragment.binding.mainViewpager;
            switch (viewPager2.getCurrentItem()) {
                case 0:
                    NewsFragment newsFragment = (NewsFragment) getFragmentFromSupportManager(NewsFragment.class);
                    if (newsFragment.viewModel != null) {
                        RecyclerView recyclerView = newsFragment.binding.newsRecyclerView;
                        if (layoutManagerAtFirstPosition(recyclerView)) return false;
                        else {
                            newsFragment.viewModel.backToTop();
                            return true;
                        }
                    } else
                        return false;
                case 1:
                    AllNewsFragment allNewsFragment = (AllNewsFragment) getFragmentFromSupportManager(AllNewsFragment.class);
                    if (allNewsFragment.viewModel != null) {
                        RecyclerView recyclerView = allNewsFragment.binding.allNewsRecyclerView;
                        if (layoutManagerAtFirstPosition(recyclerView)) return false;
                        else {
                            allNewsFragment.viewModel.backToTop();
                            return true;
                        }
                    } else
                        return false;
                case 3:
                case 4:
                default:
                    return false;
            }
        }
        return false;
    }


    private boolean layoutManagerAtFirstPosition(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        return layoutManager.findFirstVisibleItemPosition() <= 0;
    }


    private void reloadNewsInfoPage() {
        NewsInfoFragment newsInfoFragment = (NewsInfoFragment) getFragmentFromSupportManager(NewsInfoFragment.class);
        if (newsInfoFragment != null)
            if (newsInfoFragment.viewModel != null) newsInfoFragment.viewModel.load();

    }

    public MainFragment getMainFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MainFragment) {
                return (MainFragment) fragment;
            }
        }
        return MainFragment.newInstance();
    }


    public void reloadNews() {
        NewsFragment newsFragment = (NewsFragment) getFragmentFromSupportManager(NewsFragment.class);
        if (newsFragment != null)
            if (newsFragment.viewModel != null)
                newsFragment.viewModel.load();
    }

    public void reloadAllNews() {
        AllNewsFragment allNewsFragment = (AllNewsFragment) getFragmentFromSupportManager(AllNewsFragment.class);
        if (allNewsFragment != null)
            if (allNewsFragment.viewModel != null)
                allNewsFragment.viewModel.load();
    }

    public <T> Fragment getFragmentFromSupportManager(Class<T> type) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (type.isInstance(fragment)) {
                return fragment;
            }
        }
        return null;
    }

    public void changeLanguage(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, displayMetrics);
        }
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    public void reloadProfile() {
        ProfileFragment profileFragment = (ProfileFragment) getFragmentFromSupportManager(ProfileFragment.class);
        if (profileFragment != null)
            if (profileFragment.viewModel != null)
                profileFragment.viewModel.load();
    }

    public void updateNews(UserNews oldUserNews, UserNews newUserNews, NewsView newsView) {
        switch (newsView) {
            case SELECTED:
                NewsFragment newsFragment = (NewsFragment) getFragmentFromSupportManager(NewsFragment.class);
                if (newsFragment != null)
                    if (newsFragment.viewModel != null)
                        if (newsFragment.viewModel.newsAdapter != null)
                            newsFragment.viewModel.newsAdapter.changeIfExists(oldUserNews, newUserNews);
                break;
            case ALL:
                AllNewsFragment allNewsFragment = (AllNewsFragment) getFragmentFromSupportManager(AllNewsFragment.class);
                if (allNewsFragment != null)
                    if (allNewsFragment.viewModel != null)
                        if (allNewsFragment.viewModel.allNewsAdapter != null)
                            allNewsFragment.viewModel.allNewsAdapter.changeIfExists(oldUserNews, newUserNews);
                break;
        }
    }
}

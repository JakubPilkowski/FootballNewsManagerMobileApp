package pl.android.footballnewsmanager.fragments.main;

import android.view.MenuItem;

import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.MainFragmentBinding;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.adapters.main.MainViewPager;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.main.all_news.AllNewsFragment;
import pl.android.footballnewsmanager.fragments.main.news.NewsFragment;
import pl.android.footballnewsmanager.fragments.main.profile.ProfileFragment;
import pl.android.footballnewsmanager.fragments.main.sites.SitesFragment;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.BadgeListener;

public class MainFragmentViewModel extends BaseViewModel implements BadgeListener {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    public ObservableField<BottomNavigationView.OnNavigationItemSelectedListener> navigationItemSelectedListenerObservable = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> viewPagerAdapterObservable = new ObservableField<>();
    public ObservableField<ViewPager2.OnPageChangeCallback> onPageChangeCallbackObservable = new ObservableField<>();
    public ObservableBoolean userInputEnabled = new ObservableBoolean(false);


    public List<BaseFragment> fragments = new ArrayList<>();

    public void init() {
        viewPager2 = ((MainFragmentBinding) getBinding()).mainViewpager;
        bottomNavigationView = ((MainFragmentBinding) getBinding()).mainBottomNavView;

        fragments.add(NewsFragment.newInstance(this));
        fragments.add(AllNewsFragment.newInstance(this));
        fragments.add(SitesFragment.newInstance());
        fragments.add(ProfileFragment.newInstance());

        MainViewPager viewPagerAdapter = new MainViewPager((FragmentActivity) getActivity(), fragments);
        viewPagerAdapterObservable.set(viewPagerAdapter);
        onPageChangeCallbackObservable.set(onPageChangeCallback);
        navigationItemSelectedListenerObservable.set(navigationItemSelectedListener);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getNotifications(notificationResponseCallback, token);
    }


    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            switch (position) {
                case 0:
                    bottomNavigationView.getMenu().findItem(R.id.nav_news).setChecked(true);
                    break;
                case 1:
                    bottomNavigationView.getMenu().findItem(R.id.nav_all).setChecked(true);
                    break;
                case 2:
                    bottomNavigationView.getMenu().findItem(R.id.nav_sites).setChecked(true);
                    break;
                case 3:
                    bottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                    break;
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_news:
                    viewPager2.setCurrentItem(0);
                    break;
                case R.id.nav_all:
                    viewPager2.setCurrentItem(1);
                    break;
                case R.id.nav_sites:
                    viewPager2.setCurrentItem(2);
                    break;
                case R.id.nav_profile:
                    viewPager2.setCurrentItem(3);
                    break;
            }
            return true;
        }
    };

    @Override
    public void onBadgeChange() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getNotifications(notificationResponseCallback, token);
    }

    private Callback<Long> notificationResponseCallback = new Callback<Long>() {
        @Override
        public void onSuccess(Long amount) {
            UserPreferences.get().changeNotification(amount);
            getActivity().runOnUiThread(()->{
                if (amount > 0) {
                    BadgeDrawable badgeDrawable = ((MainFragmentBinding) getBinding()).mainBottomNavView.getOrCreateBadge(R.id.nav_news);
                    badgeDrawable.setMaxCharacterCount(99);
                    badgeDrawable.setNumber(amount.intValue());
                } else {
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                    notificationManagerCompat.cancel(1001);
                    ((MainFragmentBinding) getBinding()).mainBottomNavView.removeBadge(R.id.nav_news);
                }
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {
            SnackbarHelper.showDefaultSnackBarFromStatus(bottomNavigationView, error.getStatus());
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super Long> observer) {

        }
    };
}

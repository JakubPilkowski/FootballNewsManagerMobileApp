package com.example.footballnewsmanager.fragments.main;

import android.util.Log;
import android.view.MenuItem;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.adapters.main.MainViewPager;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.news.BadgesResponse;
import com.example.footballnewsmanager.api.responses.news.NotificationResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.databinding.MainFragmentBinding;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.models.NotificationData;
import com.example.footballnewsmanager.models.Team;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class MainFragmentViewModel extends BaseViewModel implements BadgeListener {
    // TODO: Implement the ViewModel
    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;

    public ObservableField<BottomNavigationView.OnNavigationItemSelectedListener> navigationItemSelectedListenerObservable = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> viewPagerAdapterObservable = new ObservableField<>();
    public ObservableField<ViewPager2.OnPageChangeCallback> onPageChangeCallbackObservable = new ObservableField<>();


    public void init() {
        viewPager2 = ((MainFragmentBinding) getBinding()).mainViewpager;
        bottomNavigationView = ((MainFragmentBinding) getBinding()).mainBottomNavView;
        MainViewPager viewPagerAdapter = new MainViewPager((FragmentActivity) getActivity(), this);
        viewPagerAdapterObservable.set(viewPagerAdapter);
        onPageChangeCallbackObservable.set(onPageChangeCallback);
        navigationItemSelectedListenerObservable.set(navigationItemSelectedListener);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getNotVisitedNewsAmount(notificationResponseCallback, token);
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
        Log.d(MainFragment.TAG, "onBadgeChange");
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getNotVisitedNewsAmount(notificationResponseCallback, token);
    }

    private Callback<BadgesResponse> notificationResponseCallback = new Callback<BadgesResponse>() {
        @Override
        public void onSuccess(BadgesResponse badgesResponse) {
            Log.d(MainFragment.TAG, "onSuccess: ");
            Long amount = badgesResponse.getAmount();
            if(amount > 0){
                BadgeDrawable badgeDrawable = ((MainFragmentBinding)getBinding()).mainBottomNavView.getOrCreateBadge(R.id.nav_news);
                badgeDrawable.setMaxCharacterCount(99);
                badgeDrawable.setNumber(amount.intValue());
            }
            else{
                ((MainFragmentBinding)getBinding()).mainBottomNavView.removeBadge(R.id.nav_news);
            }
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d(MainFragment.TAG, "onSmthWrong: ");
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BadgesResponse> observer) {

        }
    };
}
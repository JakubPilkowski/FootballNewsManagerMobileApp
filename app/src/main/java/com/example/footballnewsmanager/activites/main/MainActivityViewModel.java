package com.example.footballnewsmanager.activites.main;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.main.MainViewPager;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class MainActivityViewModel extends BaseViewModel {


    ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;

    public ObservableField<BottomNavigationView.OnNavigationItemSelectedListener> navigationItemSelectedListenerObservable = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> viewPagerAdapterObservable = new ObservableField<>();
    public ObservableField<ViewPager2.OnPageChangeCallback> onPageChangeCallbackObservable = new ObservableField<>();


    public void init() {
        viewPager2 = ((ActivityMainBinding) getBinding()).mainViewpager;
        bottomNavigationView = ((ActivityMainBinding) getBinding()).mainBottomNavView;
        MainViewPager viewPagerAdapter = new MainViewPager((FragmentActivity) getActivity());
        viewPagerAdapterObservable.set(viewPagerAdapter);
        onPageChangeCallbackObservable.set(onPageChangeCallback);
        navigationItemSelectedListenerObservable.set(navigationItemSelectedListener);
        
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

//    public void onClick() {
//
//        ProgressDialog.get().show();
//        String token = UserPreferences.get().getAuthToken();
//        Connection.get().logout(logoutCallback, token);
//    }

}

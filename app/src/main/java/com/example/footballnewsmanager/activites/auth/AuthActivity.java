package com.example.footballnewsmanager.activites.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.ViewDataBinding;

import android.content.Intent;
import android.os.Build;
import android.view.Window;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityAuthBinding;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.example.footballnewsmanager.fragments.auth.welcome.WelcomeFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class AuthActivity extends BaseActivity<ActivityAuthBinding,AuthActivityViewModel> implements Providers {

    public static final int RESULT_RESET_PASSWORD = 1001;

    @Override
    protected void initActivity(ActivityAuthBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ^ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            //            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        navigator.attach(WelcomeFragment.newInstance(), WelcomeFragment.TAG);
    }

    @Override
    protected Class<AuthActivityViewModel> getViewModel() {
        return AuthActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.auth_fragment_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_auth;
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
        return getCurrentFragment();
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_RESET_PASSWORD && resultCode == RESULT_OK){
            getNavigator().attach(LoginFragment.newInstance(), LoginFragment.TAG);
        }
    }
}

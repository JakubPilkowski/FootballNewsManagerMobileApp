package com.example.footballnewsmanager.activites.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.ViewDataBinding;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityAuthBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.auth.forgetPassword.ForgetPasswordFragment;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.example.footballnewsmanager.fragments.auth.registerFragment.RegisterFragment;
import com.example.footballnewsmanager.fragments.auth.welcome.WelcomeFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

import java.util.Locale;

public class AuthActivity extends BaseActivity<ActivityAuthBinding,AuthActivityViewModel> implements Providers {

    public static final int RESULT_RESET_PASSWORD = 1001;


    @Override
    protected void initActivity(ActivityAuthBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
        navigator.attach(WelcomeFragment.newInstance(), WelcomeFragment.TAG);
    }

    public void changeLanguage(Locale locale){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }
        Intent refresh = new Intent(this, AuthActivity.class);
        finish();
        startActivity(refresh);
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

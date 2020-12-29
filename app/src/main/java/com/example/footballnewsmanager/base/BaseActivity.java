package com.example.footballnewsmanager.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.helpers.SoundPoolManager;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;


public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {



    public VM viewModel;
    public B binding;
    public Navigator navigator = new Navigator();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreferences.init(this);
        ProgressDialog.init(this);
        Connection.init();
        SoundPoolManager.init(this);

        navigator.setActivity(this);
        navigator.setFragmentContainer(getIdFragmentContainer());
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this).get(getViewModel());
        initActivity(binding);
    }

    protected abstract void initActivity(B binding);

    protected abstract Class<VM> getViewModel();

    public abstract int getIdFragmentContainer();


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()<=1)
            finish();
        else {
            BaseFragment fragment = getCurrentFragment();
            switch (fragment.getBackPressType()) {
                case 0:
                    super.onBackPressed();
                    break;
                case 1:
                    navigator.clearBackStack();
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    }


    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(getIdFragmentContainer());
    }

    @LayoutRes
    public abstract int getLayoutRes();


    @Override
    protected void onPause() {
        KeyboardHelper.hideKeyboard(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserPreferences.init(this);
        ProgressDialog.init(this);
        Connection.init();
        SoundPoolManager.init(this);
        ProposedLanguageDialogManager.init(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SoundPoolManager.get().dismiss();
        super.onDestroy();
    }
}

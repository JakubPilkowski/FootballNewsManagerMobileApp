package com.example.footballnewsmanager.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.footballnewsmanager.helpers.Navigator;


public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {



    public VM viewModel;
    public B binding;
    public Navigator navigator = new Navigator();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(getSupportFragmentManager().getBackStackEntryCount()<=1){
            finish();
        }
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
        super.onBackPressed();
    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(getIdFragmentContainer());
    }

    @LayoutRes
    public abstract int getLayoutRes();

}

package com.example.footballnewsmanager.base;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.interfaces.Providers;

public abstract class BaseViewModel extends ViewModel {

    private Providers providers;

    public void setProviders(Providers providers) {
        this.providers = providers;
    }

    public Activity getActivity() {
        return providers.getActivity();
    }

    public BaseFragment getFragment(){
        return providers.getFragment();
    }

    public ViewDataBinding getBinding(){
        return providers.getBinding();
    }
}

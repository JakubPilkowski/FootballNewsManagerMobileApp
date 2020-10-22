package com.example.footballnewsmanager.interfaces;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.helpers.Navigator;

public interface Providers {
    Activity getActivity();

    ViewDataBinding getBinding();

    BaseFragment getFragment();

    Navigator getNavigator();
}

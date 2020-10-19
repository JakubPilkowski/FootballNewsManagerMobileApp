package com.example.footballnewsmanager.interfaces;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.base.BaseFragment;

public interface Providers {

    Activity getActivity();

    ViewDataBinding getBinding();

    BaseFragment getFragment();
}

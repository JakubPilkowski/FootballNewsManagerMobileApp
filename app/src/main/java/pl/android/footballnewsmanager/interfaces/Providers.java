package pl.android.footballnewsmanager.interfaces;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.helpers.Navigator;

public interface Providers {
    Activity getActivity();

    ViewDataBinding getBinding();

    BaseFragment getFragment();

    Navigator getNavigator();
}

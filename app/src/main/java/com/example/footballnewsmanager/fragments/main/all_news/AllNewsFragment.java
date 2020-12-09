package com.example.footballnewsmanager.fragments.main.all_news;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.main.AllNewsResponse;
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.AllNewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.BadgeListener;
import com.example.footballnewsmanager.interfaces.Providers;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class AllNewsFragment extends BaseFragment<AllNewsFragmentBinding,AllNewsFragmentViewModel> implements Providers {


    private BadgeListener badgeListener;

    public static AllNewsFragment newInstance(BadgeListener badgeListener) {
        AllNewsFragment fragment = new AllNewsFragment();
        fragment.setBadgeListener(badgeListener);
        return fragment;
    }

    public void setBadgeListener(BadgeListener badgeListener) {
        this.badgeListener = badgeListener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.all_news_fragment;
    }

    @Override
    public Class<AllNewsFragmentViewModel> getViewModelClass() {
        return AllNewsFragmentViewModel.class;
    }

    @Override
    public void bindData(AllNewsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().allNews(callback, token, 0);
    }

    private Callback<AllNewsResponse> callback = new Callback<AllNewsResponse>() {
        @Override
        public void onSuccess(AllNewsResponse newsResponse) {
            ProgressDialog.get().dismiss();
            viewModel.init(newsResponse, badgeListener);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), error.getError(), Toast.LENGTH_LONG).show();
                    }
            );
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super AllNewsResponse> observer) {
            Log.d("AllNews", "Fragment subscribeActual");
        }
    };

    @Override
    public int getBackPressType() {
        return 0;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public Navigator getNavigator() {
        return ((MainActivity)getActivity()).navigator;
    }
}

package com.example.footballnewsmanager.fragments.main.news;

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
import com.example.footballnewsmanager.api.responses.main.NewsResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.Providers;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class NewsFragment extends BaseFragment<NewsFragmentBinding, NewsFragmentViewModel> implements Providers {


    public static NewsFragment newInstance() {
        return new NewsFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.news_fragment;
    }

    @Override
    public Class<NewsFragmentViewModel> getViewModelClass() {
        return NewsFragmentViewModel.class;
    }

    @Override
    public void bindData(NewsFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);

//        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().news(callback, token, 0);
    }

    private Callback<NewsResponse> callback = new Callback<NewsResponse>() {
        @Override
        public void onSuccess(NewsResponse newsResponse) {
//            ProgressDialog.get().dismiss();
            Log.d("News", "Fragment success");
            viewModel.init(newsResponse);
        }

        @Override
        public void onSmthWrong(BaseError error) {
//            ProgressDialog.get().dismiss();
            Log.d("News", "Fragment error");
            getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), error.getError(), Toast.LENGTH_LONG).show();
                    }
            );
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super NewsResponse> observer) {
            Log.d("News", "Fragment subscribeActual");
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
        return ((MainActivity) getActivity()).navigator;
    }
}

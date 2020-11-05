package com.example.footballnewsmanager.fragments.proposed_settings.sites;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.register.ProposedSettingsActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ProposedSitesFragmentBinding;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.Providers;
import com.example.footballnewsmanager.models.Site;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSitesFragment extends BaseFragment<ProposedSitesFragmentBinding, ProposedSitesFragmentViewModel> implements Providers {


    private List<Site> sites;

//    public static ProposedSitesFragment newInstance(List<Site> sites) {
//        ProposedSitesFragment proposedSitesFragment = new ProposedSitesFragment();
//        proposedSitesFragment.setSites(sites);
//        return proposedSitesFragment;
//    }
    public static ProposedSitesFragment newInstance() {
        return new ProposedSitesFragment();
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.proposed_sites_fragment;
    }

    @Override
    public Class<ProposedSitesFragmentViewModel> getViewModelClass() {
        return ProposedSitesFragmentViewModel.class;
    }

    @Override
    public void bindData(ProposedSitesFragmentBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
//        viewModel.init(sites);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedSites(callback,
                token
                ,0);
    }


    private Callback<ProposedSitesResponse> callback = new Callback<ProposedSitesResponse>() {
        @Override
        public void onSuccess(ProposedSitesResponse proposedSitesResponse) {
            viewModel.init(proposedSitesResponse.getSites());
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedSitesResponse> observer) {

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
        return ((ProposedSettingsActivity)getActivity()).getNavigator();
    }
}

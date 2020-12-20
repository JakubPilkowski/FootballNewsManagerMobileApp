package com.example.footballnewsmanager.fragments.proposed_settings.sites;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedSitesResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.NewsFragmentBinding;
import com.example.footballnewsmanager.databinding.ProposedSitesFragmentBinding;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.LayoutManager;
import com.example.footballnewsmanager.models.Site;

import java.util.List;
import java.util.Observable;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSitesFragmentViewModel extends BaseViewModel {
    public ObservableField<ProposedSitesAdapter> recyclerViewAdapter = new ObservableField<>();
    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    private View loadingView;
    private Animation loadingAnimation;

    public void init(){
        initLoadingAnimation();
        loadingView.startAnimation(loadingAnimation);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedSites(callback,
                token,0);

    }

    private void initLoadingAnimation() {
        LinearLayout linearLayout = ((ProposedSitesFragmentBinding) getBinding()).proposedSitesLoading;
        loadingView = linearLayout.getChildAt(0);
        loadingAnimation = AnimationUtils.loadAnimation(getFragment().getContext(), R.anim.rotate_translate);
    }


    private void initItemsView(ProposedSitesResponse proposedSitesResponse){
        ProposedSitesAdapter proposedSitesAdapter = new ProposedSitesAdapter();
        proposedSitesAdapter.setItems(proposedSitesResponse.getSites());
        recyclerViewAdapter.set(proposedSitesAdapter);
    }


    private Callback<ProposedSitesResponse> callback = new Callback<ProposedSitesResponse>() {
        @Override
        public void onSuccess(ProposedSitesResponse proposedSitesResponse) {
            loadingVisibility.set(false);
            loadingView.clearAnimation();
            itemsVisibility.set(true);
            getActivity().runOnUiThread(() -> {
                initItemsView(proposedSitesResponse);
            });
        }

        @Override
        public void onSmthWrong(BaseError error) {

        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedSitesResponse> observer) {

        }
    };
}

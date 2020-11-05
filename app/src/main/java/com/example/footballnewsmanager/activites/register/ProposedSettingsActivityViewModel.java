package com.example.footballnewsmanager.activites.register;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.adapters.ProposedSettingsViewPagerAdapter;
import com.example.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapter;
import com.example.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapterViewModel;
import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import com.example.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapterViewModel;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.proposed.UserSettingsRequest;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsAndSitesResponse;
import com.example.footballnewsmanager.api.responses.proposed.ProposedUserResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityProposedSettingsBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.proposed_settings.others.ProposedOthersFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.others.ProposedOthersViewModel;
import com.example.footballnewsmanager.fragments.proposed_settings.sites.ProposedSitesFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.teams.ProposedTeamsFragment;
import com.example.footballnewsmanager.helpers.LanguageHelper;
import com.example.footballnewsmanager.helpers.ScreenHelper;
import com.example.footballnewsmanager.helpers.SoundPoolManager;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.models.Language;
import com.example.footballnewsmanager.models.Site;
import com.example.footballnewsmanager.models.Team;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSettingsActivityViewModel extends BaseViewModel {


    public ObservableInt navigationMargin = new ObservableInt();
    public ObservableBoolean enabledBackButton = new ObservableBoolean(false);
    public ObservableInt ballLeftMargin = new ObservableInt();
    public ObservableBoolean enabledNextButton = new ObservableBoolean(true);


    public ViewPager2 viewPager2;
    public View ball;
    public int item;
    public int screenWidth;
    public View stars;
    private String token;

    private ProposedSettingsViewPagerAdapter proposedSettingsViewPagerAdapter;

    private Callback<ProposedTeamsAndSitesResponse> callback = new Callback<ProposedTeamsAndSitesResponse>() {
        @Override
        public void onSuccess(ProposedTeamsAndSitesResponse proposedTeamsAndSitesResponse) {
            Log.d("ProposedSettActivity", "success"+(ProgressDialog.get().isShowing()));
            ProgressDialog.get().dismiss();
//            initView(proposedTeamsAndSitesResponse);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d("ProposedSettActivity", "error" + ProgressDialog.get().isShowing());
            Log.d("ProposedSettActivity", "error" + error.getError());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedTeamsAndSitesResponse> observer) {

        }
    };

    public void init() {
        item = 0;
        ball = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsBall;
        stars = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsStars;
        checkBackButtonEnabled();
        screenWidth = ScreenHelper.getScreenWidth(getActivity());
        ballLeftMargin.set(screenWidth * 4 / 25);
        navigationMargin.set(ScreenHelper.getNavBarHeight(getActivity().getApplicationContext()));
        viewPager2 = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsViewpager;
        token = UserPreferences.get().getAuthToken();
        initView();
//        ProgressDialog.get().show();
//        Connection.get().proposedTeamsAndSites(callback,
//                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjA0MzE1MjU0LCJleHAiOjE2MDQ5MjAwNTR9.GZWZ7LqFjbdJWtRFfzU2w16dEdP7dfP-igdggQMyFShj-oGiClM7ODC9OSbgOi-o4Ap0hiAIjvzFSP3fqmhtFw"
//                token
//                , 5, 0);
    }

    private void initView() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        ProposedTeamsFragment proposedTeamsFragment = ProposedTeamsFragment.newInstance();
        ProposedSitesFragment proposedSitesFragment = ProposedSitesFragment.newInstance();
        ProposedOthersFragment proposedOthersFragment = ProposedOthersFragment.newInstance();
        fragmentList.add(proposedTeamsFragment);
        fragmentList.add(proposedSitesFragment);
        fragmentList.add(proposedOthersFragment);
        proposedSettingsViewPagerAdapter = new ProposedSettingsViewPagerAdapter((FragmentActivity) getActivity(), fragmentList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ProgressDialog.get().dismiss();
                viewPager2.setAdapter(proposedSettingsViewPagerAdapter);
                viewPager2.setUserInputEnabled(false);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        if(item!=position){
                            item = position;
                        }
                        checkBackButtonEnabled();
                        float translateValue = screenWidth * 17 / 75;
                        ball.animate()
                                .rotation(180 * item)
                                .translationX(translateValue * item).setDuration(400).start();
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
            }
        });
    }


    public void checkBackButtonEnabled() {
        enabledBackButton.set(item > 0);
    }

    public void next() {
        SoundPoolManager.get().playSong(R.raw.pass_a_ball, 0.2f);
        if (item == 2) {
            float translateValue = screenWidth * 17 / 75;
            ball.animate()
                    .rotation(180 * 3)
                    .translationX(translateValue * 3)
                    .setDuration(400)
                    .start();
            stars.animate()
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(400)
                    .withEndAction(() -> {
                        SoundPoolManager.get().playSong(R.raw.hura, 0.1f);
                        blockButtons();
                        ProgressDialog.get().show();
                        List<BaseFragment> fragments = proposedSettingsViewPagerAdapter.getFragments();

                        List<Team> chosenTeams = getSelectedTeams(fragments);
                        List<Site> chosenSites = getSelectedSites(fragments);
                        ProposedOthersFragment proposedOthersFragment = (ProposedOthersFragment) fragments.get(2);
                        ProposedOthersViewModel proposedOthersViewModel = proposedOthersFragment.viewModel;
                        boolean notifications = proposedOthersViewModel.notifications.get();
                        boolean darkMode = proposedOthersViewModel.darkMode.get();
                        boolean proposedNews = proposedOthersViewModel.proposedNews.get();
                        String languageSelected = proposedOthersViewModel.currentLanguage.get();
                        Language language = LanguageHelper.getLanguage(languageSelected, getActivity().getResources());
                        UserSettingsRequest userSettingsRequest = new UserSettingsRequest(
                                chosenTeams, chosenSites, darkMode, notifications, proposedNews,
                                language);

                        Connection.get().userSettingsResponse(savedSettingsResponse,
                                token, userSettingsRequest);

                    })
                    .start();
        } else {
            item++;
            viewPager2.setCurrentItem(item);
        }
    }

    private List<Team> getSelectedTeams(List<BaseFragment> fragments){
        List<Team> chosenTeams = new ArrayList<>();
        ProposedTeamsFragment proposedTeamsFragment = (ProposedTeamsFragment) fragments.get(0);
        ProposedTeamsAdapter proposedTeamsAdapter = (ProposedTeamsAdapter) proposedTeamsFragment.viewModel.recyclerViewAdapter.get();
        List<ProposedTeamsAdapterViewModel> proposedTeamsAdapterViewModels = proposedTeamsAdapter.getViewModels();
        for (ProposedTeamsAdapterViewModel viewmodel :
                proposedTeamsAdapterViewModels) {
            if (viewmodel.isChosen()) {
                chosenTeams.add(viewmodel.getTeam());
            }
        }
        return chosenTeams;
    }

    private List<Site> getSelectedSites(List<BaseFragment> fragments){
        List<Site> chosenSites = new ArrayList<>();
        ProposedSitesFragment proposedSitesFragment = (ProposedSitesFragment) fragments.get(1);
        ProposedSitesAdapter proposedSitesAdapter = proposedSitesFragment.viewModel.recyclerViewAdapter.get();
        List<ProposedSitesAdapterViewModel> proposedSitesAdapterViewModels = proposedSitesAdapter.getViewModels();
        for (ProposedSitesAdapterViewModel viewmodel :
                proposedSitesAdapterViewModels) {
            if (viewmodel.isChosen()) {
                chosenSites.add(viewmodel.getSite());
            }
        }
        return chosenSites;
    }


    private Callback<ProposedUserResponse> savedSettingsResponse = new Callback<ProposedUserResponse>() {
        @Override
        public void onSuccess(ProposedUserResponse proposedUserResponse) {
            ProgressDialog.get().dismiss();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if(error instanceof SingleMessageError){
                Log.d("ProposedSettActivity", ((SingleMessageError) error).getMessage());
            }
            enableButtons();
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super ProposedUserResponse> observer) {

        }
    };

    public void blockButtons() {
        enabledBackButton.set(false);
        enabledNextButton.set(false);
    }

    public void enableButtons() {
        enabledBackButton.set(true);
        enabledNextButton.set(true);
    }

    public void back() {
        SoundPoolManager.get().playSong(R.raw.pass_a_ball, 0.2f);
        item--;
        viewPager2.setCurrentItem(item);
    }
}

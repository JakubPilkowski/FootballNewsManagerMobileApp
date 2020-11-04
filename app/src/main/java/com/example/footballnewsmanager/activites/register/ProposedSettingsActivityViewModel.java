package com.example.footballnewsmanager.activites.register;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.ProposedSettingsViewPagerAdapter;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.proposed.ProposedTeamsAndSitesResponse;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityProposedSettingsBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.proposed_settings.others.ProposedOthersFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.sites.ProposedSitesFragment;
import com.example.footballnewsmanager.fragments.proposed_settings.teams.ProposedTeamsFragment;
import com.example.footballnewsmanager.helpers.ScreenHelper;
import com.example.footballnewsmanager.helpers.SoundPoolManager;

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


    private Callback<ProposedTeamsAndSitesResponse> callback = new Callback<ProposedTeamsAndSitesResponse>() {
        @Override
        public void onSuccess(ProposedTeamsAndSitesResponse proposedTeamsAndSitesResponse) {
            ProgressDialog.get().dismiss();
            initView(proposedTeamsAndSitesResponse);
        }

        @Override
        public void onSmthWrong(BaseError error) {
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
//        String token = UserPreferences.get().getAuthToken();
        Connection.get().proposedTeamsAndSites(callback,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjA0MzE1MjU0LCJleHAiOjE2MDQ5MjAwNTR9.GZWZ7LqFjbdJWtRFfzU2w16dEdP7dfP-igdggQMyFShj-oGiClM7ODC9OSbgOi-o4Ap0hiAIjvzFSP3fqmhtFw"
                , 5, 0);

    }

    private void initView(ProposedTeamsAndSitesResponse response) {
        List<BaseFragment> fragmentList = new ArrayList<>();
        ProposedTeamsFragment proposedTeamsFragment = ProposedTeamsFragment.newInstance(response.getTeamsResponse().getTeams());
        ProposedSitesFragment proposedSitesFragment = ProposedSitesFragment.newInstance(response.getSitesResponse().getSites());
        ProposedOthersFragment proposedOthersFragment = ProposedOthersFragment.newInstance();
        fragmentList.add(proposedTeamsFragment);
        fragmentList.add(proposedSitesFragment);
        fragmentList.add(proposedOthersFragment);
        ProposedSettingsViewPagerAdapter proposedSettingsViewPagerAdapter = new ProposedSettingsViewPagerAdapter((FragmentActivity) getActivity(), fragmentList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager2.setAdapter(proposedSettingsViewPagerAdapter);
                viewPager2.setUserInputEnabled(false);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
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

                        //                Intent intent = new Intent(getActivity(), MainActivity.class);
//                getActivity().startActivity(intent);
//                getActivity().finish();
                    })
                    .start();
        } else {
            item++;
            viewPager2.setCurrentItem(item);
        }
    }

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

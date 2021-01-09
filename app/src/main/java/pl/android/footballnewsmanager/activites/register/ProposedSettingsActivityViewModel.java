package pl.android.footballnewsmanager.activites.register;

import android.content.Intent;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.error.ErrorActivity;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.adapters.ProposedSettingsViewPagerAdapter;
import pl.android.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapter;
import pl.android.footballnewsmanager.adapters.proposed_teams.ProposedTeamsAdapterViewModel;
import pl.android.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapter;
import pl.android.footballnewsmanager.adapters.propsed_sites.ProposedSitesAdapterViewModel;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.requests.proposed.UserSettingsRequest;
import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityProposedSettingsBinding;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.fragments.proposed_settings.sites.ProposedSitesFragment;
import pl.android.footballnewsmanager.fragments.proposed_settings.teams.ProposedTeamsFragment;
import pl.android.footballnewsmanager.helpers.ScreenHelper;
import pl.android.footballnewsmanager.helpers.SoundPoolManager;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.models.Site;
import pl.android.footballnewsmanager.models.Team;
import pl.android.footballnewsmanager.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProposedSettingsActivityViewModel extends BaseViewModel {


    public ObservableInt navigationMargin = new ObservableInt();
    public ObservableBoolean enabledBackButton = new ObservableBoolean(false);
    public ObservableInt ballLeftMargin = new ObservableInt();
    public ObservableBoolean enabledNextButton = new ObservableBoolean(true);
    public ObservableField<RecyclerView.Adapter> viewPagerAdapterObservable = new ObservableField<>();
    public ObservableField<ViewPager2.OnPageChangeCallback> onPageChangeCallbackObservable = new ObservableField<>();
    public ObservableBoolean userInputEnabled = new ObservableBoolean(false);

    private ViewPager2 viewPager2;
    private View ball;
    public int item;
    private int screenWidth;
    private View stars;
    private String token;

    private ProposedSettingsViewPagerAdapter proposedSettingsViewPagerAdapter;

    public void init() {
        item = 0;
        ball = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsBall;
        stars = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsStars;
        checkBackButtonEnabled();
        screenWidth = ScreenHelper.getScreenWidth(getActivity());
        ballLeftMargin.set(screenWidth * 2 / 25);
        navigationMargin.set(ScreenHelper.getNavBarHeight(getActivity().getApplicationContext()));
        viewPager2 = ((ActivityProposedSettingsBinding) getBinding()).proposedSettingsViewpager;
        token = UserPreferences.get().getAuthToken();
        initView();
    }

    private void initView() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        ProposedTeamsFragment proposedTeamsFragment = ProposedTeamsFragment.newInstance();
        ProposedSitesFragment proposedSitesFragment = ProposedSitesFragment.newInstance();
        fragmentList.add(proposedTeamsFragment);
        fragmentList.add(proposedSitesFragment);
        proposedSettingsViewPagerAdapter = new ProposedSettingsViewPagerAdapter((FragmentActivity) getActivity(), fragmentList);
        viewPagerAdapterObservable.set(proposedSettingsViewPagerAdapter);
        onPageChangeCallbackObservable.set(onPageChangeCallback);
    }


    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (item != position) {
                item = position;
            }
            checkBackButtonEnabled();
            float translateValue = screenWidth * 19 / 50;
            ball.animate()
                    .rotation(360 * item)
                    .translationX(translateValue * item).setDuration(400).start();
        }
    };

    private void checkBackButtonEnabled() {
        enabledBackButton.set(item > 0);
    }

    public void next() {
        SoundPoolManager.get().playSong(R.raw.pass_a_ball, 0.2f);
        if (item == 1) {
            float translateValue = screenWidth * 19 / 50;
            ball.animate()
                    .rotation(360 * 2)
                    .translationX(translateValue * 2)
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
                        UserSettingsRequest userSettingsRequest = new UserSettingsRequest(chosenTeams, chosenSites);
                        Connection.get().userSettingsResponse(savedSettingsResponse,
                                token, userSettingsRequest);
                    })
                    .start();
        } else {
            item++;
            viewPager2.setCurrentItem(item);
        }
    }

    private List<Team> getSelectedTeams(List<BaseFragment> fragments) {
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

    private List<Site> getSelectedSites(List<BaseFragment> fragments) {
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


    private Callback<User> savedSettingsResponse = new Callback<User>() {
        @Override
        public void onSuccess(User user) {
            ProgressDialog.get().dismiss();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Intent intent = new Intent(getActivity(), ErrorActivity.class);
            intent.putExtra("status", error.getStatus());
            getActivity().startActivity(intent);
            enableButtons();
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super User> observer) {

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

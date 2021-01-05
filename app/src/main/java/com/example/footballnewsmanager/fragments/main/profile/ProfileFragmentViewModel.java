package com.example.footballnewsmanager.fragments.main.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.activites.liked_news.LikedNewsActivity;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.profile.UserProfileResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ProfileFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.helpers.ErrorView;
import com.example.footballnewsmanager.helpers.LanguageHelper;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.helpers.SnackbarHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;
import com.example.footballnewsmanager.models.LanguageField;

import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProfileFragmentViewModel extends BaseViewModel implements ProposedLanguageListener {
    // TODO: Implement the ViewModel

    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableInt swipeRefreshColor = new ObservableInt(R.color.colorPrimary);
    public ObservableField<SwipeRefreshLayout.OnRefreshListener> swipeRefreshListenerObservable = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>("0");
    public ObservableField<String> teamsCount = new ObservableField<>("0");
    public ObservableBoolean proposedNews = new ObservableBoolean();
    public ObservableField<Switch.OnCheckedChangeListener> proposedNewsChangeListenerAdapter = new ObservableField<>();
    public ObservableField<String> currentLanguage = new ObservableField<>("Polski");
    public ObservableField<Drawable> languageDrawable = new ObservableField<>();
    public ObservableBoolean errorVisibility = new ObservableBoolean(false);
    public ObservableInt status = new ObservableInt();
    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListener = new ObservableField<>();
    private ErrorView.OnTryAgainListener listener = this::load;

    private Switch.OnCheckedChangeListener proposedNewsChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            proposedNews.set(isChecked);
            UserPreferences.get().setProposed(isChecked);
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private LanguageField languageField;

    public void init() {
        swipeRefreshLayout = ((ProfileFragmentBinding) getBinding()).profileSwipeRefresh;
        tryAgainListener.set(listener);
        load();
        proposedNewsChangeListenerAdapter.set(proposedNewsChangeListener);
        swipeRefreshListenerObservable.set(this::refresh);
    }

    public void load() {
        errorVisibility.set(false);
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        refresh();
    }

    private void refresh() {
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getUserProfile(callback, token);
    }

    public void initItemsView(UserProfileResponse userProfileResponse) {
        name.set(userProfileResponse.getUser().getUsername());
        date.set(getActivity().getResources().getString(R.string.account_from) + userProfileResponse.getUser().getAddedDate().split("T")[0]);
        proposedNews.set(UserPreferences.get().getProposed());
        String locale = UserPreferences.get().getLanguage();
        currentLanguage.set(LanguageHelper.getName(locale, getActivity().getResources()));
        languageDrawable.set(LanguageHelper.getDrawable(locale,
                getActivity().getResources()));
        likes.set(String.valueOf(userProfileResponse.getLikes()));
        teamsCount.set(String.valueOf(userProfileResponse.getFavouritesCount()));
    }

    public void languageClick() {
        ProposedLanguageDialogManager.get().show(this);
    }


    public void onManageTeamsClick() {
        Intent intent = new Intent(getActivity(), ManageTeamsActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
    }

    public void onLikedNewsClick() {
        Intent intent = new Intent(getActivity(), LikedNewsActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLanguageClick(LanguageField field) {
        languageField = field;
        UserPreferences.get().setLanguage(languageField.getLocale());
        Locale locale = new Locale(languageField.getLocale());
        Locale.setDefault(locale);
        ((MainActivity)getActivity()).changeLanguage(locale);
    }


    private Callback<UserProfileResponse> callback = new Callback<UserProfileResponse>() {
        @Override
        public void onSuccess(UserProfileResponse userProfileResponse) {
            loadingVisibility.set(false);
            itemsVisibility.set(true);
            getActivity().runOnUiThread(() -> {
                swipeRefreshLayout.setRefreshing(false);
                initItemsView(userProfileResponse);
            });

        }

        @Override
        public void onSmthWrong(BaseError error) {
            loadingVisibility.set(false);
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                if (swipeRefreshLayout.isRefreshing()) {
                    itemsVisibility.set(true);
                    getActivity().runOnUiThread(() -> {
                        swipeRefreshLayout.setRefreshing(false);
                    });
                    MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
                    SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
                } else {
                    itemsVisibility.set(false);
                    status.set(error.getStatus());
                    errorVisibility.set(true);

                }
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super UserProfileResponse> observer) {

        }
    };

    public void onLogoutClick() {
        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().logout(accountCallback, token);
    }

    public void onDeleteAccountClick() {
        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
//        Connection.get().deleteAccount(accountCallback, token);
        ProgressDialog.get().dismiss();
    }

    private Callback<BaseResponse> accountCallback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            ProgressDialog.get().dismiss();
            UserPreferences.get().clearAuthToken();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                MainFragment mainFragment = ((MainActivity) getActivity()).getMainFragment();
                SnackbarHelper.showDefaultSnackBarFromStatus(mainFragment.binding.mainBottomNavView, error.getStatus());
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

package pl.android.footballnewsmanager.fragments.main.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Switch;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.auth.AuthActivity;
import pl.android.footballnewsmanager.activites.change_password.ChangePasswordActivity;
import pl.android.footballnewsmanager.activites.liked_news.LikedNewsActivity;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.api.responses.profile.UserProfileResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ProfileFragmentBinding;
import pl.android.footballnewsmanager.fragments.main.MainFragment;
import pl.android.footballnewsmanager.helpers.AlertDialogManager;
import pl.android.footballnewsmanager.helpers.ErrorView;
import pl.android.footballnewsmanager.helpers.LanguageHelper;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import pl.android.footballnewsmanager.helpers.SnackbarHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.interfaces.ProposedLanguageListener;
import pl.android.footballnewsmanager.models.LanguageField;

import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

import static pl.android.footballnewsmanager.activites.auth.AuthActivity.RESULT_RESET_PASSWORD;

public class ProfileFragmentViewModel extends BaseViewModel implements ProposedLanguageListener {

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

    private Switch.OnCheckedChangeListener proposedNewsChangeListener = (buttonView, isChecked) -> {
        proposedNews.set(isChecked);
        UserPreferences.get().setProposed(isChecked);
        ((MainActivity)getActivity()).reloadAllNews();
    };
    private SwipeRefreshLayout swipeRefreshLayout;

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


    public void onProposedClick(){
        proposedNews.set(!proposedNews.get());
        UserPreferences.get().setProposed(proposedNews.get());
        ((MainActivity)getActivity()).reloadAllNews();
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
        UserPreferences.get().setLanguage(field.getLocale());
        Locale locale = new Locale(field.getLocale());
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

    public void onChangePassword(){
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        getActivity().startActivityForResult(intent, RESULT_RESET_PASSWORD);
    }

    public void onDeleteAccountClick() {

        AlertDialogManager.get().show((dialog, which) -> {
            ProgressDialog.get().show();
            String token = UserPreferences.get().getAuthToken();
            Connection.get().deleteAccount(accountCallback, token);
        });
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

package com.example.footballnewsmanager.fragments.main.profile;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Switch;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.activites.liked_news.LikedNewsActivity;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.activites.manageTeams.ManageTeamsActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.profile.UserProfileResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.ProposedLanguageDialogManager;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;
import com.example.footballnewsmanager.models.LanguageField;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ProfileFragmentViewModel extends BaseViewModel implements ProposedLanguageListener {
    // TODO: Implement the ViewModel

    public ObservableBoolean loadingVisibility = new ObservableBoolean(false);
    public ObservableBoolean itemsVisibility = new ObservableBoolean(false);
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> date = new ObservableField<>();
    public ObservableField<String> likes = new ObservableField<>("0");
    public ObservableField<String> teamsCount = new ObservableField<>("0");
    public ObservableBoolean proposedNews = new ObservableBoolean();
    public ObservableField<Switch.OnCheckedChangeListener> proposedNewsChangeListenerAdapter = new ObservableField<>();
    public ObservableField<String> currentLanguage = new ObservableField<>("Polski");
    public ObservableField<Drawable> languageDrawable = new ObservableField<>();

    private Switch.OnCheckedChangeListener proposedNewsChangeListener = (buttonView, isChecked) -> proposedNews.set(isChecked);

    public void init() {
        load();
        proposedNewsChangeListenerAdapter.set(proposedNewsChangeListener);
    }

    public void load(){
        itemsVisibility.set(false);
        loadingVisibility.set(true);
        String token = UserPreferences.get().getAuthToken();
        Connection.get().getUserProfile(callback, token);
    }

    public void initItemsView(UserProfileResponse userProfileResponse){
        name.set(userProfileResponse.getUser().getUsername());
        date.set("Konto od : " +userProfileResponse.getUser().getAddedDate().split("T")[0]);
        proposedNews.set(userProfileResponse.getUser().isProposedNews());
        currentLanguage.set(String.valueOf(userProfileResponse.getUser().getLanguage()));
        languageDrawable.set(getActivity().getDrawable(R.drawable.ic_poland_small));
        initValuesAnimation(userProfileResponse.getLikes().intValue(), userProfileResponse.getFavouritesCount().intValue());
    }

    private void initValuesAnimation(int likesCount, int teams) {
        ValueAnimator likesAnimator = ValueAnimator.ofInt(0, likesCount);
        likesAnimator.setDuration(1000);
        likesAnimator.setInterpolator(new FastOutSlowInInterpolator());
        likesAnimator.addUpdateListener(animation -> likes.set(String.valueOf((int)animation.getAnimatedValue())));
        likesAnimator.start();

        ValueAnimator teamsCountAnimator = ValueAnimator.ofInt(0, teams);
        teamsCountAnimator.setDuration(1000);
        teamsCountAnimator.setInterpolator(new FastOutSlowInInterpolator());
        teamsCountAnimator.addUpdateListener(animation -> teamsCount.set(String.valueOf((int)animation.getAnimatedValue())));
        teamsCountAnimator.start();
    }

    public void languageClick() {
        ProposedLanguageDialogManager.get().show(this);
    }


    public void onManageTeamsClick(){
        //add remove teams view
        Intent intent = new Intent(getActivity(), ManageTeamsActivity.class);
        getActivity().startActivityForResult(intent, MainActivity.RESULT_MANAGE_TEAMS);
    }
    public void onLikedNewsClick(){
        Intent intent = new Intent(getActivity(), LikedNewsActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onLanguageClick(LanguageField field) {
        currentLanguage.set(field.getName());
        languageDrawable.set(field.getDrawableSmall());
    }


    private Callback<UserProfileResponse> callback = new Callback<UserProfileResponse>() {
        @Override
        public void onSuccess(UserProfileResponse userProfileResponse) {
                loadingVisibility.set(false);
                itemsVisibility.set(true);
                getActivity().runOnUiThread(() -> {
                    initItemsView(userProfileResponse);
                });

        }

        @Override
        public void onSmthWrong(BaseError error) {
//            if(error instanceof SingleMessageError){
//                String message = ((SingleMessageError) error).getMessage();
//                if(message.equals("Nie ma już więcej wyników")){
                    loadingVisibility.set(false);
//                }
//                if(message.equals("Dla podanej frazy nie ma żadnej drużyny"))
//                {
//                    loadingVisibility.set(false);
//                }
//            }
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

    public void onDeleteAccountClick(){
        ProgressDialog.get().show();
        String token = UserPreferences.get().getAuthToken();
        Connection.get().deleteAccount(accountCallback, token);
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
            Log.d("MainActivity", ((SingleMessageError) error).getMessage());
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

package com.example.footballnewsmanager.fragments.auth.success_fragment;

import android.content.Intent;

import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.SuccessFragmentBinding;
import com.example.footballnewsmanager.helpers.SoundPoolManager;

public class SuccessViewModel extends BaseViewModel {

    private String type;

    public void init(String type) {
        this.type = type;
        ((SuccessFragmentBinding) getBinding()).successAnimatedView.setResultListener(new AnimatedSuccessView.ResultListener() {
            @Override
            public void onResultAnimEnd() {
                SoundPoolManager.get().playAcceptSong();
            }

            @Override
            public void onResultAnimStart() {

            }
        });
    }

    public void onNextClick() {
        if(type.equals("browser")){
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else{
            //activityForResult
        }
    }
}

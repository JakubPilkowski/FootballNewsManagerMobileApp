package com.example.footballnewsmanager.fragments.auth.success_fragment;

import android.app.Activity;
import android.content.Intent;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.SuccessFragmentBinding;
import com.example.footballnewsmanager.helpers.SoundPoolManager;

public class SuccessViewModel extends BaseViewModel {

    private String type;

    public void init(String type) {
        this.type = type;
        ((SuccessFragmentBinding) getBinding()).successAnimatedView.setResultListener(() -> SoundPoolManager.get().playSong(R.raw.accept_sound, 0.8f));
    }

    public void onNextClick() {
        if(type.equals("browser")){
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
        else{
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}

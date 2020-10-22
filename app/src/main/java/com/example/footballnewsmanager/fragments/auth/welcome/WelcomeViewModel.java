package com.example.footballnewsmanager.fragments.auth.welcome;

import android.transition.Fade;

import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.WelcomeFragmentBinding;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.example.footballnewsmanager.helpers.DetailsTransition;

public class WelcomeViewModel extends BaseViewModel {
    public void init(){

    }
    public void onRegister(){

    }

    public void onLogin(){
        LoginFragment loginFragment = LoginFragment.newInstance();
        loginFragment.setSharedElementEnterTransition(new DetailsTransition());
        loginFragment.setExitTransition(new DetailsTransition());

        getNavigator().sharedTransitionAttach(loginFragment, LoginFragment.TAG,
                ((WelcomeFragmentBinding)getBinding()).authLoginButton, "login fragment title");
    }

    public void onForgetPassword(){

    }
}

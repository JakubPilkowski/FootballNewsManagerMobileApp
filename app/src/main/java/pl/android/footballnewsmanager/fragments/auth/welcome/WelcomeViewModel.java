package pl.android.footballnewsmanager.fragments.auth.welcome;

import com.example.footballnewsmanager.databinding.WelcomeFragmentBinding;

import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.fragments.auth.forgetPassword.ForgetPasswordFragment;
import pl.android.footballnewsmanager.fragments.auth.login.LoginFragment;
import pl.android.footballnewsmanager.fragments.auth.registerFragment.RegisterFragment;
import pl.android.footballnewsmanager.helpers.AuthNameTransition;

public class WelcomeViewModel extends BaseViewModel {

    public void init() {
    }

    public void onRegister() {
        getNavigator().attach(RegisterFragment.newInstance(), RegisterFragment.TAG);
    }

    public void onLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        loginFragment.setSharedElementEnterTransition(new AuthNameTransition());
        loginFragment.setExitTransition(new AuthNameTransition());
        getNavigator().sharedTransitionAttach(loginFragment, LoginFragment.TAG,
                ((WelcomeFragmentBinding) getBinding()).authLoginButton, "login fragment title");
    }

    public void onForgetPassword() {
        ForgetPasswordFragment forgetPasswordFragment = ForgetPasswordFragment.newInstance();
        forgetPasswordFragment.setSharedElementEnterTransition(new AuthNameTransition());
        forgetPasswordFragment.setSharedElementEnterTransition(new AuthNameTransition());
        getNavigator().sharedTransitionAttach(forgetPasswordFragment, ForgetPasswordFragment.TAG,
                ((WelcomeFragmentBinding) getBinding()).authForgetPasswordButton, "forget password fragment title");
    }
}

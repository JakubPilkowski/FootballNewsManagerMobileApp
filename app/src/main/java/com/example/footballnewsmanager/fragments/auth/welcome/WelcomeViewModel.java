package com.example.footballnewsmanager.fragments.auth.welcome;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.adapters.language.LanguageAdapter;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.WelcomeFragmentBinding;
import com.example.footballnewsmanager.fragments.auth.forgetPassword.ForgetPasswordFragment;
import com.example.footballnewsmanager.fragments.auth.login.LoginFragment;
import com.example.footballnewsmanager.fragments.auth.registerFragment.RegisterFragment;
import com.example.footballnewsmanager.helpers.AuthNameTransition;
import com.example.footballnewsmanager.helpers.LanguageHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;

import java.util.ArrayList;
import java.util.Locale;

public class WelcomeViewModel extends BaseViewModel {

    public ObservableField<ArrayAdapter> spinnerAdapter = new ObservableField<>();
    private ArrayList<Drawable> drawables = new ArrayList<>();
    private ArrayList<String> nameArray = new ArrayList<>();


    public void init() {
        Resources resources = getActivity().getResources();

        nameArray.add(resources.getString(R.string.polish));
        nameArray.add(resources.getString(R.string.english));
        nameArray.add(resources.getString(R.string.italish));
        nameArray.add(resources.getString(R.string.german));
        nameArray.add(resources.getString(R.string.spanish));
        nameArray.add(resources.getString(R.string.french));


        drawables.add(resources.getDrawable(R.drawable.ic_poland_small));
        drawables.add(resources.getDrawable(R.drawable.ic_united_kingdom_small));
        drawables.add(resources.getDrawable(R.drawable.ic_italy_small));
        drawables.add(resources.getDrawable(R.drawable.ic_germany_small));
        drawables.add(resources.getDrawable(R.drawable.ic_spain_small));
        drawables.add(resources.getDrawable(R.drawable.ic_france_small));

        String locale = UserPreferences.get().getLanguage();
        String language = LanguageHelper.getName(locale, resources);
        drawables.set(0, drawables.set(nameArray.indexOf(language), drawables.get(0)));
        nameArray.set(0, nameArray.set(nameArray.indexOf(language), nameArray.get(0)));

        LanguageAdapter languageAdapter = new LanguageAdapter(
                getActivity().getApplicationContext(), R.layout.spinner_item,
                nameArray, drawables);
        spinnerAdapter.set(languageAdapter);

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

    public void onItemSelected(AdapterView<?> parentView, View selectItemView, int position, long id) {
        String localeName = LanguageHelper.nameToLocale(nameArray.get(position), getActivity().getResources());
        if(!UserPreferences.get().getLanguage().equals(localeName)){
            Locale locale = new Locale(localeName);
            Locale.setDefault(locale);
            UserPreferences.get().setLanguage(localeName);
            ((AuthActivity) getActivity()).changeLanguage(locale);
        }
    }
}

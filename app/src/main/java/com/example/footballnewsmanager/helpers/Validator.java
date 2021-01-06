package com.example.footballnewsmanager.helpers;

import android.content.res.Resources;
import android.util.Patterns;

import com.example.footballnewsmanager.R;
import com.google.android.material.textfield.TextInputLayout;

public class Validator {

    public static boolean validateLogin(String login, TextInputLayout loginLayout, Resources resources) {
        if (login.trim().isEmpty()) {
            loginLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        }
        else if(login.length() > 20 || login.length() < 4){
            loginLayout.setError(resources.getString(R.string.login_size_error));
            return false;
        }
        else{
            loginLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean validateEmail(String email, TextInputLayout emailLayout, Resources resources) {
        if (email.trim().isEmpty()) {
            emailLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (email.length() > 40) {
            emailLayout.setError(resources.getString(R.string.email_too_long));
            return false;
        } else {
            boolean isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!isValid) {
                emailLayout.setError(resources.getString(R.string.email_invalid));
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
            }
        }
        return true;
    }

    public static boolean validatePassword(String password, TextInputLayout passLayout, Resources resources) {
        if (password.trim().isEmpty()) {
            passLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (password.length() < 8 || password.length() > 30) {
            passLayout.setError(resources.getString(R.string.password_invalid));
            return false;
        } else {
            passLayout.setErrorEnabled(false);
        }
        return true;
    }


    public static boolean validateRepeatPassword(String repeatPassword, TextInputLayout repeatPassLayout, String password, Resources resources) {
        if(validatePassword(password, repeatPassLayout, resources)){
            if(!repeatPassword.equals(password)){
                repeatPassLayout.setError(resources.getString(R.string.password_must_match));
                return false;
            }
            else{
                repeatPassLayout.setErrorEnabled(false);
                return true;
            }
        }else{
            return false;
        }
    }

    public static boolean validateToken(String token, TextInputLayout tokenInputLayout, Resources resources) {
        if(token.trim().isEmpty()){
            tokenInputLayout.setError(resources.getString(R.string.token_not_empty));
            return false;
        }
        else {
            tokenInputLayout.setErrorEnabled(false);
            return true;
        }
    }
}

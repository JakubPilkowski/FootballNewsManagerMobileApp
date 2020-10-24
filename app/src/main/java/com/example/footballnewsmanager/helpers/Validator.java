package com.example.footballnewsmanager.helpers;

import android.content.res.Resources;
import android.util.Patterns;

import com.example.footballnewsmanager.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Validator {



    public static boolean validateLogin(TextInputEditText loginInput, TextInputLayout loginLayout) {
        return true;
    }

    public static boolean validateEmail(TextInputEditText emailInput, TextInputLayout emailLayout, Resources resources) {
        String emailValue = emailInput.getText().toString();
        if (emailValue.trim().isEmpty()) {
            emailLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (emailValue.length() > 40) {
            emailLayout.setError(resources.getString(R.string.email_too_long));
            return false;
        } else {
            boolean isValid = Patterns.EMAIL_ADDRESS.matcher(emailValue).matches();
            if (!isValid) {
                emailLayout.setError(resources.getString(R.string.email_invalid));
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
            }
        }
        return true;
    }

    public static boolean validatePassword(TextInputEditText passInput, TextInputLayout passLayout, Resources resources) {
        String passwordValue = passInput.getText().toString();
        if (passwordValue.trim().isEmpty()) {
            passLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (passwordValue.length() < 8 || passwordValue.length() > 30) {
            passLayout.setError(resources.getString(R.string.password_invalid));
            return false;
        } else {
            passLayout.setErrorEnabled(false);
        }
        return true;
    }


}

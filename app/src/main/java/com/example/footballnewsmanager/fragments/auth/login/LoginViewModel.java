package com.example.footballnewsmanager.fragments.auth.login;

import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.SplashActivity;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class LoginViewModel extends BaseViewModel {


    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private LoginFragmentBinding binding;
    private Resources resources;


    public void init() {
        binding = ((LoginFragmentBinding) getBinding());
        resources = getActivity().getResources();
        email = binding.loginEmailInput;
        password = binding.loginPasswordInput;
        emailLayout = binding.loginEmailLayout;
        passwordLayout = binding.loginPasswordLayout;

        email.addTextChangedListener(new ValidationWatcher(email));
        password.addTextChangedListener(new ValidationWatcher(password));

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    ((BaseActivity)getActivity()).hideKeyboard();
                    password.clearFocus();
                    validate();
                    return true;
                }
                return false;
            }
        });

    }

    public boolean validateEmail() {
        String emailValue = email.getText().toString();
        if (emailValue.trim().isEmpty()) {
            emailLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (emailValue.length() > 40) {
            emailLayout.setError(resources.getString(R.string.email_too_long));
            return false;
        } else {
            boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailValue).matches();
            if (!isValid) {
                emailLayout.setError(resources.getString(R.string.email_invalid));
//                requestFocus(email);
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
            }
        }
        return true;
    }

    private void requestFocus(TextInputEditText view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean validatePassword() {
        String passwordValue = password.getText().toString();
        if (passwordValue.trim().isEmpty()) {
            passwordLayout.setError(resources.getString(R.string.field_not_blank));
//            requestFocus(password);
            return false;
        } else if (passwordValue.length() < 8 || passwordValue.length() > 30) {
            passwordLayout.setError(resources.getString(R.string.password_invalid));
//            requestFocus(password);
            return false;
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateBoth(){
        boolean emailValidation = validateEmail();
        boolean passwordValidation = validatePassword();
        return emailValidation && passwordValidation;
    }

    public void validate() {
        if (validateBoth())
//            validateEmail();
//            validatePassword();
        //validacja w backendzie
        {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }


    class ValidationWatcher implements TextWatcher {

        private View view;

        public ValidationWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.login_email_input:
                    validateEmail();
                    break;
                case R.id.login_password_input:
                    validatePassword();
                    break;
            }
        }
    }


}

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
import android.widget.Toast;

import com.example.dialogs.ProgressDialog;
import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.activites.SplashActivity;
import com.example.footballnewsmanager.activites.auth.AuthActivity;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import retrofit2.HttpException;


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
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
            }
        }
        return true;
    }

    public boolean validatePassword() {
        String passwordValue = password.getText().toString();
        if (passwordValue.trim().isEmpty()) {
            passwordLayout.setError(resources.getString(R.string.field_not_blank));
            return false;
        } else if (passwordValue.length() < 8 || passwordValue.length() > 30) {
            passwordLayout.setError(resources.getString(R.string.password_invalid));
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
        {
            ProgressDialog.get().show();
            LoginRequest loginRequest = new LoginRequest(email.getText().toString(), password.getText().toString());
            Connection.get().login(callback, loginRequest);
        }
    }


    private Callback<LoginResponse> callback = new Callback<LoginResponse>() {
        @Override
        protected void subscribeActual(@NonNull Observer<? super LoginResponse> observer) {

        }

        @Override
        public void onSuccess(LoginResponse loginResponse) {
            Log.d(LoginFragment.TAG, "loginToken" + loginResponse.getAccessToken());

                       ProgressDialog.get().dismiss();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            getActivity().startActivity(intent);
//            getActivity().finish();
        }


        @Override
        public void onSmthWrong(Throwable message) {
            ProgressDialog.get().dismiss();
            Log.d(LoginFragment.TAG, "login failed1" +message.getMessage());
            try {
                Log.d(LoginFragment.TAG, "login failed"+((HttpException) message).response().errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getActivity().getApplicationContext(), , Toast.LENGTH_SHORT).show();
        }
    };




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

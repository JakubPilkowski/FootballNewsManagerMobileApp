package com.example.footballnewsmanager.fragments.auth.login;

import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableField;

import com.example.dialogs.ProgressDialog;
import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;


public class LoginViewModel extends BaseViewModel {


    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private LoginFragmentBinding binding;
    private Resources resources;
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<ValidatorTextWatcher> emailValidationTextWatcher = new ObservableField<>();
    public ObservableField<ValidatorTextWatcher> passwordValidationTextWatcher = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> passwordEditorActionListener = new ObservableField<>();

    private TextView.OnEditorActionListener passwordListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ((BaseActivity) getActivity()).hideKeyboard();
                password.clearFocus();
                validate();
                return true;
            }
            return false;
        }
    };

    public void init() {
        binding = ((LoginFragmentBinding) getBinding());
        resources = getActivity().getResources();
        email = binding.loginEmailInput;
        password = binding.loginPasswordInput;
        emailLayout = binding.loginEmailLayout;
        passwordLayout = binding.loginPasswordLayout;
        emailValidationTextWatcher.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        passwordValidationTextWatcher.set(new ValidatorTextWatcher(password, passwordLayout, FieldType.PASSWORD));
        passwordEditorActionListener.set(passwordListener);
    }


    public boolean validateBoth() {
        boolean emailValidation = Validator.validateEmail(email, emailLayout, resources);
        boolean passwordValidation = Validator.validatePassword(password, passwordLayout, resources);
        return emailValidation && passwordValidation;
    }

    public void validate() {
        if (validateBoth()) {
            errorText.set("");
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
            ProgressDialog.get().dismiss();
            UserPreferences.get().save(loginResponse);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }


        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            if (error instanceof SingleMessageError) {
                errorText.set(((SingleMessageError) error).getMessage());
            }
        }
    };

}
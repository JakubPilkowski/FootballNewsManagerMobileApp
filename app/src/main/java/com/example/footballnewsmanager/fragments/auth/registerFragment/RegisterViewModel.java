package com.example.footballnewsmanager.fragments.auth.registerFragment;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.register.ProposedSettingsActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.MultipleMessageError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.requests.auth.RegisterRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.api.responses.auth.LoginResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.RegisterFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class RegisterViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel


    public ObservableField<String> username = new ObservableField<>("qweqweqweqwew");
    public ObservableField<String> email = new ObservableField<>("weqww@wqewq.com");
    public ObservableField<String> password = new ObservableField<>("asdasdasds");
    public ObservableBoolean clearFocus = new ObservableBoolean(false);
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<TextWatcher> usernameTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> emailTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> passwordTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> passwordEditorListenerAdapter = new ObservableField<>();

    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private RegisterFragmentBinding binding;
    private Resources resources;
    private ScrollView scrollView;

    private TextView.OnEditorActionListener passwordEditorListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            KeyboardHelper.hideKeyboard(getActivity());
            clearFocus.set(true);
            validate();
            return true;
        }
        return false;
    };

    public void init(){
        resources = getActivity().getResources();
        binding = ((RegisterFragmentBinding)getBinding());
        scrollView = binding.registerScrollview;
        usernameLayout = binding.registerLoginLayout;
        emailLayout = binding.registerEmailLayout;
        passwordLayout = binding.registerPasswordLayout;

        usernameTextWatcherAdapter.set(new ValidatorTextWatcher(username, usernameLayout, FieldType.LOGIN));
        emailTextWatcherAdapter.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        passwordTextWatcherAdapter.set(new ValidatorTextWatcher(password, passwordLayout, FieldType.PASSWORD));
        passwordEditorListenerAdapter.set(passwordEditorListener);

    }

    private boolean validateAll(){
        boolean loginValidation = Validator.validateLogin(username.get(), usernameLayout, resources);
        boolean emailValidation = Validator.validateEmail(email.get(), emailLayout, resources);
        boolean passwordValidation = Validator.validatePassword(password.get(), passwordLayout, resources);
        return loginValidation && emailValidation && passwordValidation;
    }

    public void validate(){
        if(validateAll()){
            errorText.set("");
//            Intent intent = new Intent(getActivity(), ProposedSettingsActivity.class);
//            getActivity().startActivity(intent);
//            getActivity().finish();
            ProgressDialog.get().show();
            RegisterRequest registerRequest = new RegisterRequest(username.get(), email.get(), password.get());
            Connection.get().register(callback,registerRequest);
        }
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            LoginRequest loginRequest = new LoginRequest(email.get(), password.get());
            Connection.get().login(loginCallback, loginRequest);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            errorText.set(error.getError());
            if(error instanceof SingleMessageError){
                errorText.set(((SingleMessageError) error).getMessage());
            }
            else if(error instanceof MultipleMessageError)
            {
                StringBuilder stringBuilder = new StringBuilder();
                for (String key:
                     ((MultipleMessageError) error).getMessages().keySet()) {
                    String value = ((MultipleMessageError) error).getMessages().get(key);
                    stringBuilder.append(value);
                    stringBuilder.append("\n");
                }
                errorText.set(stringBuilder.toString());
            }
            ProgressDialog.get().dismiss();
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

    private Callback<LoginResponse> loginCallback = new Callback<LoginResponse>() {
        @Override
        public void onSuccess(LoginResponse loginResponse) {
            ProgressDialog.get().dismiss();
            UserPreferences.get().addToken(loginResponse);
            Intent intent = new Intent(getActivity(), ProposedSettingsActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            errorText.set(((SingleMessageError)error).getMessage());
            ProgressDialog.get().dismiss();
            //alert????
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super LoginResponse> observer) {

        }
    };

}

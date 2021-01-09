package pl.android.footballnewsmanager.fragments.auth.registerFragment;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import pl.android.footballnewsmanager.activites.error.ErrorActivity;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.MultipleMessageError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.requests.auth.LoginRequest;
import pl.android.footballnewsmanager.api.requests.auth.RegisterRequest;
import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.api.responses.auth.LoginResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.RegisterFragmentBinding;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.helpers.KeyboardHelper;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.helpers.Validator;
import pl.android.footballnewsmanager.helpers.ValidatorTextWatcher;
import pl.android.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class RegisterViewModel extends BaseViewModel {
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

    public void init() {
        resources = getActivity().getResources();
        RegisterFragmentBinding binding = ((RegisterFragmentBinding) getBinding());
        scrollView = binding.registerScrollview;
        usernameLayout = binding.registerLoginLayout;
        emailLayout = binding.registerEmailLayout;
        passwordLayout = binding.registerPasswordLayout;

        usernameTextWatcherAdapter.set(new ValidatorTextWatcher(username, usernameLayout, FieldType.LOGIN));
        emailTextWatcherAdapter.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        passwordTextWatcherAdapter.set(new ValidatorTextWatcher(password, passwordLayout, FieldType.PASSWORD));
        passwordEditorListenerAdapter.set(passwordEditorListener);

    }

    private boolean validateAll() {
        boolean loginValidation = Validator.validateLogin(username.get(), usernameLayout, resources);
        boolean emailValidation = Validator.validateEmail(email.get(), emailLayout, resources);
        boolean passwordValidation = Validator.validatePassword(password.get(), passwordLayout, resources);
        return loginValidation && emailValidation && passwordValidation;
    }

    public void validate() {
        if (validateAll()) {
            errorText.set("");
            ProgressDialog.get().show();
            RegisterRequest registerRequest = new RegisterRequest(username.get(), email.get(), password.get());
            Connection.get().register(callback, registerRequest);
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
            ProgressDialog.get().dismiss();
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Intent intent = new Intent(getActivity(), ErrorActivity.class);
                intent.putExtra("status", error.getStatus());
                getActivity().startActivity(intent);
            } else {
                if (error instanceof SingleMessageError) {
                    errorText.set(((SingleMessageError) error).getMessage());
                } else if (error instanceof MultipleMessageError) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String key :
                            ((MultipleMessageError) error).getMessages().keySet()) {
                        String value = ((MultipleMessageError) error).getMessages().get(key);
                        stringBuilder.append(value);
                        stringBuilder.append("\n");
                    }
                    errorText.set(stringBuilder.toString());
                }
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            }
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
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            errorText.set(((SingleMessageError) error).getMessage());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super LoginResponse> observer) {

        }
    };

}

package pl.android.footballnewsmanager.fragments.auth.login;

import android.content.Intent;
import android.content.res.Resources;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.error.ErrorActivity;
import pl.android.footballnewsmanager.activites.main.MainActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.MultipleMessageError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.requests.auth.LoginRequest;
import pl.android.footballnewsmanager.api.responses.auth.LoginResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.KeyboardHelper;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.helpers.UserPreferences;
import pl.android.footballnewsmanager.helpers.Validator;
import pl.android.footballnewsmanager.helpers.ValidatorTextWatcher;
import pl.android.footballnewsmanager.models.FieldType;


public class LoginViewModel extends BaseViewModel {


    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private LoginFragmentBinding binding;
    private Resources resources;
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<ValidatorTextWatcher> emailValidationTextWatcher = new ObservableField<>();
    public ObservableField<ValidatorTextWatcher> passwordValidationTextWatcher = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> passwordEditorActionListener = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableBoolean clearFocus = new ObservableBoolean(false);

    private TextView.OnEditorActionListener passwordListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            KeyboardHelper.hideKeyboard(getActivity());
            clearFocus.set(true);
            validate();
            return true;
        }
        return false;
    };

    public void init() {
        binding = ((LoginFragmentBinding) getBinding());
        resources = getActivity().getResources();
        emailLayout = binding.loginEmailLayout;
        passwordLayout = binding.loginPasswordLayout;
        emailValidationTextWatcher.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        passwordValidationTextWatcher.set(new ValidatorTextWatcher(password, passwordLayout, FieldType.PASSWORD));
        passwordEditorActionListener.set(passwordListener);
    }


    private boolean validateBoth() {
        boolean emailValidation = Validator.validateEmail(email.get(), emailLayout, resources);
        boolean passwordValidation = Validator.validatePassword(password.get(), passwordLayout, resources);
        return emailValidation && passwordValidation;
    }

    public void validate() {
        if (validateBoth()) {
            errorText.set("");
            if(ProgressDialog.get() != null && !ProgressDialog.get().isShowing())
                ProgressDialog.get().show();
            LoginRequest loginRequest = new LoginRequest(email.get(), password.get());
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
            UserPreferences.get().addToken(loginResponse);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }


        @Override
        public void onSmthWrong(BaseError error) {
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
                    }
                    errorText.set(stringBuilder.toString());
                }
            }
        }
    };

}

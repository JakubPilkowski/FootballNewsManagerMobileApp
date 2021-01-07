package com.example.footballnewsmanager.fragments.auth.changePassword;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.activites.error.ErrorActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.ChangePasswordRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ChangePasswordFragmentBinding;
import com.example.footballnewsmanager.fragments.auth.success_fragment.SuccessFragment;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.ProgressDialog;
import com.example.footballnewsmanager.helpers.UserPreferences;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ChangePasswordFragmentViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<String> oldPassword = new ObservableField<>("suEsKACHpVSt6dmO");
    public ObservableField<String> newPassword = new ObservableField<>("");
    public ObservableField<String> repeatPassword = new ObservableField<>("");
    public ObservableField<TextWatcher> oldPassTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> newPassTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> repeatPasswordTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> repeatPasswordActionAdapter = new ObservableField<>();
    public ObservableBoolean clearFocus = new ObservableBoolean(false);

    private TextInputLayout oldPassLayout;
    private TextInputLayout newPassLayout;
    private TextInputLayout repeatPassLayout;
    private Resources resources;
    private String type;

    private TextView.OnEditorActionListener resetPasswordActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            validate();
            return true;
        }
        return false;
    };

    public void init(String type) {
        this.type = type;
        ChangePasswordFragmentBinding binding = (ChangePasswordFragmentBinding) getBinding();
        resources = getActivity().getResources();
        oldPassLayout = binding.changePasswordOldPassLayout;
        newPassLayout = binding.changePasswordNewPassLayout;
        repeatPassLayout = binding.changePasswordRepeatPasswordLayout;

        oldPassTextWatcherAdapter.set(new ValidatorTextWatcher(oldPassword, oldPassLayout, FieldType.PASSWORD));
        newPassTextWatcherAdapter.set(new ValidatorTextWatcher(newPassword, newPassLayout, FieldType.PASSWORD));
        repeatPasswordTextWatcherAdapter.set(new ValidatorTextWatcher(repeatPassword, repeatPassLayout, newPassword, FieldType.REPEAT_PASSWORD));
        repeatPasswordActionAdapter.set(resetPasswordActionListener);
    }

    private boolean validateAll() {
        boolean oldPassValidation = Validator.validatePassword(oldPassword.get(), oldPassLayout, resources);
        boolean newPassValidation = Validator.validatePassword(newPassword.get(), newPassLayout, resources);
        boolean repeatPassValidation = Validator.validateRepeatPassword(repeatPassword.get(), repeatPassLayout, newPassword.get(), resources);
        return oldPassValidation && newPassValidation && repeatPassValidation;
    }

    public void validate() {
        KeyboardHelper.hideKeyboard(getActivity());
        clearFocus.set(true);
        if (validateAll()) {
            errorText.set("");
            ProgressDialog.get().show();
            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword.get(), newPassword.get());
            String token = UserPreferences.get().getAuthToken();
            Connection.get().changePassword(callback,token, changePasswordRequest);
        }
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            ProgressDialog.get().dismiss();
            getNavigator().attach(SuccessFragment.newInstance(type), SuccessFragment.TAG);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            ProgressDialog.get().dismiss();
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Intent intent = new Intent(getActivity(), ErrorActivity.class);
                intent.putExtra("status", error.getStatus());
                getActivity().startActivity(intent);
            } else {
                errorText.set(((SingleMessageError) error).getMessage());
            }
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };
}

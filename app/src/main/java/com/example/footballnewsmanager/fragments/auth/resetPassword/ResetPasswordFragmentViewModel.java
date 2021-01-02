package com.example.footballnewsmanager.fragments.auth.resetPassword;

import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.bumptech.glide.util.ContentLengthInputStream;
import com.example.footballnewsmanager.activites.error.ErrorActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ResetPasswordFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.fragments.auth.success_fragment.SuccessFragment;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ResetPasswordFragmentViewModel extends BaseViewModel {


    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<String> token = new ObservableField<>("");
    public ObservableField<String> newPassword = new ObservableField<>("suEsKACHpVSt6dmO");
    public ObservableField<String> repeatPassword = new ObservableField<>("suEsKACHpVSt6dmO");
    public ObservableField<TextWatcher> tokenTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> newPassTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> repeatPasswordTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> repeatPasswordActionAdapter = new ObservableField<>();
    public ObservableBoolean clearFocus = new ObservableBoolean(false);

    private TextInputLayout tokenInputLayout;
    private TextInputLayout newPassLayout;
    private TextInputLayout repeatPassLayout;
    private ResetPasswordFragmentBinding binding;
    private Resources resources;
    private String type;

    private TextView.OnEditorActionListener resetPasswordActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            KeyboardHelper.hideKeyboard(getActivity());
            clearFocus.set(true);
            validate();
            return true;
        }
        return false;
    };

    public void init(String token, String type) {
        this.token.set(token);
        this.type = type;
        binding = ((ResetPasswordFragmentBinding) getBinding());
        resources = getActivity().getResources();
        tokenInputLayout = binding.resetPasswordTokenLayout;
        newPassLayout = binding.resetPasswordNewPassLayout;
        repeatPassLayout = binding.resetPasswordRepeatPasswordLayout;

        tokenTextWatcherAdapter.set(new ValidatorTextWatcher(this.token, tokenInputLayout, FieldType.TOKEN));
        newPassTextWatcherAdapter.set(new ValidatorTextWatcher(newPassword, newPassLayout, FieldType.PASSWORD));
        repeatPasswordTextWatcherAdapter.set(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validator.validateRepeatPassword(repeatPassword.get(), repeatPassLayout, newPassword.get(), resources);
            }
        });
        repeatPasswordActionAdapter.set(resetPasswordActionListener);
    }

    private boolean validateAll() {
        boolean tokenValidation = Validator.validateToken(token.get(), tokenInputLayout, resources);
        boolean newPassValidation = Validator.validatePassword(newPassword.get(), newPassLayout, resources);
        boolean repeatPassValidation = Validator.validateRepeatPassword(repeatPassword.get(), repeatPassLayout, newPassword.get(), resources);
        return tokenValidation && newPassValidation && repeatPassValidation;
    }

    public void validate() {
        if (validateAll()) {
            errorText.set("");
            ProgressDialog.get().show();
            ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(token.get(), newPassword.get());
            Connection.get().resetPassword(callback, resetPasswordRequest);
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

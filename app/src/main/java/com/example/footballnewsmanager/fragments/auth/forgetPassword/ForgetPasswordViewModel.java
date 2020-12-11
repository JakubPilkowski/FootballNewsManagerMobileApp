package com.example.footballnewsmanager.fragments.auth.forgetPassword;

import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
import com.example.footballnewsmanager.activites.resetPassword.ResetPasswordActivity;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import com.example.footballnewsmanager.helpers.KeyboardHelper;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

import static com.example.footballnewsmanager.activites.auth.AuthActivity.RESULT_RESET_PASSWORD;

public class ForgetPasswordViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    private TextInputLayout emailLayout;
    private ForgetPasswordFragmentBinding binding;
    private Resources resources;
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<ValidatorTextWatcher> emailValidationTextWatcher = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> emailEditorListenerObservable = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>("pilkowskijakub@gmail.com");
    public ObservableBoolean clearFocus = new ObservableBoolean(false);

    private TextView.OnEditorActionListener emailListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            KeyboardHelper.hideKeyboard(getActivity());
            clearFocus.set(true);
            validate();
            return true;
        }
        return false;
    };

    public void init() {
        binding = (ForgetPasswordFragmentBinding) getBinding();
        resources = getActivity().getResources();
        emailLayout = binding.forgetPasswordInputLayout;
        emailValidationTextWatcher.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        emailEditorListenerObservable.set(emailListener);
    }

    public void validate() {
        if (Validator.validateEmail(email.get(), emailLayout, resources)) {
            errorText.set("");
            ProgressDialog.get().show();
            Connection.get().sendResetPasswordMail(sendResetTokenMail, email.get());
        }
    }

    private Callback<BaseResponse> sendResetTokenMail = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            ProgressDialog.get().dismiss();
            Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
            getActivity().startActivityForResult(intent, RESULT_RESET_PASSWORD);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            errorText.set(((SingleMessageError) error).getMessage());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

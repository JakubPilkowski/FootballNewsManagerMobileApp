package com.example.footballnewsmanager.fragments.auth.forgetPassword;

import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.dialogs.ProgressDialog;
import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.LoginRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import com.example.footballnewsmanager.databinding.LoginFragmentBinding;
import com.example.footballnewsmanager.helpers.Validator;
import com.example.footballnewsmanager.helpers.ValidatorTextWatcher;
import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;

public class ForgetPasswordViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    private TextInputEditText email;
    private TextInputLayout emailLayout;
    private ForgetPasswordFragmentBinding binding;
    private Resources resources;
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<ValidatorTextWatcher> emailValidationTextWatcher = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> emailEditorListenerObservable = new ObservableField<>();

    private TextView.OnEditorActionListener emailListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ((BaseActivity) getActivity()).hideKeyboard();
                email.clearFocus();
                validate();
                return true;
            }
            return false;
        }
    };

    public void init() {
        binding = (ForgetPasswordFragmentBinding) getBinding();
        resources = getActivity().getResources();
        email = binding.forgetPasswordInput;
        emailLayout = binding.forgetPasswordInputLayout;
        emailValidationTextWatcher.set(new ValidatorTextWatcher(email, emailLayout, FieldType.EMAIL));
        emailEditorListenerObservable.set(emailListener);
    }

    public void validate() {
        if (Validator.validateEmail(email, emailLayout, resources)) {
            errorText.set("");
            ProgressDialog.get().show();
            Connection.get().sendResetPasswordMail(sendResetTokenMail, email.getText().toString());
        }
    }

    private Callback<BaseResponse> sendResetTokenMail = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            //przejście do nowego activity?
            ProgressDialog.get().dismiss();
        }

        @Override
        public void onSmthWrong(BaseError error) {
            Log.d(ForgetPasswordFragment.TAG, ((SingleMessageError) error).getMessage());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

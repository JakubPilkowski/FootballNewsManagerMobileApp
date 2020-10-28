package com.example.footballnewsmanager.fragments.auth.resetPassword;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.footballnewsmanager.api.Callback;
import com.example.footballnewsmanager.api.Connection;
import com.example.footballnewsmanager.api.errors.BaseError;
import com.example.footballnewsmanager.api.errors.SingleMessageError;
import com.example.footballnewsmanager.api.requests.auth.ResetPasswordRequest;
import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import com.example.footballnewsmanager.databinding.ResetPasswordFragmentBinding;
import com.example.footballnewsmanager.dialogs.ProgressDialog;
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
    public ObservableField<TextWatcher> tokenTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> newPassTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextWatcher> repeatPasswordTextWatcherAdapter = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> repeatPasswordActionAdapter = new ObservableField<>();

    private TextInputEditText tokenInput;
    private TextInputLayout tokenInputLayout;
    private TextInputEditText newPassInput;
    private TextInputLayout newPassLayout;
    private TextInputEditText repeatPassInput;
    private TextInputLayout repeatPassLayout;
    private ResetPasswordFragmentBinding binding;
    private Resources resources;


    private TextView.OnEditorActionListener resetPasswordActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ((BaseActivity) getActivity()).hideKeyboard();
                newPassInput.clearFocus();
                validate();
                return true;
            }
            return false;
        }
    };

    public void init(String token){
        this.token.set(token);
        binding = ((ResetPasswordFragmentBinding) getBinding());
        resources = getActivity().getResources();
        tokenInput = binding.resetPasswordTokenInput;
        tokenInputLayout = binding.resetPasswordTokenLayout;
        newPassInput = binding.resetPasswordNewPassInput;
        newPassLayout = binding.resetPasswordNewPassLayout;
        repeatPassInput = binding.resetPasswordRepeatPasswordInput;
        repeatPassLayout = binding.resetPasswordRepeatPasswordLayout;

        tokenTextWatcherAdapter.set(new ValidatorTextWatcher(tokenInput, tokenInputLayout, FieldType.TOKEN));
        newPassTextWatcherAdapter.set(new ValidatorTextWatcher(newPassInput, newPassLayout, FieldType.PASSWORD));
        repeatPasswordTextWatcherAdapter.set(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Validator.validateRepeatPassword(repeatPassInput, repeatPassLayout, newPassInput.getText().toString(), resources);
            }
        });
        repeatPasswordActionAdapter.set(resetPasswordActionListener);
    }

    private boolean validateAll() {
        boolean tokenValidation = Validator.validateToken(tokenInput, tokenInputLayout, resources);
        boolean newPassValidation = Validator.validatePassword(newPassInput, newPassLayout, resources);
        boolean repeatPassValidation = Validator.validateRepeatPassword(repeatPassInput, repeatPassLayout,newPassInput.getText().toString(), resources);
        return tokenValidation && newPassValidation && repeatPassValidation;
    }

    public void validate(){
        if(validateAll()){
            errorText.set("");
            ProgressDialog.get().show();
            ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(tokenInput.getText().toString(), newPassInput.getText().toString());
            Connection.get().resetPassword(callback, resetPasswordRequest);
        }
    }

    private Callback<BaseResponse> callback = new Callback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse baseResponse) {
            //przejście do nowego fragmentu
            ProgressDialog.get().dismiss();
            Log.d(ResetPasswordFragment.TAG, "sukces");
        }

        @Override
        public void onSmthWrong(BaseError error) {
            errorText.set(((SingleMessageError)error).getMessage());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

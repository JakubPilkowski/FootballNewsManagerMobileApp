package pl.android.footballnewsmanager.fragments.auth.forgetPassword;

import android.content.Intent;
import android.content.res.Resources;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.databinding.ForgetPasswordFragmentBinding;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import pl.android.footballnewsmanager.activites.auth.AuthActivity;
import pl.android.footballnewsmanager.activites.error.ErrorActivity;
import pl.android.footballnewsmanager.activites.resetPassword.ResetPasswordActivity;
import pl.android.footballnewsmanager.api.Callback;
import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.api.errors.BaseError;
import pl.android.footballnewsmanager.api.errors.SingleMessageError;
import pl.android.footballnewsmanager.api.responses.BaseResponse;
import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.KeyboardHelper;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.helpers.Validator;
import pl.android.footballnewsmanager.helpers.ValidatorTextWatcher;
import pl.android.footballnewsmanager.models.FieldType;

public class ForgetPasswordViewModel extends BaseViewModel {
    private TextInputLayout emailLayout;
    private Resources resources;
    public ObservableField<String> errorText = new ObservableField<>("");
    public ObservableField<ValidatorTextWatcher> emailValidationTextWatcher = new ObservableField<>();
    public ObservableField<TextView.OnEditorActionListener> emailEditorListenerObservable = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>("");
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
        ForgetPasswordFragmentBinding binding = (ForgetPasswordFragmentBinding) getBinding();
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
            getActivity().startActivityForResult(intent, AuthActivity.RESULT_RESET_PASSWORD);
        }

        @Override
        public void onSmthWrong(BaseError error) {
            if (error.getStatus() == 598 || error.getStatus() == 408 || error.getStatus() == 500) {
                Intent intent = new Intent(getActivity(), ErrorActivity.class);
                intent.putExtra("status", error.getStatus());
                getActivity().startActivity(intent);
            } else errorText.set(((SingleMessageError) error).getMessage());
            ProgressDialog.get().dismiss();
        }

        @Override
        protected void subscribeActual(@NonNull Observer<? super BaseResponse> observer) {

        }
    };

}

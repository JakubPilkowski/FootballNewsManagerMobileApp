package com.example.footballnewsmanager.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.ObservableField;

import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputLayout;

public class ValidatorTextWatcher implements TextWatcher {
    private ObservableField<String> text;
    private ObservableField<String> repeatPassword;
    private TextInputLayout editTextLayout;
    private FieldType type;

    public ValidatorTextWatcher(ObservableField<String> text, TextInputLayout editTextLayout, FieldType type) {
        this.text = text;
        this.editTextLayout = editTextLayout;
        this.type = type;
    }
    public ValidatorTextWatcher(ObservableField<String> text, TextInputLayout editTextLayout, ObservableField<String> repeatPassword, FieldType type) {
        this.text = text;
        this.editTextLayout = editTextLayout;
        this.type = type;
        this.repeatPassword = repeatPassword;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (type) {
            case LOGIN:
                Validator.validateLogin(text.get(), editTextLayout, editTextLayout.getResources());
                break;
            case EMAIL:
                Validator.validateEmail(text.get(), editTextLayout, editTextLayout.getResources());
                break;
            case PASSWORD:
                Validator.validatePassword(text.get(), editTextLayout, editTextLayout.getResources());
                break;
            case TOKEN:
                Validator.validateToken(text.get(), editTextLayout, editTextLayout.getResources());
                break;
            case REPEAT_PASSWORD:
                Validator.validateRepeatPassword(text.get(), editTextLayout, repeatPassword.get(),editTextLayout.getResources());
                break;
        }
    }
}

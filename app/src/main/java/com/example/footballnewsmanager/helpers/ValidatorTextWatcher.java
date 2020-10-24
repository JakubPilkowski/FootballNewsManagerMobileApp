package com.example.footballnewsmanager.helpers;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.footballnewsmanager.models.FieldType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ValidatorTextWatcher implements TextWatcher {
    private TextInputEditText editText;
    private TextInputLayout editTextLayout;
    private FieldType type;

    public ValidatorTextWatcher(TextInputEditText editText, TextInputLayout editTextLayout, FieldType type) {
        this.editText = editText;
        this.editTextLayout = editTextLayout;
        this.type = type;
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
//                    Validator.validateLogin(editText, editTextLayout);
                break;
            case EMAIL:
                Validator.validateEmail(editText, editTextLayout, editText.getResources());
                break;
            case PASSWORD:
                Validator.validatePassword(editText, editTextLayout, editText.getResources());
                break;
        }
    }
}

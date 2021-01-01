package com.example.footballnewsmanager.helpers;

import android.view.View;

import com.example.footballnewsmanager.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {
    public static Snackbar getSnackBarFromStatus(View view, int status){
        switch (status) {
            case 598:
                return Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_INDEFINITE);
            case 408:
                return Snackbar.make(view, R.string.connection_timeout, Snackbar.LENGTH_INDEFINITE);
            default:
                return Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_INDEFINITE);
        }
    }
}

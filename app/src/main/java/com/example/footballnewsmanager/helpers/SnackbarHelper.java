package com.example.footballnewsmanager.helpers;

import android.view.View;

import com.example.footballnewsmanager.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {
    public static Snackbar getInfinitiveSnackBarFromStatus(View view, int status){
        switch (status) {
            case 598:
                return Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_INDEFINITE);
            case 408:
                return Snackbar.make(view, R.string.connection_timeout, Snackbar.LENGTH_INDEFINITE);
            default:
                return Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_INDEFINITE);
        }
    }
    public static Snackbar getShortSnackBarFromStatus(View view, int status){
        switch (status) {
            case 598:
                return Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_SHORT);
            case 408:
                return Snackbar.make(view, R.string.connection_timeout, Snackbar.LENGTH_SHORT);
            default:
                return Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT);
        }
    }

    public static void showDefaultSnackBarFromStatus(View view, int status){
        Snackbar snackbar = getShortSnackBarFromStatus(view, status);
        snackbar.setAnchorView(view);
        snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
        snackbar.show();
    }
}

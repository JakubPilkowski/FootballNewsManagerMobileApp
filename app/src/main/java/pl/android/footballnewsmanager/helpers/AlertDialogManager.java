package pl.android.footballnewsmanager.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.footballnewsmanager.R;

public class AlertDialogManager {

    private static AlertDialogManager INSTANCE;
    private AlertDialog dialog;
    private Context context;

    private AlertDialogManager(Context context) {
        this.context = context;
    }

    public static AlertDialogManager get() {
        return INSTANCE;
    }

    public static AlertDialogManager init(Context context) {
        return INSTANCE = new AlertDialogManager(context);
    }

    public void show(DialogInterface.OnClickListener clickListener) {
        dismiss();

        if (context == null) return;

        dialog = new AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle(context.getString(R.string.delete_account))
                .setMessage(context.getString(R.string.are_you_sure_you_want_to_delete_account))
                .setPositiveButton(context.getString(R.string.yes), clickListener)
                .setNegativeButton(context.getString(R.string.no), getDefaultClick())
                .setCancelable(false)
                .show();
    }

    private DialogInterface.OnClickListener getDefaultClick() {
        return (dialogInterface, i) -> dismiss();
    }

    public void dismiss() {
        if (dialog != null && this.dialog.isShowing()) dialog.dismiss();

        dialog = null;

    }
}

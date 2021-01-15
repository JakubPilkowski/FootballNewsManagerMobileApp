package pl.android.footballnewsmanager.helpers;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {
    private static ToastManager INSTANCE;
    private Toast toast;
    private Context context;

    private ToastManager(Context context) {
        this.context = context;
    }

    public static ToastManager get() {
        return INSTANCE;
    }

    public static ToastManager init(Context context) {
        return INSTANCE = new ToastManager(context);
    }

    public void show(String text) {
        dismiss();
        if (context == null) return;
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void dismiss() {
        if (toast != null)
            toast.cancel();
        toast = null;
    }
}

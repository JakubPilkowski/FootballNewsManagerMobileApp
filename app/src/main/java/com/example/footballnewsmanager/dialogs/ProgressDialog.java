package com.example.footballnewsmanager.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.footballnewsmanager.R;

public class ProgressDialog {
    private static ProgressDialog INSTANCE;

    private Context context;
    private AlertDialog dialog;

    public ProgressDialog(Context context) {
        this.context = context;
    }

    public static ProgressDialog init(Context context){
        return INSTANCE = new ProgressDialog(context);
    }

    public static ProgressDialog get(){
        return INSTANCE;
    }

    public void show(){
        dismiss();

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.progress_dialog, null, false);
        View view = linearLayout.getChildAt(0);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_translate);
        view.startAnimation(animation);

        dialog = new AlertDialog.Builder(context, R.style.ProgressDialogTheme)
                .setCancelable(false)
                .setView(linearLayout).create();

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_NO){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ^ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            }
        }
        dialog.show();
    }

    public void dismiss(){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }
}

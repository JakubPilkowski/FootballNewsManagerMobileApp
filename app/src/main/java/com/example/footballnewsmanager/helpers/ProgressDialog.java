package com.example.footballnewsmanager.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

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

        dialog.show();
    }

    public boolean isShowing(){
        return dialog!=null && dialog.isShowing();
    }

    public void dismiss(){
        if(dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }
}

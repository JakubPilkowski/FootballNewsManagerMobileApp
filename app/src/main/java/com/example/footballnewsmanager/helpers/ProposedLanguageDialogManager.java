package com.example.footballnewsmanager.helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.LangugageDragViewBinding;
import com.example.footballnewsmanager.fragments.proposed_settings.ProposedLanguageViewModel;
import com.example.footballnewsmanager.interfaces.ProposedLanguageListener;

public class ProposedLanguageDialogManager {

    private static ProposedLanguageDialogManager INSTANCE;
    private Context context;

    private Dialog dialog;

    private ProposedLanguageDialogManager(Context context) {
        this.context = context;
    }

    public static ProposedLanguageDialogManager init(Context context) {
        return INSTANCE = new ProposedLanguageDialogManager(context);
    }


    public static ProposedLanguageDialogManager get() {
        return INSTANCE;
    }

    public void show(ProposedLanguageListener listener) {
        View layout = LayoutInflater.from(context).inflate(R.layout.langugage_drag_view, null, false);
        LangugageDragViewBinding binding = LangugageDragViewBinding.bind(layout);
        ProposedLanguageViewModel viewModel = new ProposedLanguageViewModel();
        binding.setViewModel(viewModel);
        layout.setOnClickListener(v -> dismiss());
        viewModel.init(listener, context);
        dialog = new AlertDialog.Builder(context, R.style.LanguageDialogTheme)
                .setView(layout).create();
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
}

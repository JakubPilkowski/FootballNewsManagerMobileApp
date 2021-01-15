package pl.android.footballnewsmanager.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import pl.android.footballnewsmanager.api.Connection;
import pl.android.footballnewsmanager.helpers.AlertDialogManager;
import pl.android.footballnewsmanager.helpers.KeyboardHelper;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.helpers.ProgressDialog;
import pl.android.footballnewsmanager.helpers.SoundPoolManager;
import pl.android.footballnewsmanager.helpers.ToastManager;
import pl.android.footballnewsmanager.helpers.UserPreferences;


public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {



    public VM viewModel;
    public B binding;
    public Navigator navigator = new Navigator();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialogManager.init(this);
        UserPreferences.init(this);
        ProgressDialog.init(this);
        Connection.init();
        ToastManager.init(this);
        SoundPoolManager.init(this);

        navigator.setActivity(this);
        navigator.setFragmentContainer(getIdFragmentContainer());
        binding = DataBindingUtil.setContentView(this, getLayoutRes());
        viewModel = ViewModelProviders.of(this).get(getViewModel());
        initActivity(binding);
    }

    protected abstract void initActivity(B binding);

    protected abstract Class<VM> getViewModel();

    public abstract int getIdFragmentContainer();


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()<=1)
            finish();
        else {
            BaseFragment fragment = getCurrentFragment();
            switch (fragment.getBackPressType()) {
                case 0:
                    super.onBackPressed();
                    break;
                case 1:
                    navigator.clearBackStack();
                    break;
                case 2:
                    finish();
                    break;
                case 3:
                    break;
            }
        }
    }

    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(getIdFragmentContainer());
    }

    @LayoutRes
    public abstract int getLayoutRes();


    @Override
    protected void onPause() {
        KeyboardHelper.hideKeyboard(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        UserPreferences.init(this);
        AlertDialogManager.init(this);
        ProgressDialog.init(this);
        Connection.init();
        SoundPoolManager.init(this);
        ToastManager.init(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        SoundPoolManager.get().dismiss();
        super.onDestroy();
    }
}

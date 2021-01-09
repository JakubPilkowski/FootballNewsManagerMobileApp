package pl.android.footballnewsmanager.activites.error;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.activites.SplashActivity;
import pl.android.footballnewsmanager.base.BaseActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityErrorBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public class ErrorActivity extends BaseActivity<ActivityErrorBinding, ErrorViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityErrorBinding binding) {
        Intent intent = getIntent();
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        int status = intent.getIntExtra("status", 0);
        viewModel.init(status);
    }

    @Override
    public void onBackPressed() {
        int requestCode = getIntent().getIntExtra("requestCode", 0);
        if (requestCode == SplashActivity.REQUEST_SPLASH_ERROR) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    protected Class<ErrorViewModel> getViewModel() {
        return ErrorViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_error;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public BaseFragment getFragment() {
        return null;
    }

    @Override
    public Navigator getNavigator() {
        return navigator;
    }
}

package com.example.footballnewsmanager.activites.main;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.base.BaseActivity;
import com.example.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityMainBinding;
import com.example.footballnewsmanager.fragments.main.MainFragment;
import com.example.footballnewsmanager.helpers.Navigator;
import com.example.footballnewsmanager.interfaces.Providers;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityViewModel> implements Providers {


    @Override
    protected void initActivity(ActivityMainBinding binding) {
        binding.setViewModel(viewModel);
        viewModel.setProviders(this);
        viewModel.init();
        navigator.attach(MainFragment.newInstance(), MainFragment.TAG);



//        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
//        broadcastIntent.putExtra("toastMessage", "elo elo elo");
//        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon);


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ^ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//        }
    }

    @Override
    protected Class<MainActivityViewModel> getViewModel() {
        return MainActivityViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.main_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    public BaseActivity getActivity() {
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

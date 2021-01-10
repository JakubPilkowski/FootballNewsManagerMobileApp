package pl.android.footballnewsmanager.base;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;

public abstract class BaseViewModel extends ViewModel {

    private Providers providers;

    public void setProviders(Providers providers) {
        this.providers = providers;
    }

    public Activity getActivity() {
        return providers.getActivity();
    }

    public BaseFragment getFragment(){
        return providers.getFragment();
    }

    public ViewDataBinding getBinding(){
        return providers.getBinding();
    }

    public Navigator getNavigator() {return providers.getNavigator();}
}

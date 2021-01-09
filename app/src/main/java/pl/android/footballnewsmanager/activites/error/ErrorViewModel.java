package pl.android.footballnewsmanager.activites.error;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import pl.android.footballnewsmanager.base.BaseViewModel;
import pl.android.footballnewsmanager.helpers.ErrorView;

public class ErrorViewModel extends BaseViewModel {

    public ObservableField<ErrorView.OnTryAgainListener> tryAgainListenerObservable = new ObservableField<>();
    public ObservableInt status = new ObservableInt();


    public void init(int status) {
        loadView(status);
    }

    private void loadView(int status) {
        tryAgainListenerObservable.set(listener);
        this.status.set(status);
    }

    private ErrorView.OnTryAgainListener listener = () -> getActivity().onBackPressed();

}

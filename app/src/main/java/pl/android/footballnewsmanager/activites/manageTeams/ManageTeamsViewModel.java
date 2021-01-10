package pl.android.footballnewsmanager.activites.manageTeams;

import androidx.databinding.ObservableField;

import pl.android.footballnewsmanager.base.BaseFragment;
import pl.android.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityManageTeamsBinding;

public class ManageTeamsViewModel extends BaseViewModel {


    public ObservableField<String> title = new ObservableField<>();

    public void refreshToolbar() {
        BaseFragment fragment = ((ManageTeamsActivity) getActivity()).getCurrentFragment();
        ManageTeamsActivity activity = ((ManageTeamsActivity) getActivity());

        ActivityManageTeamsBinding binding = (ActivityManageTeamsBinding) getBinding();
        activity.setSupportActionBar(binding.toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(fragment.getHomeIcon() != 0){
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(fragment.getHomeIcon());
        }
        else{
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        title.set(fragment.getToolbarName());
    }


}

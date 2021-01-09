package pl.android.footballnewsmanager.activites.news_for_team;

import android.app.Activity;
import android.content.Intent;

import androidx.databinding.ViewDataBinding;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.base.BaseActivity;
import pl.android.footballnewsmanager.base.BaseFragment;
import com.example.footballnewsmanager.databinding.ActivityNewsForTeamBinding;
import pl.android.footballnewsmanager.helpers.Navigator;
import pl.android.footballnewsmanager.interfaces.Providers;
import pl.android.footballnewsmanager.interfaces.RecyclerViewItemsListener;

public class NewsForTeamActivity extends BaseActivity<ActivityNewsForTeamBinding, NewsForTeamViewModel> implements Providers, RecyclerViewItemsListener {

    private boolean refresh = false;

    @Override
    protected void initActivity(ActivityNewsForTeamBinding binding) {
        viewModel.setProviders(this);
        binding.setViewModel(viewModel);
        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", 0L);
        String name = intent.getStringExtra("name");
        String img = intent.getStringExtra("img");
        boolean isFavourite = intent.getBooleanExtra("favourite", false);
        viewModel.init(id, name, img, isFavourite, this);
    }

    @Override
    public void onBackPressed() {
        if(refresh)
            setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected Class<NewsForTeamViewModel> getViewModel() {
        return NewsForTeamViewModel.class;
    }

    @Override
    public int getIdFragmentContainer() {
        return R.id.news_for_team_container;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_news_for_team;
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


    @Override
    public void onChangeItem(Object oldItem, Object newItem) {
        refresh = true;
    }


}

package pl.android.footballnewsmanager.adapters.news.additionalInfo.teams;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;
import pl.android.footballnewsmanager.base.BaseRecyclerViewAdapter;
import pl.android.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamBinding;
import pl.android.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class AdditionalTeamsAdapter extends BaseRecyclerViewAdapter<UserTeam, BaseViewHolder> {

    private List<AdditionalTeamsAdapterViewModel> viewModels = new ArrayList<>();

    private Activity activity;

    public AdditionalTeamsAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.additional_info_team;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        AdditionalInfoTeamBinding binding = AdditionalInfoTeamBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        AdditionalTeamsAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new AdditionalTeamsAdapterViewModel();
            viewModels.add(viewModel);
            ((AdditionalInfoTeamBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position), holder.getBinding(), activity);
        }
        else{
            viewModel = viewModels.get(position);
            ((AdditionalInfoTeamBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }
}

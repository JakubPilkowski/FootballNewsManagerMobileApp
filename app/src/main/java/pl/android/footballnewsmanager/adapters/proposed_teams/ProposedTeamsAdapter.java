package pl.android.footballnewsmanager.adapters.proposed_teams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.ProposedTeamLayoutBinding;
import pl.android.footballnewsmanager.models.UserTeam;

import java.util.ArrayList;
import java.util.List;

public class ProposedTeamsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProposedTeamsAdapterViewModel> viewModels = new ArrayList<>();
    private List<UserTeam> items = new ArrayList<>();

    private final static int ITEM = 0;
    private final static int ITEM_LOADING = 1;
    public boolean isLoading = false;

    public void setItems(List<UserTeam> items) {
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (!isLoading)
            notifyItemChanged(items.size());
    }

    public List<ProposedTeamsAdapterViewModel> getViewModels() {
        return viewModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_team_layout, parent, false);
                ProposedTeamLayoutBinding proposedTeamLayoutBinding = ProposedTeamLayoutBinding.bind(view);
                return new TeamsViewHolder(view, proposedTeamLayoutBinding);
            }
            case ITEM_LOADING: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_progress_view, parent, false);
                return new ProgressViewHolder(view);
            }
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.proposed_team_layout, parent, false);
                ProposedTeamLayoutBinding proposedTeamLayoutBinding = ProposedTeamLayoutBinding.bind(view);
                return new TeamsViewHolder(view, proposedTeamLayoutBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (position == items.size() && isLoading)
            return;
        else {
            ProposedTeamsAdapterViewModel viewModel;
            if (viewModels.size() <= position) {
                viewModel = new ProposedTeamsAdapterViewModel();
                viewModels.add(viewModel);
                ((TeamsViewHolder) holder).getBinding().setViewModel(viewModel);
                viewModel.init(items.get(position));
            } else {
                viewModel = viewModels.get(position);
                ((TeamsViewHolder) holder).getBinding().setViewModel(viewModel);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size())
            return ITEM_LOADING;
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if(isLoading)
            return items.size() +1;
        return items.size();
    }

    public class TeamsViewHolder extends RecyclerView.ViewHolder {

        private ProposedTeamLayoutBinding binding;

        TeamsViewHolder(@NonNull View itemView, ProposedTeamLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public ProposedTeamLayoutBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

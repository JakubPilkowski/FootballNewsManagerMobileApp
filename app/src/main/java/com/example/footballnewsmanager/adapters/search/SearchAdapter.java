package com.example.footballnewsmanager.adapters.search;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.SearchLabelBinding;
import com.example.footballnewsmanager.databinding.SearchResultBinding;
import com.example.footballnewsmanager.models.SearchResult;
import com.example.footballnewsmanager.models.SearchType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchResult> items = new ArrayList<>();
    private List<SearchAdapterViewModel> viewModels = new ArrayList<>();

    private static final int ITEM = 0;
    private static final int LABEL = 1;
    private int newsCounter = 0;
    private int teamsCounter = 0;
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setItems(List<SearchResult> items) {
        notifyItemRangeRemoved(0, this.items.size());
        this.items.clear();
        this.viewModels.clear();
        this.items.addAll(items);
        newsCounter = 0;
        teamsCounter = 0;
        notifyDataSetChanged();
        for (SearchResult searchResult : items) {
            if (searchResult.getType().equals(SearchType.NEWS)) {
                newsCounter++;
            } else {
                teamsCounter++;
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
            SearchResultBinding binding = SearchResultBinding.bind(view);
            return new SearchResultViewHolder(view, binding);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_label, parent, false);
            SearchLabelBinding binding = SearchLabelBinding.bind(view);
            return new SearchLabelViewHolder(view, binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Resources resources = activity.getResources();
        if (position == 0) {
            SearchLabelViewModel viewModel = new SearchLabelViewModel();
            String label;
            if (newsCounter > 0 && teamsCounter > 0) label = resources.getString(R.string.news);
            else if (teamsCounter > 0 && newsCounter == 0) label = resources.getString(R.string.teams);
            else label = resources.getString(R.string.news);
            ((SearchLabelViewHolder) holder).getBinding().setViewModel(viewModel);
            viewModel.init(label);
        } else if (newsCounter > 0 && teamsCounter > 0) {
            if(position == newsCounter+1){
                SearchLabelViewModel viewModel = new SearchLabelViewModel();
                ((SearchLabelViewHolder) holder).getBinding().setViewModel(viewModel);
                viewModel.init(resources.getString(R.string.teams));
            }
            else{
                int updatePosition;
                if(position<=newsCounter){
                    updatePosition = position-1;
                }
                else{
                    updatePosition = position - 2;
                }
                SearchAdapterViewModel viewModel;
                if (viewModels.size() <= updatePosition) {
                    viewModel = new SearchAdapterViewModel();
                    viewModels.add(viewModel);
                } else {
                    viewModel = viewModels.get(updatePosition);
                }
                ((SearchResultViewHolder) holder).getBinding().setViewModel(viewModel);
                viewModel.init(items.get(updatePosition), activity);
            }
        } else {
            SearchAdapterViewModel viewModel;
            if (viewModels.size() <= position-1) {
                viewModel = new SearchAdapterViewModel();
                viewModels.add(viewModel);
            } else {
                viewModel = viewModels.get(position-1);
            }
            ((SearchResultViewHolder) holder).getBinding().setViewModel(viewModel);
            viewModel.init(items.get(position-1), activity);
        }
    }

    @Override
    public int getItemCount() {
        HashSet<SearchType> types = new HashSet<>();
        for (SearchResult searchResult : items) {
            types.add(searchResult.getType());
        }
        return items.size() + types.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LABEL;
        } else if (newsCounter > 0 && teamsCounter >0) {
            if (position == newsCounter + 1) {
                return LABEL;
            } else {
                return ITEM;
            }
        } else {
            return ITEM;
        }
    }

    public void removeItems() {

        if(newsCounter > 0 && teamsCounter >0){
            notifyItemRangeRemoved(0, this.items.size()+2);
        }
        else{
            notifyItemRangeRemoved(0, this.items.size()+1);
        }
        newsCounter = 0;
        teamsCounter = 0;
        this.items.clear();
        this.viewModels.clear();
    }


    class SearchLabelViewHolder extends RecyclerView.ViewHolder {

        private SearchLabelBinding binding;

        SearchLabelViewHolder(@NonNull View itemView, SearchLabelBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public SearchLabelBinding getBinding() {
            return binding;
        }
    }


    class SearchResultViewHolder extends RecyclerView.ViewHolder {

        private SearchResultBinding binding;

        SearchResultViewHolder(@NonNull View itemView, SearchResultBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public SearchResultBinding getBinding() {
            return binding;
        }
    }

}

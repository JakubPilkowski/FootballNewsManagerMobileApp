package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.databinding.NewsLayoutBinding;
import com.example.footballnewsmanager.models.LayoutManager;
import com.example.footballnewsmanager.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<News> items = new ArrayList<>();
    private List<NewsAdapterViewModel> viewModels = new ArrayList<>();
    private Activity activity;
    private final static int ITEM = 0;
    private final static int ITEM_LOADING = 1;
    public boolean isLoading = false;


    public void setItems(List<News> items) {
        Log.d("News", "Adapter-setItems ");
        int start = this.items.size();
        this.items.addAll(items);
        notifyItemRangeChanged(start, items.size());
    }

    public NewsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("News", "Adapter-onCreateViewHolder: ");
        View view;
        if(viewType==ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
            NewsLayoutBinding newsLayoutBinding = NewsLayoutBinding.bind(view);
            return new NewsViewHolder(view, newsLayoutBinding);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_progress_view, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("News", "loading "+isLoading);
        if(position == items.size() && isLoading)
            return;

        Log.d("News", "Adapter-onBindViewHolder: ");
        NewsAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new NewsAdapterViewModel();
            viewModels.add(viewModel);
        }
        else{
            viewModel = viewModels.get(position);
        }
        viewModel.init(items.get(position), activity);
        NewsLayoutBinding binding = ((NewsViewHolder)holder).getBinding();
        binding.setViewModel(viewModel);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == items.size()+1)
            return ITEM_LOADING;
        return ITEM;
    }

    @Override
    public int getItemCount() {
        if(isLoading)
            return items.size()+1;
        return items.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        NewsLayoutBinding binding;

        public NewsViewHolder(@NonNull View itemView, NewsLayoutBinding binding) {
            super(itemView);
            this.binding = binding;
        }

        public NewsLayoutBinding getBinding() {
            return binding;
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder{

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

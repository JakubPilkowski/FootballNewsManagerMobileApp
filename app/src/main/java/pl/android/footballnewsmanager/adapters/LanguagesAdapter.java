package pl.android.footballnewsmanager.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.footballnewsmanager.R;

import pl.android.footballnewsmanager.base.BaseRecyclerViewAdapter;
import pl.android.footballnewsmanager.base.BaseViewHolder;
import com.example.footballnewsmanager.databinding.LanguageItemBinding;

import pl.android.footballnewsmanager.interfaces.ProposedLanguageListener;
import pl.android.footballnewsmanager.models.LanguageField;

import java.util.ArrayList;
import java.util.List;

public class LanguagesAdapter extends BaseRecyclerViewAdapter<LanguageField, BaseViewHolder> {


   private List<LanguagesAdapterViewModel> viewModels = new ArrayList<>();

    private ProposedLanguageListener listener;

    @Override
    public int getItemLayoutRes() {
        return R.layout.language_item;
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType, View itemView) {
        LanguageItemBinding binding = LanguageItemBinding.bind(itemView);
        return new BaseViewHolder(itemView, binding);
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        LanguagesAdapterViewModel viewModel;
        if(viewModels.size()<=position){
            viewModel = new LanguagesAdapterViewModel();
            viewModels.add(viewModel);
            ((LanguageItemBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
            holder.setElement(items.get(position), listener);
        }
        else{
            viewModel = viewModels.get(position);
            ((LanguageItemBinding)holder.getBinding()).setViewModel(viewModel);
            holder.setViewModel(viewModel);
        }
    }

    public void setListener(ProposedLanguageListener listener) {
        this.listener = listener;
    }
}

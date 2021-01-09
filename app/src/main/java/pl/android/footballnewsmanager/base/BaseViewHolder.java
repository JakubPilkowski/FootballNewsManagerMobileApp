package pl.android.footballnewsmanager.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private BaseAdapterViewModel viewModel;
    private ViewDataBinding binding;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public BaseViewHolder(@NonNull View itemView, BaseAdapterViewModel viewModel) {
        super(itemView);
        this.viewModel = viewModel;
    }

    public BaseViewHolder(@NonNull View itemView, ViewDataBinding binding) {
        super(itemView);
        this.binding = binding;
    }

    public BaseViewHolder(@NonNull View itemView, ViewDataBinding binding, BaseAdapterViewModel viewModel) {
        super(itemView);
        this.viewModel = viewModel;
        this.binding = binding;
    }

    public void setElement(Object... args) {
        viewModel.init(args);
    }

    @NonNull
    public BaseAdapterViewModel getViewModel() {
        return viewModel;
    }

    @NonNull
    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setViewModel(BaseAdapterViewModel viewModel) {
        this.viewModel = viewModel;
    }
}

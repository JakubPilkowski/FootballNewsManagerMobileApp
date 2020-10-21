package com.example.footballnewsmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.models.SliderItem;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SliderViewHolder> {


    private List<SliderItem> items;
    private ViewPager2 viewPager2;

    public SlideAdapter(List<SliderItem> items, ViewPager2 viewPager2) {
        this.items = items;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.auth_logo_news,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setSliderContent(items.get(position));
        if(position == items.size()-1){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{


        private ImageView imageView;
        private TextView textView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_slider_image);
            textView = itemView.findViewById(R.id.image_slider_title);
        }

        void setSliderContent(SliderItem sliderItem){
            imageView.setImageDrawable(sliderItem.getDrawable());
            textView.setText(sliderItem.getTitle());
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            items.addAll(items);
            notifyDataSetChanged();
        }
    };
}

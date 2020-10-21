package com.example.footballnewsmanager.activites.auth;

import android.os.Handler;

import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.SlideAdapter;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.models.SliderItem;

import java.util.ArrayList;

public class AuthActivityViewModel extends BaseViewModel {



    private Handler handler = new Handler();
    private ViewPager2 viewPager2;

    public void init(){

//        viewPager2 = ((ActivityAuthBinding)getBinding()).authViewpager;

        ArrayList<SliderItem> sliderItems = new ArrayList<>();


        sliderItems.add(new SliderItem(getActivity().getResources().getDrawable(R.drawable.ic_logo_news),
                getActivity().getResources().getString(R.string.logo_news_title)));
        sliderItems.add(new SliderItem(getActivity().getResources().getDrawable(R.drawable.ic_logo_teams),
                getActivity().getResources().getString(R.string.logo_teams_title)));
        sliderItems.add(new SliderItem(getActivity().getResources().getDrawable(R.drawable.ic_logo_sites),
                getActivity().getResources().getString(R.string.logo_sites_title)));

        SlideAdapter slideAdapter = new SlideAdapter(sliderItems ,viewPager2);

//        viewPager2.setAdapter(slideAdapter);
//        viewPager2.setClipToPadding(false);
//        viewPager2.setClipChildren(false);
//        viewPager2.setOffscreenPageLimit(3);

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                handler.removeCallbacks(sliderRunnable);
//                handler.postDelayed(sliderRunnable, 3000);
//            }
//        });


    }

    public void onRegister(){

    }

    public void onLogin(){

    }

    public void onForgetPassword(){

    }



    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

}

package com.example.footballnewsmanager.activites.register;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableFloat;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.footballnewsmanager.activites.main.MainActivity;
import com.example.footballnewsmanager.adapters.ProposedSettingsViewPagerAdapter;
import com.example.footballnewsmanager.base.BaseViewModel;
import com.example.footballnewsmanager.databinding.ActivityProposedSettingsBinding;
import com.example.footballnewsmanager.helpers.ScreenHelper;

import java.util.Observable;

public class ProposedSettingsActivityViewModel extends BaseViewModel {


    public ObservableInt navigationMargin = new ObservableInt();
    public ObservableBoolean enabled = new ObservableBoolean(false);
    public ObservableInt ballLeftMargin = new ObservableInt();
    public ViewPager2 viewPager2;
    public View ball;
    public int item;
    public int screenWidth;
    public void init(){
        item =0;
        ball = ((ActivityProposedSettingsBinding)getBinding()).proposedSettingsBall;
        checkBackButtonEnabled();
        screenWidth = ScreenHelper.getScreenWidth(getActivity());
        ballLeftMargin.set(screenWidth*4/25);
        navigationMargin.set(ScreenHelper.getNavBarHeight(getActivity().getApplicationContext()));
        viewPager2 = ((ActivityProposedSettingsBinding)getBinding()).proposedSettingsViewpager;
        ProposedSettingsViewPagerAdapter proposedSettingsViewPagerAdapter = new ProposedSettingsViewPagerAdapter((FragmentActivity) getActivity());
        viewPager2.setAdapter(proposedSettingsViewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                checkBackButtonEnabled();
                Log.d("item", String.valueOf(item));
                Log.d("item", String.valueOf(position));
                float translateValue = screenWidth*17/75;
                ball.animate()
                        .rotation(180*item)
                        .translationX(translateValue*item).setDuration(400).start();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }
        });
    }

    public void checkBackButtonEnabled(){
        enabled.set(item>0);
    }

    public void next(){
        if(item==2){

            float translateValue = screenWidth*17/75;
            ball.animate()
                    .rotation(180*3)
                    .translationX(translateValue*3).setDuration(400).start();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            getActivity().startActivity(intent);
//            getActivity().finish();
            //zapisanie do bazy danych przejście ustawień początkowych
        }else{
            item++;
            viewPager2.setCurrentItem(item);
        }
    }

    public void back(){
        item--;
        viewPager2.setCurrentItem(item);
    }
}

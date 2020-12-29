package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.footballnewsmanager.R;

public class NewsPlaceholder extends LinearLayout {

    private Context context;
    private Animation animation;
    private View bottleView;
    private Button addTeamsButton;


    private OnAddTeamsInterface onAddTeamsInterface;

    public void setOnAddTeamsInterface(OnAddTeamsInterface onAddTeamsInterface) {
        this.onAddTeamsInterface = onAddTeamsInterface;
    }

    public interface OnAddTeamsInterface {
        void onAdd();
    }

    public NewsPlaceholder(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public int getLayoutRes() {
        return R.layout.news_placeholder;
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutRes(), this);
        initBottleAnimation();
        initButton();
    }

    private void initButton() {
        addTeamsButton = findViewById(R.id.news_placeholder_button);
        addTeamsButton.setOnClickListener(v -> onAddTeamsInterface.onAdd());
    }

    private void initBottleAnimation() {
        bottleView = findViewById(R.id.news_placeholder_bottle);
        animation = AnimationUtils.loadAnimation(bottleView.getContext(), R.anim.shake);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bottleView.startAnimation(animation);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
                new Handler().postDelayed(() -> onAnimationStart(animation), 750);
            }
        });
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == VISIBLE && bottleView.getAnimation() == null) {
            bottleView.startAnimation(animation);
        } else if (visibility == INVISIBLE)
            bottleView.clearAnimation();
        super.onVisibilityChanged(changedView, visibility);
    }
}

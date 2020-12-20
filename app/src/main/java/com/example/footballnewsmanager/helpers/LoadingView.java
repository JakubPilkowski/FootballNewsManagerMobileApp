package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.footballnewsmanager.R;

public class LoadingView extends LinearLayout {

    private static final String TAG = "LoadingView";
    private View loadingView;
    private Animation loadingAnimation;
    private Context context;

    private void initControl()
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutRes(), this);
        initLoadingAnimation();
    }
    public int getLayoutRes() {
        return R.layout.progress_dialog;
    }

    private void initLoadingAnimation() {
        LinearLayout linearLayout = findViewById(R.id.progress_dialog_layout);
        loadingView = linearLayout.getChildAt(0);
        loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_translate);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initControl();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        Log.d(TAG, "onVisibilityChanged: ");
        if(visibility == VISIBLE)
            loadingView.startAnimation(loadingAnimation);
        else 
            loadingView.clearAnimation();
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow: ");
//        loadingView.clearAnimation();
        super.onDetachedFromWindow();
    }
}

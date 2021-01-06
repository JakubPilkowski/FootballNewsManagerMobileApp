package com.example.footballnewsmanager.fragments.auth.success_fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.footballnewsmanager.R;

public class AnimatedSuccessView extends View {

    public interface ResultListener {
        void onResultAnimEnd();
    }

    private Paint paint;
    private Path path;
    private Point point;

    private ValueAnimator secondWingAnimation;

    private int width;
    private int height;

    private ResultListener resultListener;

    public AnimatedSuccessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        point = new Point();

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30f);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void startAnimation() {
        point.set(width * 33 / 100, height / 2);
        path.moveTo(point.x, point.y);
        ValueAnimator firstWingAnimation = ValueAnimator.ofInt(0, 100);
        firstWingAnimation.setDuration(300);
        firstWingAnimation.addUpdateListener(firstWingUpdateListener);
        firstWingAnimation.addListener(firstWingAnimationListener);
        firstWingAnimation.start();
        secondWingAnimation = ValueAnimator.ofInt(0,100);
        secondWingAnimation.setDuration(300);
        secondWingAnimation.addUpdateListener(secondWingUpdateListener);
        secondWingAnimation.addListener(secondWingAnimationListener);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    private ValueAnimator.AnimatorUpdateListener firstWingUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            float xValue = (width*48/100-point.x)*value/100;
            float yValue = (height*13/20-point.y)*value/100;
            path.lineTo(point.x + xValue,point.y+ yValue);
            invalidate();
        }
    };

    private AnimatorListenerAdapter firstWingAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            point.set(getMeasuredWidth()*48/100, getMeasuredHeight()*13/20);
            secondWingAnimation.start();
            super.onAnimationEnd(animation);
        }
    };

    private ValueAnimator.AnimatorUpdateListener secondWingUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            float xValue = (width*13/20-point.x)*value/100;
            float yValue = (height*7/20-point.y)*value/100;
            path.lineTo(point.x + xValue,point.y+ yValue);
            invalidate();
        }
    };

    private AnimatorListenerAdapter secondWingAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if(resultListener!=null)
                resultListener.onResultAnimEnd();
        }
    };

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        startAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}

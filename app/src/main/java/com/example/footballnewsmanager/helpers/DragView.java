package com.example.footballnewsmanager.helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.interfaces.DragViewListener;

public class DragView extends ViewGroup {

    private ViewDragHelper mDragHelper;
    private int mDragRange;
    private View container;
    private View bell;
    private int mTop;
    private float mDragOffset;
    private boolean isActive = false;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        container = findViewById(R.id.language_drag_view_container);
        bell = findViewById(R.id.bell);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    private DragViewListener dragViewListener;

    public void setDragViewListener(DragViewListener dragViewListener){
        this.dragViewListener = dragViewListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if(action!=MotionEvent.ACTION_DOWN){
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        else{
            final float y = ev.getY();
            return mDragHelper.isViewUnder(bell, (int) ev.getY(), (int) y);
        }
    }
    boolean smoothSlideTo(float slideOffset){
        final int topBound = getPaddingTop();
        int y = (int) (topBound + slideOffset * mDragRange);
        if(mDragHelper.smoothSlideViewTo(container,container.getLeft(),y)){
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        final int action = event.getAction();
        final float y = event.getY();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (y > getHeight()/2 && isActive ) {
                    smoothSlideTo(1f);
                }
                else{
                smoothSlideTo(0f);
                }
                break;
            }
        }
        return true;
    }


    private class DragHelperCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == container;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            isActive = true;
            mTop = top;
            mDragOffset = (float) top / mDragRange;
            requestLayout();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if(state==0)
                isActive=false;
            if(state==0 && mTop > 0){
                dragViewListener.onClose();
            }
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
        }
    }

    @Override
    public void computeScroll() {
        if(mDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, MeasureSpec.UNSPECIFIED),
                resolveSizeAndState(container.getMeasuredHeight(), heightMeasureSpec, MeasureSpec.UNSPECIFIED));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight();

        container.layout(
                l,
                mTop,
                r,
                b
        );
    }
}

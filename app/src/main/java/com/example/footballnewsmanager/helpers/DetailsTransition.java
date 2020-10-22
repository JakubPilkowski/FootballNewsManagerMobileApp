package com.example.footballnewsmanager.helpers;

import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

public class DetailsTransition extends TransitionSet {
    public DetailsTransition() {
        setOrdering(ORDERING_TOGETHER);
                addTransition(new ChangeBounds()).
                setDuration(400);

    }
}

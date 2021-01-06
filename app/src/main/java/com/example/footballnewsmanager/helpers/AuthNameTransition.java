package com.example.footballnewsmanager.helpers;

import android.transition.ChangeBounds;
import android.transition.TransitionSet;

public class AuthNameTransition extends TransitionSet {
    public AuthNameTransition() {
        setOrdering(ORDERING_TOGETHER);
                addTransition(new ChangeBounds()).
                setDuration(300);

    }
}

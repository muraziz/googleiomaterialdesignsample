package com.ioext2015tash.activitytransitionanim.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by aziz on 6/2/15.
 */
public class MediaButton extends ImageButton {
    boolean mPlaying = false;
    private static final int[] PLAYING_STATE_SET = {
            android.R.attr.state_checked
    };

    public MediaButton(Context context) {
        super(context);
    }

    public MediaButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    private void toggle() {
        mPlaying = !mPlaying;
        refreshDrawableState();
    }

    public boolean isPlaying() {
        return mPlaying;
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mPlaying) {
            mergeDrawableStates(drawableState, PLAYING_STATE_SET);
        }
        return drawableState;
    }
}

package com.ioext2015tash.activitytransitionanim.interpolators;

import android.view.animation.Interpolator;

/**
 * Created by aziz on 6/3/15.
 */
public class EaseOutCirc implements Interpolator {

    @Override
    public float getInterpolation(float pos) {
        return (float)Math.sqrt(1 - Math.pow((pos-1), 2));
    }
}

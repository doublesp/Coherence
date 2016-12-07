package com.doublesp.coherence.utils;

import android.view.animation.Interpolator;

public class MVAccelerateDecelerateInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float v) {
        float x;
        if (v < 0.5f) {
            x = v * 2.0f;
            return 0.5f * x * x * x * x * x;
        }
        x = (v - 0.5f) * 2 - 1;
        return 0.5f * x * x * x * x * x + 1;

    }
}

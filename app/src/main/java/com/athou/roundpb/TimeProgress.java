package com.athou.roundpb;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 时间progressbar
 * Created by czwathou on 2017/7/13.
 */
public class TimeProgress extends RoundProgressBar {
    public TimeProgress(Context context) {
        super(context);
    }

    public TimeProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String makeText(int progress, int max) {
        return formatSecondsToMMSS(progress);
    }

    public static String formatSecondsToMMSS(final int second) {
        int s = second;
        int m = s / 60;
        s = s % 60;
        return String.format("%02d:%02d", m, s);
    }
}

package com.athou.roundpb;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import static com.athou.roundpb.TimeProgress.formatSecondsToMMSS;

/**
 * Created by czwathou on 2017/7/13.
 */

public class PercentProgress extends RoundProgressBar{
    public PercentProgress(Context context) {
        super(context);
    }

    public PercentProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String makeText(int progress, int max) {
        if (progress >= max && !TextUtils.isEmpty(endText)) {
            return endText;
        } else {
            return String.format("%.0f%%", 1f * progress / max * 100);
        }
    }
}

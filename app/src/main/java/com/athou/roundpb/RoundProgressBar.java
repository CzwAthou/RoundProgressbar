package com.athou.roundpb;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by czwathou on 2017/7/13.
 */

public class RoundProgressBar extends View {
    private static final String TAG = RoundProgressBar.class.getSimpleName();
    private final int DEFAULT_MAX = 100;
    private final int DEFAULT_PROGRESS_COLOR = Color.RED;
    private final int DEFAULT_PROGRESS_TEXT_COLOR = Color.WHITE;
    private final float DEFAULT_TEXT_SIZE = 26f;

    private Paint mBackgroundPaint;//背景画笔
    private Paint mProgressPaint;//进度画笔
    private Paint mTextPaint;//文字

    private int mRadius = 0;
    private int mProgressColor;
    private int mProgressBgColor;
    private int mTextColor;
    private float mTextSize;
    private int mStokeWidth;

    protected String endText = null; //进度结束后显示的文字
    private int mViewWidth;
    private int mViewHeight;

    private int mProgress = 0;//当前进度
    private int mMax = 0;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextProgressBar);
        try {
            mMax = a.getInt(R.styleable.TextProgressBar_max, DEFAULT_MAX);
            mProgressColor = a.getColor(R.styleable.TextProgressBar_progressColor, DEFAULT_PROGRESS_COLOR);
            mProgressBgColor = a.getColor(R.styleable.TextProgressBar_progressBgColor, Color.TRANSPARENT);
            mTextColor = a.getColor(R.styleable.TextProgressBar_textColor, DEFAULT_PROGRESS_TEXT_COLOR);
            mTextSize = a.getDimension(R.styleable.TextProgressBar_textSize, DEFAULT_TEXT_SIZE);
            mStokeWidth = a.getInt(R.styleable.TextProgressBar_stroke_width, 0);
            endText = a.getString(R.styleable.TextProgressBar_endText);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mProgressBgColor);//默认背景颜色

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//进度条画笔
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mProgressColor);//进度条背景颜色

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文本
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        if (mRadius <= 0) {
            mRadius = mViewHeight / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        RectF bgRectF = new RectF(0, 0, mViewWidth, mViewHeight);
        canvas.drawRoundRect(bgRectF, mRadius, mRadius, mBackgroundPaint);
        //画进度条
        drawProgress(canvas);
        //画文字
        Paint.FontMetricsInt mFontMetrics = mTextPaint.getFontMetricsInt();
        canvas.drawText(makeText(mProgress, mMax), mViewWidth / 2, (mViewHeight - mFontMetrics.bottom - mFontMetrics.top) / 2, mTextPaint);
    }

    Path progressPath = new Path();

    private void drawProgress(Canvas canvas) {
        int pbWidth = mViewWidth - mStokeWidth * 2;
        int pbHeight = mViewHeight - mStokeWidth * 2;
        int pbRadius = pbHeight / 2;
        float pbLength = pbWidth * (mProgress * 1.0f / mMax);
        progressPath.reset();
        if (pbLength < pbRadius * 2) {
            float angle = (float) (Math.acos((pbRadius - pbLength / 2) / pbRadius) / Math.PI * 180);

            RectF rectFLeft = new RectF(0, 0, pbRadius * 2, pbRadius * 2);
            progressPath.addArc(rectFLeft, 180 - angle, angle * 2);

            RectF rectFRight = new RectF(-(pbRadius * 2 - pbLength), 0, pbLength, pbRadius * 2);
            progressPath.addArc(rectFRight, -angle, angle * 2);

            progressPath.close();
        } else {
            RectF rectFLeft = new RectF(0, 0, pbRadius * 2, pbRadius * 2);
            progressPath.addArc(rectFLeft, 90, 180);

            RectF rectFMiddle = new RectF(pbRadius, 0, pbLength - pbRadius, pbHeight);
            progressPath.addRect(rectFMiddle, Path.Direction.CW);

            RectF rectFRight = new RectF(pbLength - 2 * pbRadius, 0, pbLength, pbHeight);
            progressPath.addArc(rectFRight, -90, 180);

            progressPath.close();
        }
        progressPath.offset(mStokeWidth, mStokeWidth);
        canvas.drawPath(progressPath, mProgressPaint);
    }

    protected String makeText(int progress, int max) {
        if (progress >= mMax && !TextUtils.isEmpty(endText)) {
            return endText;
        } else {
            return mProgress + "/" + max;
        }
    }

    public void setCurProgress(final int progress) {
        if (progress > mMax) {
            return;
        }
        int diff = progress - this.mProgress;
        long duration = Math.abs(diff) * 100 > 2000 ? 2000 : Math.abs(diff) * 100;
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", mProgress, progress).setDuration(duration);
        animator.start();
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        mProgress = progress > mMax ? mMax : progress;
        invalidate();
    }

    /**
     * 设置最大进度
     *
     * @param maxProgress
     */
    public void setMax(int maxProgress) {
        mMax = maxProgress < 0 ? 0 : maxProgress;
        invalidate();
    }

    /**
     * 获取最大进度
     *
     * @return
     */
    public int getMax() {
        return mMax;
    }

    /**
     * 设置背景颜色
     */
    public void setBgColor(@ColorInt int color) {
        mBackgroundPaint.setColor(color);
        invalidate();
    }

    /**
     * 设置进度条颜色
     */
    public void setProgressColor(@ColorInt int color) {
        mProgressPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
        invalidate();
    }
}


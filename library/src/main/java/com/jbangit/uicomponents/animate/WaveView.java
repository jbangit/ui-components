package com.jbangit.uicomponents.animate;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;

public class WaveView extends View {

    public static final int PERIOD_COUNT = 2;

    private Paint mPaint = new Paint();

    private float mStartX;

    private float mStartY;

    private float mWidth;

    private float mHeight;

    private ValueAnimator mWaveAnimator;

    private Path mPath = new Path();

    private float mAttrHorizon;

    private float mAttrTop;

    {
        int color = Globals.getPrimaryColor(getContext());
        mPaint.setColor(ColorUtils.setAlphaComponent(color, 0x88));
        mPaint.setStrokeWidth(DensityUtils.getPxFromDp(getContext(), 3));
        mPaint.setStyle(Paint.Style.FILL);
    }

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        mAttrHorizon = typedArray.getFraction(R.styleable.WaveView_waveViewHorizon, 1, 1, .5f);
        mAttrTop = typedArray.getFraction(R.styleable.WaveView_waveViewTop, 1, 1, .1f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();

        mStartX = 0;
        mStartY = height * (1 - mAttrHorizon);
        mWidth = getMeasuredWidth();
        mHeight = height * mAttrTop;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mWaveAnimator = ValueAnimator.ofFloat(0, 0.5f, 1);
        mWaveAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mStartX = ((float) animation.getAnimatedValue()) * -mWidth;
                        invalidate();
                    }
                });
        mWaveAnimator.setInterpolator(new LinearInterpolator());
        mWaveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveAnimator.setRepeatMode(ValueAnimator.RESTART);
        mWaveAnimator.setDuration(1000);
        mWaveAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWaveAnimator != null) {
            mWaveAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(mStartX, mStartY);

        float halfWidth = mWidth / 2f;
        float totalHeight = mHeight;
        for (int i = 0; i < PERIOD_COUNT; i++) {
            drawHalf(totalHeight, halfWidth, true);
            drawHalf(totalHeight, halfWidth, false);
        }

        mPath.lineTo(getMeasuredWidth(), getMeasuredHeight());
        mPath.lineTo(0, getMeasuredHeight());
        mPath.lineTo(mStartX, getMeasuredHeight());

        canvas.drawPath(mPath, mPaint);
    }

    private void drawHalf(float totalHeight, float halfWidth, boolean isUp) {
        mPath.rQuadTo(halfWidth / 2, totalHeight * (isUp ? -1 : 1), halfWidth, 0);
    }
}

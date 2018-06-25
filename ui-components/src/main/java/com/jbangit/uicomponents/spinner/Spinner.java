package com.jbangit.uicomponents.spinner;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.Globals;

public class Spinner extends FrameLayout {

    private static final int DURATION = 30;

    private final ValueAnimator mNumberAnimator = ValueAnimator.ofInt(0, 100);

    private ImageView mPlus;

    private ImageView mMinus;

    private TextView mNumberView;

    private int mNumber;

    private int mMiniNumber;

    private int mMaxNumber;

    private OnSpinnerChangeListener mOnSpinnerChangeListener;

    public Spinner(@NonNull Context context) {
        super(context);
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Spinner);
        mNumber = typedArray.getInt(R.styleable.Spinner_spinnerNumber, 1);
        mMaxNumber = typedArray.getInt(R.styleable.Spinner_spinnerMaxNumber, 99);
        mMiniNumber = typedArray.getInt(R.styleable.Spinner_spinnerMiniNumber, 1);
        typedArray.recycle();
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        setNumber(number, false);
    }

    public int getMiniNumber() {
        return mMiniNumber;
    }

    public void setMiniNumber(int miniNumber) {
        if (mNumber < miniNumber) {
            setNumber(miniNumber);
        }
        mMiniNumber = miniNumber;
    }

    public int getMaxNumber() {
        return mMaxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        if (mNumber > maxNumber) {
            setNumber(maxNumber);
        }
        mMaxNumber = maxNumber;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNumberAnimator.cancel();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mNumber = mNumber;
        saveState.mMaxNumber = mMaxNumber;
        saveState.mMiniNumber = mMiniNumber;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        setNumber(saveState.mNumber);
        setMaxNumber(saveState.mMaxNumber);
        setMiniNumber(saveState.mMiniNumber);
        super.onRestoreInstanceState(saveState.getSuperState());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate();

        initViews();
    }

    private void inflate() {
        View layout = inflate(getContext(), R.layout.view_spinner, this);
        mPlus = layout.findViewById(R.id.plus);
        mMinus = layout.findViewById(R.id.minus);
        mNumberView = layout.findViewById(R.id.number);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int rippleColor = getResources().getColor(R.color.colorTextDark);
            RippleDrawable plusDrawable =
                    new RippleDrawable(
                            ColorStateList.valueOf(rippleColor), mPlus.getDrawable(), null);
            mPlus.setImageDrawable(plusDrawable);
            RippleDrawable minusDrawable =
                    new RippleDrawable(
                            ColorStateList.valueOf(rippleColor), mMinus.getDrawable(), null);
            mMinus.setImageDrawable(minusDrawable);
        }

        int primaryColor = Globals.getPrimaryColor(getContext());
        DrawableCompat.setTint(mPlus.getDrawable(), primaryColor);
        DrawableCompat.setTint(mMinus.getDrawable(), primaryColor);

        showNumber();
        mNumberAnimator.setInterpolator(new LinearInterpolator());
        mNumberAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setNumber((Integer) animation.getAnimatedValue(), true);
                    }
                });

        mPlus.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNumber < mMaxNumber) {
                            setNumber(mNumber + 1, true);
                        }
                    }
                });
        mPlus.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mNumberAnimator.setIntValues(mNumber, mMaxNumber);
                        mNumberAnimator.setDuration((mMaxNumber - mNumber) * DURATION);
                        mNumberAnimator.start();
                        return true;
                    }
                });
        mPlus.setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            mNumberAnimator.cancel();
                        }
                        return false;
                    }
                });
        mMinus.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNumber > mMiniNumber) {
                            setNumber(mNumber - 1, true);
                        }
                    }
                });
        mMinus.setOnLongClickListener(
                new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mNumberAnimator.setIntValues(mNumber, mMiniNumber);
                        mNumberAnimator.setDuration((mNumber - mMiniNumber) * DURATION);
                        mNumberAnimator.start();
                        return true;
                    }
                });
        mMinus.setOnTouchListener(
                new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            mNumberAnimator.cancel();
                        }
                        return false;
                    }
                });
    }

    private void showNumber() {
        mNumberView.setText(String.valueOf(mNumber));
    }

    private void setNumber(int number, boolean withUser) {
        mNumber = number;
        showNumber();

        if (withUser && mOnSpinnerChangeListener != null) {
            mOnSpinnerChangeListener.onSpinnerChange(this, mNumber);
        }
    }

    public OnSpinnerChangeListener getOnSpinnerChangeListener() {
        return mOnSpinnerChangeListener;
    }

    public void setOnSpinnerChangeListener(OnSpinnerChangeListener onSpinnerChangeListener) {
        mOnSpinnerChangeListener = onSpinnerChangeListener;
    }

    public interface OnSpinnerChangeListener {

        void onSpinnerChange(@NonNull Spinner spinner, int number);
    }

    private static class SaveState extends BaseSavedState {

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };

        int mNumber;

        int mMaxNumber;

        int mMiniNumber;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);
            mNumber = source.readInt();
            mMaxNumber = source.readInt();
            mMiniNumber = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mNumber);
            out.writeInt(mMaxNumber);
            out.writeInt(mMiniNumber);
        }
    }
}

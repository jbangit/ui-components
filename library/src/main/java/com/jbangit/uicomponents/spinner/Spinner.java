package com.jbangit.uicomponents.spinner;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
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

    public static final int DEFAULT_NUMBER = 1;

    public static final int DEFAULT_MAX_NUMBER = 99;

    public static final int DEFAULT_MINI_NUMBER = 1;

    private final ValueAnimator mNumberAnimator = ValueAnimator.ofInt(0, 100);

    private View mLayout;

    private View mPlus;

    private View mMinus;

    private TextView mNumberView;

    private int mAttrLayoutId;

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
        mAttrLayoutId =
                typedArray.getResourceId(
                        R.styleable.Spinner_spinnerLayoutId, R.layout.view_spinner);
        mNumber = typedArray.getInt(R.styleable.Spinner_spinnerNumber, DEFAULT_NUMBER);
        mMaxNumber = typedArray.getInt(R.styleable.Spinner_spinnerMaxNumber, DEFAULT_MAX_NUMBER);
        mMiniNumber = typedArray.getInt(R.styleable.Spinner_spinnerMiniNumber, DEFAULT_MINI_NUMBER);
        typedArray.recycle();
    }

    public Spinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public int getNumber() {
        return mNumber;
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

    @Override
    public int getBaseline() {
        // layout to get child view top
        mLayout.layout(0, 0, mLayout.getMeasuredWidth(), mLayout.getMeasuredHeight());
        mLayout.requestLayout();
        return mNumberView.getTop() + mNumberView.getBaseline();
    }

    private void inflate() {
        mLayout = inflate(getContext(), mAttrLayoutId, this);
        mPlus = mLayout.findViewById(R.id.spinner_plus);
        mMinus = mLayout.findViewById(R.id.spinner_minus);
        mNumberView = mLayout.findViewById(R.id.spinner_number);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        if (isDefaultLayout()) {
            // add ripple
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ((ImageView) mPlus)
                        .setImageDrawable(
                                Globals.addRipple(getContext(), ((ImageView) mPlus).getDrawable()));
                ((ImageView) mMinus)
                        .setImageDrawable(
                                Globals.addRipple(
                                        getContext(), ((ImageView) mMinus).getDrawable()));
            }

            // color hint
            int primaryColor = Globals.getPrimaryColor(getContext());
            DrawableCompat.setTint(((ImageView) mPlus).getDrawable(), primaryColor);
            DrawableCompat.setTint(((ImageView) mMinus).getDrawable(), primaryColor);
        }

        mNumberAnimator.setInterpolator(new LinearInterpolator());
        mNumberAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setNumber((Integer) animation.getAnimatedValue());
                    }
                });

        mPlus.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNumber < mMaxNumber) {
                            setNumber(mNumber + 1);
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
                            setNumber(mNumber - 1);
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setNumber(mNumber);
    }

    private boolean isDefaultLayout() {
        return mAttrLayoutId == R.layout.view_spinner;
    }

    private void showNumber() {
        mNumberView.setText(String.valueOf(mNumber));
    }

    private void setNumber(int number) {
        mNumber = number;
        showNumber();

        if (mOnSpinnerChangeListener != null) {
            mOnSpinnerChangeListener.onSpinnerChange(this, mNumber);
        }
    }

    public OnSpinnerChangeListener getOnSpinnerChangeListener() {
        return mOnSpinnerChangeListener;
    }

    public void setOnSpinnerChangeListener(OnSpinnerChangeListener listener) {
        mOnSpinnerChangeListener = listener;
    }

    public void setOnSpinnerChangeListener(final SimpleOnSpinnerChangeListener listener) {
        if (listener == null) {
            mOnSpinnerChangeListener = null;
            return;
        }
        mOnSpinnerChangeListener = new OnSpinnerChangeListener() {
            @Override
            public void onSpinnerChange(@NonNull Spinner spinner, int number) {
                listener.onSpinnerChange(number);
            }
        };
    }

    public interface OnSpinnerChangeListener {

        void onSpinnerChange(@NonNull Spinner spinner, int number);
    }

    public interface SimpleOnSpinnerChangeListener {

        void onSpinnerChange(int number);
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

    @BindingAdapter("spinnerNumber")
    public static void setNumber(Spinner spinner, int number) {
        spinner.setNumber(number);
    }

    @BindingAdapter("spinnerMaxNumber")
    public static void setMaxNumber(Spinner spinner, int maxNumber) {
        spinner.setNumber(maxNumber);
    }

    @BindingAdapter("spinnerMineNumber")
    public static void setMiniNumber(Spinner spinner, int miniNumber) {
        spinner.setNumber(miniNumber);
    }

    @BindingAdapter("spinnerOnChange")
    public static void setOnChangeListener(Spinner spinner,
                                           SimpleOnSpinnerChangeListener listener) {
        spinner.setOnSpinnerChangeListener(listener);
    }
}

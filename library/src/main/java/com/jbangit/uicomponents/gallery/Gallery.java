package com.jbangit.uicomponents.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.view.ViewRecycler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * There are some PictureView in the Gallery
 *
 * <p>{@link Gallery#setPictures(List)}
 *
 * <p>{@link Gallery#addPicture(String)}
 *
 * <p>{@link Gallery#removePicture(int)}}
 *
 * <p>{@link Gallery#setAddMode(boolean)}
 *
 * <p>{@link Gallery#setDeleteMode(boolean)}
 *
 * <p>{@link Gallery#setOnClickPictureListener(OnClickPictureListener)}
 *
 * <p>{@link Gallery#setOnClickAddPictureListener(OnClickAddPictureListener)}
 */
public class Gallery extends ViewGroup {

    public static final int PICTURE_MAX_NUM_INFINITE = Integer.MAX_VALUE;

    private final List<PictureViewHolder> mPictureViewHolders = new ArrayList<>();

    private View mAddPictureView;

    private final ViewRecycler mViewRecycler = new ViewRecycler();

    private final List<String> mPictures = new ArrayList<>();

    private int mAttrRowCount = 4;

    private boolean mIsDeleteMode = false;

    private boolean mIsAddMode = false;

    private int mPictureMax = PICTURE_MAX_NUM_INFINITE;

    private int mAttrPadding = 0;

    private int mAttrDeleteButtonMargin = DensityUtils.getPxFromDp(getContext(), 4);

    private Drawable mAttrPlaceholder = null;

    private int mAttrAddViewLayoutId;

    private Drawable mAttrAddDrawable = null;

    private Drawable mAttrDeleteDrawable;

    private RequestOptions mGlideOption;

    private OnClickPictureListener mOnClickPictureListener;

    private OnClickAddPictureListener mOnClickAddPictureListener;

    private boolean mAttrIsAddMode = false;

    private boolean mAttrIsDeleteMode = false;

    public Gallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initGlide();
        initAddPictureView();
    }

    public Gallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initGlide();
        initAddPictureView();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Gallery);

        mAttrDeleteButtonMargin =
                typedArray.getDimensionPixelOffset(
                        R.styleable.Gallery_galleryDeleteButtonMargin, mAttrDeleteButtonMargin);

        mAttrPadding =
                typedArray.getDimensionPixelOffset(
                        R.styleable.Gallery_galleryPadding, mAttrPadding);

        if (mAttrPadding > mAttrDeleteButtonMargin) {
            mAttrPadding -= mAttrDeleteButtonMargin;
        }

        mAttrRowCount = typedArray.getInt(R.styleable.Gallery_galleryRowNumber, mAttrRowCount);

        int grayColor = ContextCompat.getColor(context, R.color.colorTextLightGray);

        mAttrPlaceholder = typedArray.getDrawable(R.styleable.Gallery_galleryPlaceholderDrawable);
        if (mAttrPlaceholder == null) {
            mAttrPlaceholder =
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_picture_placeholder);
            DrawableCompat.setTint(Objects.requireNonNull(mAttrPlaceholder), grayColor);
        }

        mAttrAddDrawable = typedArray.getDrawable(R.styleable.Gallery_galleryAddDrawable);
        if (mAttrAddDrawable == null) {
            mAttrAddDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_add_picture);
            DrawableCompat.setTint(Objects.requireNonNull(mAttrAddDrawable), grayColor);
        }

        mAttrAddViewLayoutId = typedArray.getResourceId(R.styleable.Gallery_galleryAddView, -1);

        mAttrDeleteDrawable = typedArray.getDrawable(R.styleable.Gallery_galleryDeleteDrawable);
        if (mAttrDeleteDrawable == null) {
            mAttrDeleteDrawable =
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_picture);
        }

        mAttrIsAddMode = typedArray.getBoolean(R.styleable.Gallery_galleryAddMode, mAttrIsAddMode);
        mAttrIsDeleteMode = typedArray.getBoolean(R.styleable.Gallery_galleryDeleteMode, mAttrIsDeleteMode);
        mPictureMax = typedArray.getInt(R.styleable.Gallery_galleryPictureMax, mPictureMax);

        typedArray.recycle();
    }

    private void initGlide() {
        if (!isInEditMode()) {
            mGlideOption = new RequestOptions().centerCrop().placeholder(mAttrPlaceholder);
        }
    }

    private void initAddPictureView() {
        if (mAttrAddViewLayoutId != -1) {
            mAddPictureView =
                    LayoutInflater.from(getContext()).inflate(mAttrAddViewLayoutId, this, false);
        } else {
            mAddPictureView =
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.view_item_gallery, this, false);
            ((ImageView) mAddPictureView.findViewById(R.id.gallery_picture))
                    .setImageDrawable(mAttrAddDrawable);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        initEditMode();
    }

    private void initEditMode() {
        if (!isInEditMode()) {
            return;
        }

        List<String> pictures = new ArrayList<>();

        int count;
        if (isAddMode()) {
            if (mPictureMax == PICTURE_MAX_NUM_INFINITE) {
                mPictureMax = 9;
            }

            count = mPictureMax - 1;
        } else {
            count = mAttrRowCount;
        }

        for (int i = 0; i < count; i++) {
            pictures.add("edit mode");
        }
        setPictures(pictures);
    }

    private void initView() {
        setAddMode(mAttrIsAddMode);
        setDeleteMode(mAttrIsDeleteMode);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = widthSize;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }

        if (getChildCount() == 0) {
            setMeasuredDimension(width, 0);
            return;
        }

        int allWidthPadding = (mAttrRowCount - 1) * mAttrPadding;
        int pictureWidth = (width - mAttrDeleteButtonMargin - allWidthPadding) / mAttrRowCount;

        measureAllPictureView(pictureWidth);
        measureAddPictureView(pictureWidth);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int height = heightSize;
        int allPictureViewHeight = getLines() * getPictureViewHeight();
        int allHeightPadding = (getLines() - 1) * mAttrPadding;
        int expectedHeight = allPictureViewHeight + allHeightPadding + mAttrDeleteButtonMargin;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, expectedHeight);
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                height = expectedHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    private void measureAddPictureView(int pictureWidth) {
        mAddPictureView.measure(
                MeasureSpec.makeMeasureSpec(pictureWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(pictureWidth, MeasureSpec.EXACTLY));
    }

    private int getPictureViewHeight() {
        return getChildAt(0).getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int left = mAttrDeleteButtonMargin;
        int top = 0;

        for (int i = 0; i < childCount; i++) {
            View pictureView = getChildAt(i);
            pictureView.layout(
                    left,
                    top,
                    left + pictureView.getMeasuredWidth(),
                    top + pictureView.getMeasuredHeight());

            left += pictureView.getMeasuredWidth() + mAttrPadding;

            if ((i + 1) % mAttrRowCount == 0) {
                left = mAttrDeleteButtonMargin;
                top += pictureView.getMeasuredHeight() + mAttrPadding;
            }
        }

    }

    private void measureAllPictureView(int pictureWidth) {
        if (mPictureViewHolders.size() == 0) {
            return;
        }

        for (PictureViewHolder holder : mPictureViewHolders) {
            holder.getView()
                    .measure(
                            MeasureSpec.makeMeasureSpec(pictureWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }
    }

    private int getLines() {
        return ((getChildCount() - 1) / mAttrRowCount) + 1;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SaveState saveState = new SaveState(super.onSaveInstanceState());
        saveState.mPictures = mPictures;
        saveState.mIsAddMode = mIsAddMode;
        saveState.mIsDeleteMode = mIsDeleteMode;
        return saveState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SaveState saveState = (SaveState) state;
        setPictures(saveState.mPictures);
        setAddMode(saveState.mIsAddMode);
        setDeleteMode(saveState.mIsDeleteMode);
        super.onRestoreInstanceState(((SaveState) state).getSuperState());
    }

    public List<String> getPictures() {
        return mPictures;
    }

    public void setPictures(List<String> picture) {
        mPictures.clear();
        if (picture.size() > mPictureMax) {
            mPictures.addAll(picture.subList(0, mPictureMax));
        } else {
            mPictures.addAll(picture);
        }
        setupView();
    }

    public void removePicture(int position) {
        mPictures.remove(position);
        setupView();
    }

    public void addPicture(@NonNull String string) {
        if (mPictures.size() < mPictureMax) {
            mPictures.add(string);
            setupView();
        }
    }

    public boolean isDeleteMode() {
        return mIsDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        if (mIsDeleteMode != deleteMode) {
            for (PictureViewHolder holder : mPictureViewHolders) {
                holder.setDelete(deleteMode);
            }
        }

        mIsDeleteMode = deleteMode;
    }

    public boolean isAddMode() {
        return mIsAddMode;
    }

    public void setAddMode(boolean addMode) {
        if (mIsAddMode != addMode) {
            if (addMode && mPictures.size() < mPictureMax) {
                if (!mAddPictureView.isAttachedToWindow()) {
                    addView(mAddPictureView);
                }
            } else {
                removeView(mAddPictureView);
            }
        }

        mIsAddMode = addMode;
    }

    private void setupView() {
        setupPictureView();
        setupAddPictureView();
        initPictureView();
        requestLayout();
    }

    private void setupPictureView() {
        cacheAllPictureView(mPictureViewHolders);
        removeAllViewsInLayout();
        mPictureViewHolders.clear();

        for (int i = 0; i < mPictures.size(); i++) {
            View pictureView = getPictureView();
            addViewInLayout(pictureView, -1, pictureView.getLayoutParams(), true);
            mPictureViewHolders.add(new PictureViewHolder(pictureView, i));
        }
    }

    private void cacheAllPictureView(List<PictureViewHolder> pictureViewHolders) {
        for (PictureViewHolder holder : pictureViewHolders) {
            mViewRecycler.recycle(holder.getView());
        }
    }

    private View getPictureView() {
        View view = mViewRecycler.get();
        if (view == null) {
            view = createPictureView();
        }
        return view;
    }

    private View createPictureView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_item_gallery, this, false);
    }

    private void setupAddPictureView() {
        if (mIsAddMode) {
            if (mPictures.size() < mPictureMax) {
                addViewInLayout(mAddPictureView, -1, mAddPictureView.getLayoutParams());
            }
        }
    }

    private void initPictureView() {
        for (int i = 0; i < mPictureViewHolders.size(); i++) {
            PictureViewHolder pictureViewHolder = mPictureViewHolders.get(i);
            pictureViewHolder.setPicture(mPictures.get(i));
            pictureViewHolder.setDelete(mIsDeleteMode);
            if (mOnClickPictureListener != null) {
                pictureViewHolder.setClickAble(true);
            } else {
                pictureViewHolder.setClickAble(false);
            }

            if (isInEditMode()) {
                highlightInEditMode(pictureViewHolder);
            }
        }
    }

    private void highlightInEditMode(PictureViewHolder pictureViewHolder) {
        pictureViewHolder.mPicture.setBackground(
                new ColorDrawable(ColorUtils.setAlphaComponent(Globals.getPrimaryColor(getContext()), 0x10)));
    }

    private void onClickPicture(int position) {
        if (position == -1) {
            if (mOnClickAddPictureListener != null) {
                mOnClickAddPictureListener.onClick(this);
            }
        } else {
            if (mOnClickPictureListener != null) {
                mOnClickPictureListener.onClick(this, position, mPictures.get(position));
            }
        }
    }

    public OnClickPictureListener getOnClickPictureListener() {
        return mOnClickPictureListener;
    }

    public void setOnClickPictureListener(OnClickPictureListener onClickPictureListener) {
        mOnClickPictureListener = onClickPictureListener;

        if (mOnClickPictureListener == null) {
            for (PictureViewHolder holder : mPictureViewHolders) {
                holder.setClickAble(false);
            }
        } else {
            for (PictureViewHolder holder : mPictureViewHolders) {
                holder.setClickAble(true);
            }
        }
    }

    public OnClickAddPictureListener getOnClickAddPictureListener() {
        return mOnClickAddPictureListener;
    }

    public void setOnClickAddPictureListener(OnClickAddPictureListener onClickAddPictureListener) {
        mOnClickAddPictureListener = onClickAddPictureListener;

        if (mOnClickAddPictureListener == null) {
            setAddPictureViewClickAble(false);
        } else {
            setAddPictureViewClickAble(true);
        }
    }

    private void setAddPictureViewClickAble(boolean clickAble) {

        ImageView picture = mAddPictureView.findViewById(R.id.gallery_picture);

        if (picture == null) {
            mAddPictureView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickPicture(-1);
                        }
                    });
            return;
        }

        if (clickAble) {
            picture.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickPicture(-1);
                        }
                    });
        } else {
            picture.setOnClickListener(null);
            picture.setClickable(false);
        }
    }

    private class PictureViewHolder {

        private final View mView;

        private final ImageView mPicture;

        private final ImageView mDelete;

        private final Drawable mRippleForeGround;

        private int mPosition = 0;

        PictureViewHolder(@NonNull View pictureView, int position) {
            mView = pictureView;
            mPicture = pictureView.findViewById(R.id.gallery_picture);
            mDelete = pictureView.findViewById(R.id.gallery_delete);
            mPosition = position;

            mRippleForeGround = Globals.addRipple(
                    getContext(),
                    new ColorDrawable(Color.TRANSPARENT),
                    new ColorDrawable(Color.BLACK));

            mPicture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickPicture(mPosition);
                }
            });

            addDeleteButtonMargin();
            mDelete.setImageDrawable(mAttrDeleteDrawable);
            makeDeleteDrawableGravityEndTop();
        }

        private void addDeleteButtonMargin() {
            ((MarginLayoutParams) mPicture.getLayoutParams()).setMarginEnd(mAttrDeleteButtonMargin);
            ((MarginLayoutParams) mPicture.getLayoutParams()).topMargin = mAttrDeleteButtonMargin;
        }

        private void makeDeleteDrawableGravityEndTop() {
            int paddingStart = 0, paddingBottom = 0;
            if (mDelete.getMinimumHeight() > mAttrDeleteDrawable.getIntrinsicWidth()) {
                paddingStart = mDelete.getMinimumWidth() - mAttrDeleteDrawable.getIntrinsicHeight();
            }

            if (mDelete.getMinimumHeight() > mAttrDeleteDrawable.getIntrinsicHeight()) {
                paddingBottom =
                        mDelete.getMinimumHeight() - mAttrDeleteDrawable.getIntrinsicHeight();
            }
            mDelete.setPadding(paddingStart, 0, 0, paddingBottom);
        }

        @NonNull
        View getView() {
            return mView;
        }

        void setPicture(String picture) {
            if (!isInEditMode()) {
                Glide.with(Gallery.this).load(picture).apply(mGlideOption).into(mPicture);
            } else {
                mPicture.setImageDrawable(mAttrPlaceholder);
            }
        }

        void setDelete(boolean enable) {
            if (enable) {
                mDelete.setVisibility(View.VISIBLE);
                mDelete.setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removePicture(mPosition);
                            }
                        });
            } else {
                mDelete.setVisibility(View.GONE);
                mDelete.setOnClickListener(null);
            }
        }

        void setClickAble(boolean clickAble) {
            setRippleForeground(clickAble);
        }

        private void setRippleForeground(boolean clickAble) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (clickAble) {
                    mPicture.setForeground(mRippleForeGround);
                } else {
                    mPicture.setForeground(null);
                }
            }
        }

        void setDrawable(Drawable drawable) {
            mPicture.setImageDrawable(drawable);
        }
    }

    public interface OnClickPictureListener {

        void onClick(Gallery gallery, int position, String picture);
    }

    public interface OnClickAddPictureListener {

        void onClick(Gallery gallery);
    }

    static class SaveState extends BaseSavedState {

        public static final Creator<SaveState> CREATOR =
                new Creator<SaveState>() {
                    public SaveState createFromParcel(Parcel in) {
                        return new SaveState(in);
                    }

                    public SaveState[] newArray(int size) {
                        return new SaveState[size];
                    }
                };

        private boolean mIsAddMode;

        private boolean mIsDeleteMode;

        private List<String> mPictures;

        SaveState(Parcelable superState) {
            super(superState);
        }

        SaveState(Parcel source) {
            super(source);

            boolean[] booleans = new boolean[2];
            source.readBooleanArray(booleans);

            mIsAddMode = booleans[0];
            mIsDeleteMode = booleans[1];

            mPictures = new ArrayList<>();
            source.readStringList(mPictures);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeBooleanArray(new boolean[]{mIsAddMode, mIsDeleteMode});
            out.writeList(mPictures);
        }
    }
}

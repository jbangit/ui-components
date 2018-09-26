package com.jbangit.uicomponents.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.DensityUtils;
import com.jbangit.uicomponents.common.Globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TextTab extends ViewTab {

    public static final int STYLE_DRY = 0;

    public static final int STYLE_FILL = 1;

    public static final int STYLE_STRONG = 2;

    private List<CharSequence> mAttrTitles;

    @ColorInt
    private int mAttrSelectedTextColor = getResources().getColor(R.color.colorTextDark);

    @ColorInt
    private int mAttrUnselectedTextColor = getResources().getColor(R.color.colorTextDark);

    @ColorInt
    private int mAttrSelectedColor;

    @ColorInt
    private int mAttrUnselectedColor;

    private int mAttrVPadding = DensityUtils.getPxFromDp(getContext(), 12);

    private int mAttrHPadding = DensityUtils.getPxFromDp(getContext(), 12);

    private int mAttrTextSize = DensityUtils.getPxFromSp(getContext(), 16);

    private int mAttrTextGravity = Gravity.CENTER;

    private boolean mAttrIsTextBold = false;

    private List<CharSequence> mTitle = new ArrayList<>();

    public TextTab(Context context) {
        super(context);
    }

    public TextTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        setTitles(mAttrTitles);
    }

    public TextTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setTitles(mAttrTitles);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextTab);

        CharSequence[] titles = typedArray.getTextArray(R.styleable.TextTab_textTabTitles);
        if (titles != null) {
            mAttrTitles = Arrays.asList(titles);
        } else {
            mAttrTitles = Collections.emptyList();
        }

        switch (typedArray.getInt(R.styleable.TextTab_textTabStyle, STYLE_DRY)) {
            case STYLE_DRY:
                mAttrUnselectedTextColor = getResources().getColor(R.color.colorTextGray);
                break;
            case STYLE_FILL:
                mAttrSelectedColor = getResources().getColor(R.color.colorForeground);
                break;
            case STYLE_STRONG:
                mAttrSelectedTextColor = getResources().getColor(R.color.colorForeground);
                mAttrSelectedColor = Globals.getPrimaryColor(getContext());
                break;
        }

        mAttrSelectedTextColor =
                typedArray.getColor(
                        R.styleable.TextTab_textTabSelectedTextColor, mAttrSelectedTextColor);
        mAttrUnselectedTextColor =
                typedArray.getColor(
                        R.styleable.TextTab_textTabUnselectedTextColor, mAttrUnselectedTextColor);

        mAttrSelectedColor =
                typedArray.getColor(R.styleable.TextTab_textTabSelectedColor, mAttrSelectedColor);
        mAttrUnselectedColor =
                typedArray.getColor(
                        R.styleable.TextTab_textTabUnselectedColor, mAttrUnselectedColor);

        mAttrVPadding =
                typedArray.getDimensionPixelSize(
                        R.styleable.TextTab_textTabVPadding, mAttrVPadding);
        mAttrHPadding =
                typedArray.getDimensionPixelSize(
                        R.styleable.TextTab_textTabHPadding, mAttrHPadding);

        mAttrTextSize =
                typedArray.getDimensionPixelSize(
                        R.styleable.TextTab_textTabTextSize, mAttrTextSize);

        mAttrTextGravity =
                typedArray.getInt(
                        R.styleable.TextTab_textTabTextGravity, mAttrTextGravity);

        mAttrIsTextBold =
                typedArray.getBoolean(
                        R.styleable.TextTab_textTabTextBold, mAttrIsTextBold);
        typedArray.recycle();

        setCurrentItem(0);
    }

    /**
     * @param titles set empty list to clear all tab
     */
    public void setTitles(Collection<CharSequence> titles) {
        if (mTitle.size() != 0) {
            mTitle.clear();
        }

        mTitle.addAll(titles);
        setAdapter();
    }

    private void setAdapter() {
        setAdapter(
                new ViewTabAdapter() {
                    @Override
                    public View getItemView(@NonNull ViewGroup container, int position) {
                        View layout =
                                LayoutInflater.from(getContext())
                                        .inflate(R.layout.view_item_text_tab, container, false);
                        TextView title = layout.findViewById(R.id.title);
                        title.setTextColor(mAttrUnselectedTextColor);
                        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrTextSize);
                        title.setGravity(mAttrTextGravity);
                        title.setPadding(
                                mAttrHPadding, mAttrVPadding, mAttrHPadding, mAttrVPadding);
                        title.setText(mTitle.get(position));
                        if (mAttrIsTextBold) {
                            title.setTypeface(null, Typeface.BOLD);
                        }

                        layout.setBackgroundColor(mAttrUnselectedColor);

                        return layout;
                    }

                    @Override
                    public void onSelected(@NonNull View item, int position, boolean selected) {
                        item.setBackgroundColor(
                                selected ? mAttrSelectedColor : mAttrUnselectedColor);
                        ((TextView) item.findViewById(R.id.title))
                                .setTextColor(
                                        selected
                                                ? mAttrSelectedTextColor
                                                : mAttrUnselectedTextColor);
                    }

                    @Override
                    public int getCount() {
                        return mTitle.size();
                    }
                });
    }
}

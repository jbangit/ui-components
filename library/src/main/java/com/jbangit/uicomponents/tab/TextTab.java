package com.jbangit.uicomponents.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbangit.uicomponents.R;

import java.util.Arrays;
import java.util.List;

public class TextTab extends ViewTab {

    private List<CharSequence> mAttrTitles;

    @ColorInt
    private int mColorTextUnselected;

    @ColorInt
    private int mColorTextSelected;

    public TextTab(Context context) {
        super(context);
    }

    public TextTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        setupAdapter();
        setCurrentItem(0, false);
    }

    public TextTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setupAdapter();
        setCurrentItem(0, false);
    }

    private void setupAdapter() {
        setAdapter(
                new ViewTabAdapter() {
                    @Override
                    public View getItemView(@NonNull ViewGroup container, int position) {
                        View item =
                                LayoutInflater.from(getContext())
                                        .inflate(R.layout.view_item_text_tab, container, false);
                        TextView title = item.findViewById(R.id.title);
                        title.setTextColor(mColorTextUnselected);
                        title.setText(mAttrTitles.get(position));
                        return item;
                    }

                    @Override
                    public void onSelected(@NonNull View item, int position, boolean selected) {
                        ((TextView) item.findViewById(R.id.title))
                                .setTextColor(selected ? mColorTextSelected : mColorTextUnselected);
                    }

                    @Override
                    public int getCount() {
                        return mAttrTitles.size();
                    }
                });
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextTab);

        mAttrTitles = Arrays.asList(typedArray.getTextArray(R.styleable.TextTab_textTabTitles));

        mColorTextUnselected = getContext().getResources().getColor(R.color.colorTextGray);
        mColorTextSelected = getContext().getResources().getColor(R.color.colorTextDark);

        typedArray.recycle();
    }
}

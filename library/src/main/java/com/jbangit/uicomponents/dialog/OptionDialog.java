package com.jbangit.uicomponents.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.dialog.BottomDialogFragment;
import com.jbangit.uicomponents.common.dialog.FragmentUtils;
import com.jbangit.uicomponents.common.dialog.State;

import java.util.ArrayList;
import java.util.Collection;

/** {@link OptionDialog#show(Fragment, int, Collection, int)} */
public class OptionDialog extends BottomDialogFragment {

    @State private int mIndex = 0;

    @State private ArrayList<String> mOptions;

    public static void show(
            Fragment fragment, int requestCode, Collection<String> options, int defaultIndex) {
        OptionDialog dialog = new OptionDialog();
        dialog.mIndex = defaultIndex;
        dialog.mOptions = new ArrayList<>(options);
        FragmentUtils.show(dialog, fragment, requestCode);
    }

    public static void show(
            FragmentActivity activity,
            int requestCode,
            Collection<String> options,
            int defaultIndex) {
        OptionDialog dialog = new OptionDialog();
        dialog.mIndex = defaultIndex;
        dialog.mOptions = new ArrayList<>(options);
        FragmentUtils.show(dialog, activity, requestCode);
    }

    public int getIndex() {
        return mIndex;
    }

    public String getOption() {
        return mOptions.get(mIndex);
    }

    @Override
    public View onCreateContentView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentUtils.restoreState(this);

        View layout = inflater.inflate(R.layout.dialog_option, container, false);
        initHeaderLayout(layout);
        initOptionView(layout);

        return layout;
    }

    private void initHeaderLayout(View layout) {
        int primaryColor = Globals.getPrimaryColor(layout.getContext());
        TextView ok = layout.findViewById(R.id.ok);
        TextView cancel = layout.findViewById(R.id.cancel);

        ok.setTextColor(primaryColor);
        cancel.setTextColor(primaryColor);

        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(OptionDialog.this, true);
                        dismiss();
                    }
                });
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(OptionDialog.this, false);
                        dismiss();
                    }
                });
    }

    private void initOptionView(View layout) {
        WheelView option = layout.findViewById(R.id.option);

        option.setAdapter(new ArrayWheelAdapter<>(mOptions));
        option.setCurrentItem(mIndex);
        option.setCyclic(false);
        option.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        mIndex = index;
                    }
                });
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        FragmentUtils.setResult(OptionDialog.this, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentUtils.saveState(this);
    }
}

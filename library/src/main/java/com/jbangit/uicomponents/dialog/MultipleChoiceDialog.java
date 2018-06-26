package com.jbangit.uicomponents.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.Globals;
import com.jbangit.uicomponents.common.dialog.BottomDialogFragment;
import com.jbangit.uicomponents.common.dialog.FragmentUtils;
import com.jbangit.uicomponents.common.dialog.State;
import com.jbangit.uicomponents.gridradiogroup.GridRadioGroup;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link MultipleChoiceDialog#show(FragmentActivity, int, Collection)}
 * <p>
 * <p>{@link MultipleChoiceDialog#show(Fragment, int, Collection)}
 * <p>
 * <p>{@link MultipleChoiceDialog#getChoicesIndexes()}
 * <p>
 * <p>{@link MultipleChoiceDialog#getChosenItem()}
 */
public class MultipleChoiceDialog extends BottomDialogFragment {

    @State
    private ArrayList<String> mChoices;

    private GridRadioGroup mChoice;

    public static void show(
            FragmentActivity activity, int requestCode, Collection<String> choices) {
        MultipleChoiceDialog dialog = new MultipleChoiceDialog();
        dialog.mChoices = new ArrayList<>(choices);
        FragmentUtils.show(dialog, activity, requestCode);
    }

    public static void show(Fragment fragment, int requestCode, Collection<String> choices) {
        MultipleChoiceDialog dialog = new MultipleChoiceDialog();
        dialog.mChoices = new ArrayList<>(choices);
        FragmentUtils.show(dialog, fragment, requestCode);
    }

    @Override
    public View onCreateContentView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentUtils.restoreState(this);

        View layout = inflater.inflate(R.layout.dialog_multiple_choice, container, false);
        initHeaderLayout(layout);
        initChoicesView(layout);

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
                        FragmentUtils.setResult(MultipleChoiceDialog.this, true);
                        dismiss();
                    }
                });
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(MultipleChoiceDialog.this, false);
                        dismiss();
                    }
                });
    }

    private void initChoicesView(View layout) {
        mChoice = layout.findViewById(R.id.choices);
        mChoice.setItem(mChoices);
    }

    public Collection<Integer> getChoicesIndexes() {
        return mChoice.getCheckedIndexes();
    }

    public Collection<String> getChosenItem() {
        return mChoice.getCheckedItems();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        FragmentUtils.setResult(MultipleChoiceDialog.this, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentUtils.saveState(this);
    }
}

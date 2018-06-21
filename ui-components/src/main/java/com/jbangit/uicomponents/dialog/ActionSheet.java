package com.jbangit.uicomponents.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbangit.uicomponents.R;
import com.jbangit.uicomponents.common.dialog.BottomDialogFragment;
import com.jbangit.uicomponents.common.dialog.FragmentUtils;
import com.jbangit.uicomponents.common.dialog.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * {@link ActionSheet#show(Fragment, int, Collection)}
 * <p>
 * <p>{@link ActionSheet#show(FragmentActivity, int, Collection)}
 * <p>
 * <p>{@link ActionSheet#getAction()}
 * <p>
 * <p>{@link ActionSheet#getActionIndex()}
 */
public class ActionSheet extends BottomDialogFragment {

    @State
    private int mIndex = 0;

    @State
    private ArrayList<String> mActions;

    public static void show(
            FragmentActivity activity, int requestCode, Collection<String> actions) {
        ActionSheet actionSheet = new ActionSheet();
        actionSheet.mActions = new ArrayList<>(actions);
        FragmentUtils.show(actionSheet, activity, requestCode);
    }

    public static void show(Fragment fragment, int requestCode, Collection<String> actions) {
        ActionSheet actionSheet = new ActionSheet();
        actionSheet.mActions = new ArrayList<>(actions);
        FragmentUtils.show(actionSheet, fragment, requestCode);
    }

    @Override
    public View onCreateContentView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentUtils.restoreState(this);
        View layout = inflater.inflate(R.layout.dialog_action_sheet, container, false);
        initActionsView(layout);
        initLayout(layout);
        return layout;
    }

    private void initActionsView(View layout) {
        LinearLayout actionsContainer = layout.findViewById(R.id.actions);

        Iterator<String> iterator = mActions.iterator();

        int index = 0;
        addActionView(actionsContainer, iterator.next(), index);
        index++;

        while (iterator.hasNext()) {
            addDivider(actionsContainer);
            addActionView(actionsContainer, iterator.next(), index);
            index++;
        }
    }

    private void initLayout(View layout) {
        View cancel = layout.findViewById(R.id.cancel);
        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentUtils.setResult(ActionSheet.this, false);
                        dismiss();
                    }
                });
    }

    private void addActionView(LinearLayout actionsContainer, String action, final int index) {
        View actionView =
                getLayoutInflater()
                        .inflate(R.layout.view_item_action_sheet, actionsContainer, false);
        actionsContainer.addView(actionView);

        ((TextView) actionView.findViewById(R.id.action)).setText(action);
        actionView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIndex = index;
                        FragmentUtils.setResult(ActionSheet.this, true);
                        dismiss();
                    }
                });
    }

    private void addDivider(LinearLayout actionsContainer) {
        View divider =
                getLayoutInflater()
                        .inflate(R.layout.view_divider_action_sheet, actionsContainer, false);
        actionsContainer.addView(divider);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentUtils.saveState(this);
    }

    @NonNull
    public String getAction() {
        return mActions.get(mIndex);
    }

    public int getActionIndex() {
        return mIndex;
    }
}

package com.jbangit.uicomponents.common.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import java.util.LinkedList;

public class ViewRecycler {

    private SparseArray<LinkedList<View>> mRecycler = new SparseArray<>();

    public void recycle(@NonNull View view) {
        recycle(view, 0);
    }

    /**
     * Recycle view
     */
    public void recycle(@NonNull View view, int viewType) {
        LinkedList<View> viewList = mRecycler.get(viewType);
        if (viewList == null) {
            viewList = new LinkedList<>();
            mRecycler.put(viewType, viewList);
        }

        viewList.add(view);
    }

    /**
     * Get view from recycler
     *
     * @return null if there is no view in recycler, you should create
     */
    @Nullable
    public View get(int viewType) {
        LinkedList<View> viewList = mRecycler.get(viewType);

        if (viewList != null) {
            return viewList.pollFirst();
        } else {
            return null;
        }
    }

    @Nullable
    public View get() {
        return get(0);
    }
}

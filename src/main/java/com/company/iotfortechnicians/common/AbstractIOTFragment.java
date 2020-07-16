/*
* Software Name : IoT for technicians
* Version: 1.0
*
* Copyright (c) 2020 Orange
*
* This software is distributed under the Apache License, Version 2.0,
* the text of which is available at http://www.apache.org/licenses/
* or see the "license.txt" file for more details.
*
*/

package com.company.iotfortechnicians.common;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.company.iotfortechnicians.common.util.FragmentUtils;

import java.io.Serializable;

public abstract class AbstractIOTFragment extends Fragment {

    protected void addGlobalLayoutListener(@IdRes int scrollViewId, View viewToHide) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            View contentView = activity.findViewById(android.R.id.content);
            View scrollView = activity.findViewById(scrollViewId);
            ViewTreeObserver viewTreeObserver = contentView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(getOnGlobalLayoutListener(contentView, scrollView, viewToHide));
        }
    }

    @NonNull
    private ViewTreeObserver.OnGlobalLayoutListener getOnGlobalLayoutListener(View contentView, View scrollView, View viewToHide) {
        return () -> {
            Rect r = new Rect();
            contentView.getWindowVisibleDisplayFrame(r);
            int screenHeight = contentView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * Constants.KEYBOARD_MAGIC_PARAM) {
                changeViewVisibility(viewToHide, View.GONE);
                scrollView.scrollTo(0, scrollView.getBottom());
            } else {
                changeViewVisibility(viewToHide, View.VISIBLE);
                scrollView.scrollTo(0, scrollView.getTop());
            }
        };
    }

    protected void closeKeyboard() {
        FragmentActivity activity = getActivity();
        FragmentUtils.closeKeyboard(activity);
    }

    private void changeViewVisibility(View view, int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    protected Serializable getSerializable(@Nullable String key) {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getSerializable(key) != null) {
            return arguments.getSerializable(key);
        }

        return null;
    }
}

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

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.company.iotfortechnicians.R;

public abstract class AbstractIOTFragmentActivity extends AbstractIOTActivity {

    private boolean attachFragmentWithoutScroll;

    public AbstractIOTFragmentActivity(boolean attachFragmentWithoutScroll) {
        this.attachFragmentWithoutScroll = attachFragmentWithoutScroll;
    }

    public AbstractIOTFragmentActivity() {
        this(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Fragment fragment = getFragment();
            int containerViewId = getContainerViewId();
            if (shouldBeAttachedWithoutScroll()) {
                containerViewId = R.id.contentLayout_without_scroll;
                setViewVisibility();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(containerViewId, fragment)
                    .commit();
        }
    }

    private boolean shouldBeAttachedWithoutScroll() {
        return attachFragmentWithoutScroll && getContainerViewId() == R.id.contentLayout;
    }

    private void setViewVisibility() {
        View scroll = findViewById(R.id.abstractFragmentScrollView);
        scroll.setVisibility(View.GONE);
        View view = findViewById(R.id.contentLayout_without_scroll);
        view.setVisibility(View.VISIBLE);
    }

    @IdRes
    protected int getContainerViewId() {
        return R.id.contentLayout;
    }

    protected abstract Fragment getFragment();

}

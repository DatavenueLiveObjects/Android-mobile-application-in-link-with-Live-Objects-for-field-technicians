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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.company.iotfortechnicians.R;
import com.company.iotfortechnicians.common.api.ApiClient;
import com.company.iotfortechnicians.common.service.AuthService;
import com.company.iotfortechnicians.menu.MenuActivity;

import java.util.HashMap;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractIOTAppBarActivity extends AbstractIOTFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    protected static final String MENU = "menu_key";
    protected static final String ABOUT = "about_key";
    protected static final String SUPPORT = "support_key";
    protected static final String OTHER = "other_key";

    public AbstractIOTAppBarActivity(boolean attachFragmentWithoutScroll) {
        super(attachFragmentWithoutScroll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        drawer.addDrawerListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_menu_home:
                if (!type().equals(MENU)) {
                    startActivity(new Intent(AbstractIOTAppBarActivity.this, MenuActivity.class));
                    return true;
                }
                break;
            case R.id.nav_menu_logout:
                AuthService authService = ApiClient.createApiService(AuthService.class);
                authService.logout(new HashMap<>()).enqueue(new LogoutCallback(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
        //Not needed
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        //Not needed
    }

    @Override
    public void onDrawerStateChanged(int i) {
        //Not needed
    }

    protected abstract Fragment getFragment();

    @IdRes
    protected int getContainerViewId() {
        return R.id.contentLayout;
    }

    @Override
    protected int getContentView() {
        return R.layout.abstract_fragment_activity;
    }

    protected String type() {
        return OTHER;
    }
}
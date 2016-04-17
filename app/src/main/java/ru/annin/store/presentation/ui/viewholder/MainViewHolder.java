/*
 * Copyright 2016, Pavel Annin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.annin.store.presentation.ui.viewholder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.annin.store.R;
import ru.annin.store.presentation.common.BaseViewHolder;

/**
 * BaseViewHolder главного экрана.
 *
 * @author Pavel Annin.
 */
public class MainViewHolder extends BaseViewHolder {

    // View's
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav)
    NavigationView navigation;

    private ActionBarDrawerToggle drawerToggle;

    // Listener's
    private OnClickListener listener;

    public MainViewHolder(@NonNull Activity activity, @NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
        // Setup
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                R.string.title_nav_drawer_open, R.string.title_nav_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigation.setNavigationItemSelectedListener(onNavigationClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawerLayout.removeDrawerListener(drawerToggle);
        ButterKnife.unbind(this);
    }

    public MainViewHolder showNavigation() {
        drawerLayout.openDrawer(GravityCompat.START);
        return this;
    }

    public MainViewHolder hideNavigation() {
        drawerLayout.closeDrawer(GravityCompat.START);
        return this;
    }

    public boolean isNavigationShowing() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationClickListener = item -> {
        if (listener != null) {
            switch (item.getItemId()) {
                case R.id.action_nav_units:
                    listener.onUnitsClick();
                    return true;
                case R.id.action_nav_about:
                    listener.onAboutClick();
                    return true;
                default:
                    break;
            }
        }
        return false;
    };

    public interface OnClickListener {
        void onGitHubClick();
        void onUnitsClick();
        void onAboutClick();
    }
}

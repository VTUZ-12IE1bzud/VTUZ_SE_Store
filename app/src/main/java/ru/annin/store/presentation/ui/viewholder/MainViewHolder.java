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

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import io.realm.RealmResults;
import ru.annin.store.R;
import ru.annin.store.domain.model.StoreModel;
import ru.annin.store.presentation.common.BaseViewHolder;
import ru.annin.store.presentation.ui.adapter.ChartPagerAdapter;
import ru.annin.store.presentation.ui.adapter.StoreSelectAdapter;

/**
 * BaseViewHolder главного экрана.
 *
 * @author Pavel Annin.
 */
public class MainViewHolder extends BaseViewHolder {

    // View's
    private final Toolbar toolbar;
    private final DrawerLayout drawerLayout;
    private final NavigationView navigation;
    private final Spinner spStore;
    private final TabLayout tabLayout;
    private final ViewPager pager;

    private ActionBarDrawerToggle drawerToggle;

    // Adapter's
    private final StoreSelectAdapter storeAdapter;

    // Listener's
    private OnClickListener listener;

    public MainViewHolder(@NonNull AppCompatActivity activity, @NonNull View view) {
        super(view);
        toolbar = (Toolbar) vRoot.findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) vRoot.findViewById(R.id.drawer_layout);
        navigation = (NavigationView) vRoot.findViewById(R.id.nav);
        spStore = (AppCompatSpinner) navigation.getHeaderView(0).findViewById(R.id.sp_store);
        tabLayout = (TabLayout) vRoot.findViewById(R.id.tabs);
        pager = (ViewPager) vRoot.findViewById(R.id.pager_container);

        // Setup
        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                R.string.title_nav_drawer_open, R.string.title_nav_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        storeAdapter = new StoreSelectAdapter(vRoot.getContext());
        spStore.setOnItemSelectedListener(onStoreListener);
        navigation.setNavigationItemSelectedListener(onNavigationClickListener);
        spStore.setAdapter(storeAdapter);

        ChartPagerAdapter chartPagerAdapter = new ChartPagerAdapter(activity.getSupportFragmentManager());
        pager.setAdapter(chartPagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    public MainViewHolder showStore(RealmResults<StoreModel> data, String selectId) {
        showStore(data);
        int selectPosition = storeAdapter.getPosition(selectId);
        spStore.setSelection(selectPosition);
        return this;
    }

    public MainViewHolder showStore(RealmResults<StoreModel> data) {
        storeAdapter.updateData(data);
        return this;
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

    private final NavigationView.OnNavigationItemSelectedListener onNavigationClickListener = item -> {
        if (listener != null) {
            switch (item.getItemId()) {
                case R.id.action_nav_invoice:
                    listener.onNavInvoiceClick();
                    return true;
                case R.id.action_nav_nomenclature:
                    listener.onNavNomenclatureClick();
                    return true;
                case R.id.action_nav_store:
                    listener.onNavStoreClick();
                    return true;
                case R.id.action_nav_units:
                    listener.onNavUnitsClick();
                    return true;
                case R.id.action_nav_about:
                    listener.onNavAboutClick();
                    return true;
                default:
                    break;
            }
        }
        return false;
    };

    private final AdapterView.OnItemSelectedListener onStoreListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String storeId = storeAdapter.getId(position);
            if (listener != null) {
                listener.onStoreSelect(storeId);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public interface OnClickListener {
        void onNavInvoiceClick();
        void onNavNomenclatureClick();
        void onNavStoreClick();
        void onNavUnitsClick();
        void onNavAboutClick();
        void onStoreSelect(String storeId);
    }
}

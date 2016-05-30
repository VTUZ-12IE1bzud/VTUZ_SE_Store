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

package ru.annin.store.presentation.ui.activity;

import android.os.Bundle;

import ru.annin.store.R;
import ru.annin.store.data.repository.SettingsRepositoryImpl;
import ru.annin.store.data.repository.StoreRepositoryImpl;
import ru.annin.store.presentation.common.BaseActivity;
import ru.annin.store.presentation.presenter.MainPresenter;
import ru.annin.store.presentation.ui.view.MainView;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder;

/**
 * <p>Главный экран.</p>
 *
 * @author Pavel Annin.
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewHolder viewHolder = new MainViewHolder(this, findViewById(R.id.drawer_layout));
        presenter = new MainPresenter(new StoreRepositoryImpl(), new SettingsRepositoryImpl(this));
        presenter.setViewHolder(viewHolder);
        presenter.setView(this);
        presenter.onInitialization();
    }

    @Override
    public void onBackPressed() {
        if (presenter != null && presenter.onBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onInvoiceOpen() {
        navigator.navigate2Invoice(this);
    }

    @Override
    public void onNomenclatureOpen() {
        navigator.navigate2Nomenclature(this);
    }

    @Override
    public void onStoresOpen() {
        navigator.navigate2Stores(this);
    }

    @Override
    public void onUnitsOpen() {
        navigator.navigate2Units(this);
    }

    @Override
    public void onAboutOpen() {
        navigator.navigate2About(this);
    }
}

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.annin.store.R;
import ru.annin.store.data.repository.StoreRepositoryImpl;
import ru.annin.store.presentation.common.BaseActivity;
import ru.annin.store.presentation.presenter.StorePresenter;
import ru.annin.store.presentation.ui.view.StoreView;
import ru.annin.store.presentation.ui.viewholder.StoreViewHolder;

/**
 * Экран "Склады".
 *
 * @author Pavel Annin.
 */
public class StoreActivity extends BaseActivity<StorePresenter> implements StoreView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        StoreViewHolder viewHolder = new StoreViewHolder(findViewById(R.id.main_container));
        presenter = new StorePresenter(new StoreRepositoryImpl());
        presenter.setViewHolder(viewHolder);
        presenter.setView(this);
        presenter.onInitialization();
    }

    @Override
    public void onCreateStoreOpen() {
        navigator.navigate2CreateStore(this);
    }

    @Override
    public void onStoreOpen(@NonNull String storeId) {
        navigator.navigate2OpenStore(this, storeId);
    }

    @Override
    public void onFinish() {
        finish();
    }
}

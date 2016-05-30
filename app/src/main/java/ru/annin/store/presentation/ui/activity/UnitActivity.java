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
import ru.annin.store.data.repository.UnitRepositoryImpl;
import ru.annin.store.presentation.common.BaseActivity;
import ru.annin.store.presentation.presenter.UnitPresenter;
import ru.annin.store.presentation.ui.view.UnitView;
import ru.annin.store.presentation.ui.viewholder.UnitViewHolder;

/**
 * <p>Экран "Единиц измерения".</p>
 *
 * @author Pavel Annin.
 */
public class UnitActivity extends BaseActivity<UnitPresenter> implements UnitView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        UnitViewHolder viewHolder  = new UnitViewHolder(findViewById(R.id.main_container));
        presenter = new UnitPresenter(new UnitRepositoryImpl());
        presenter.setViewHolder(viewHolder);
        presenter.setView(this);
        presenter.onInitialization();
    }

    @Override
    public void onCreateUnitOpen() {
        navigator.navigate2CreateUnit(this);
    }

    @Override
    public void onUnitOpen(@NonNull String unitId) {
        navigator.navigate2OpenUnit(this, unitId);
    }

    @Override
    public void onFinish() {
        finish();
    }
}

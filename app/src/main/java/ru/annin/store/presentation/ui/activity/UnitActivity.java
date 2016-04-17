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
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.annin.store.R;
import ru.annin.store.presentation.presenter.UnitPresenter;
import ru.annin.store.presentation.ui.view.UnitView;
import ru.annin.store.presentation.ui.viewholder.UnitViewHolder;

/**
 * Экран "Единиц измерения".
 *
 * @author Pavel Annin.
 */
public class UnitActivity extends BaseActivity implements UnitView {

    @Inject
    UnitPresenter mPresenter;
    @Bind(R.id.main_container)
    View mainView;
    private UnitViewHolder mViewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);
        getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        mViewHolder = new UnitViewHolder(mainView);
        mPresenter.setViewHolder(mViewHolder);
        mPresenter.setView(this);
        mPresenter.onInitialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mViewHolder.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreateUnitOpen() {
        mNavigator.navigate2CreateUnit(this);
    }

    @Override
    public void onUnitOpen(@NonNull String unitId) {
        mNavigator.navigate2OpenUnit(this, unitId);
    }

    @Override
    public void onFinish() {
        finish();
    }
}

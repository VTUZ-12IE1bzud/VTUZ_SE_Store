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

package ru.annin.store.presentation.presenter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import ru.annin.store.R;
import ru.annin.store.presentation.StoreApplication;
import ru.annin.store.presentation.di.ApplicationComponent;
import ru.annin.store.presentation.presenter.presenter.AboutPresenter;
import ru.annin.store.presentation.presenter.view.AboutView;
import ru.annin.store.presentation.presenter.viewholder.AboutViewHolder;

/**
 * Экран с информацией о приложении: назначение, разработчиков.
 *
 * @author Pavel Annin.
 */
public class AboutActivity extends AppCompatActivity implements AboutView {

    @Inject
    AboutPresenter mPresenter;
    private AboutViewHolder mViewHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setContentView(R.layout.activity_about);
        mViewHolder = new AboutViewHolder(findViewById(R.id.main_container));
        mPresenter.setView(this);
        mPresenter.setViewHolder(mViewHolder);
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
    }

    private ApplicationComponent getApplicationComponent() {
        return ((StoreApplication)getApplication()).getApplicationComponent();
    }

    @Override
    public void onFinish() {
        finish();
    }
}

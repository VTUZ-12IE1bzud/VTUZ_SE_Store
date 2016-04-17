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
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.annin.store.R;
import ru.annin.store.presentation.presenter.MainPresenter;
import ru.annin.store.presentation.ui.view.MainView;
import ru.annin.store.presentation.ui.viewholder.MainViewHolder;

/**
 * Главный экран.
 *
 * @author Pavel Annin.
 */
public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter mPresenter;
    @Bind(R.id.drawer_layout)
    View mainView;
    private MainViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        mViewHolder = new MainViewHolder(this, mainView);
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
    public void onBackPressed() {
        if (mPresenter.onBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onUnitsOpen() {
        mNavigator.navigate2Units(this);
    }

    @Override
    public void onAboutOpen() {
        mNavigator.navigate2About(this);
    }
}

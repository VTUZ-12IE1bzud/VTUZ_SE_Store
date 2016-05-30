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
import android.support.annotation.Nullable;

import ru.annin.store.R;
import ru.annin.store.presentation.common.BaseActivity;
import ru.annin.store.presentation.presenter.AboutPresenter;
import ru.annin.store.presentation.ui.view.AboutView;
import ru.annin.store.presentation.ui.viewholder.AboutViewHolder;

/**
 * <p>Экран с информацией о приложении, назначение.</p>
 *
 * @author Pavel Annin.
 */
public class AboutActivity extends BaseActivity<AboutPresenter> implements AboutView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AboutViewHolder viewHolder = new AboutViewHolder(findViewById(R.id.main_container));
        presenter = new AboutPresenter();
        presenter.setViewHolder(viewHolder);
        presenter.setView(this);
        presenter.onInitialization();
    }

    @Override
    public void onFinish() {
        finish();
    }
}

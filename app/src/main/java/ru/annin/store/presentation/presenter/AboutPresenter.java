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

package ru.annin.store.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.annin.store.BuildConfig;
import ru.annin.store.presentation.common.BasePresenter;
import ru.annin.store.presentation.ui.view.AboutView;
import ru.annin.store.presentation.ui.viewholder.AboutViewHolder;

/**
 * BasePresenter экрана "О программе".
 *
 * @author Pavel Annin.
 */
public class AboutPresenter extends BasePresenter<AboutViewHolder, AboutView> {

    public void onInitialization() {
        if (viewHolder != null) {
            viewHolder.showAppVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        }
    }

    @NonNull
    @Override
    public BasePresenter setViewHolder(@Nullable AboutViewHolder aboutViewHolder) {
        super.setViewHolder(aboutViewHolder);
        if (viewHolder != null) {
            viewHolder.setOnClickListener(onViewHolderClickListener);
        }
        return this;
    }

    private final AboutViewHolder.OnClickListener onViewHolderClickListener = new AboutViewHolder.OnClickListener() {
        @Override
        public void onNavBackClick() {
            if (view != null) {
                view.onFinish();
            }
        }
    };
}
